package com.web.demo.service.concretes;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.web.demo.dto.Request.UserRequestDto;
import com.web.demo.dto.Request.UserUpdateEmailRequestDto;
import com.web.demo.dto.Request.UserUpdatePasswordRequestDto;
import com.web.demo.dto.Request.UserUpdateRequestDto;
import com.web.demo.dto.Request.UserUpdateUsernameRequestDto;
import com.web.demo.dto.Response.UserResponseDto;
import com.web.demo.event.AuditEventPublisher;
import com.web.demo.exception.UserAlreadyExistsException;
import com.web.demo.mapper.UserMapper;
import com.web.demo.repository.abstracts.UserRepository;
import com.web.demo.security.JwtUtil;
import com.web.demo.service.abstracts.UserService;
import com.web.demo.util.GlobalContext;
import org.springframework.transaction.annotation.Transactional;
import com.web.demo.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import java.lang.String;
import java.util.List;
import java.util.stream.Collectors;
import com.web.demo.exception.AuthenticationException;

@Service
public class UserServiceManager implements  UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuditEventPublisher auditEventPublisher;
    private final JwtBlacklistService jwtBlacklistService;

    public UserServiceManager(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,AuditEventPublisher auditEventPublisher,JwtBlacklistService jwtBlacklistService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.auditEventPublisher = auditEventPublisher;
        this.jwtBlacklistService = jwtBlacklistService;
        
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto register(UserRequestDto requestDto) {
        // Email kontrolü
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserAlreadyExistsException("Bu email zaten kullanılıyor.");
        }

        // Parola şifreleme
        if (requestDto.getPassword() == null || requestDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Parola boş olamaz.");
        }
        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());

        // DTO'dan Entity oluştur
        User user = UserMapper.toEntity(requestDto, hashedPassword);

        // Veritabanına kaydet
        userRepository.save(user);

        logger(user,"REGISTER","Kullanici kaydi olusturuldu");
        
        // Entity'den Response DTO 
        return UserMapper.toDto(user);
    }

    public void logger(User user,String action, String details){
        // Audit loglama
        auditEventPublisher.publish(
            "USER", 
            user.getId(), 
            action, 
            details,
            GlobalContext.getCurrentUsername(),
            GlobalContext.getIpAddress(),
            GlobalContext.getUserAgent());
    }
    
    @Cacheable(value = "users")
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
    

    public UserResponseDto getUserById(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));
    }


    @Transactional
    @CacheEvict(value = "users", allEntries = true) // Cache temizleme
    public UserResponseDto updateUser(UserDetails userDetails, UserUpdateRequestDto requestDto) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(userDetails.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));

        if (requestDto.getEmail() != null &&
            !requestDto.getEmail().isEmpty() &&
            !user.getEmail().equals(requestDto.getEmail()) &&
            userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserAlreadyExistsException("Bu email zaten kullanılıyor.");
        }


        if (requestDto.getUsername() != null &&
            !requestDto.getUsername().isEmpty() &&
            !user.getUsername().equals(requestDto.getUsername()) &&
            userRepository.existsByUsername(requestDto.getUsername())) {
            throw new UserAlreadyExistsException("Bu kullanıcı adı zaten kullanılıyor.");
        }


        //Guncelleme islemi
        if (requestDto.getUsername() != null && !requestDto.getUsername().isEmpty()) {
            user.setUsername(requestDto.getUsername());
        }

        if (requestDto.getEmail() != null && !requestDto.getEmail().isEmpty()) {
            user.setEmail(requestDto.getEmail());
        }

        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        logger(user,"UPDATE","Kullanici kaydi guncellendi");

        return UserMapper.toDto(updatedUser);
    
    }


    @Transactional
    @CacheEvict(value = "users", allEntries = true) // Cache temizleme
    public void updatePassword(Long userId, UserUpdatePasswordRequestDto requestDto) {

       User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));

        // Eski şifre doğru mu kontrol et
        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Eski parola hatalı!");
        }

        // Yeni şifre encode edilip kaydedilir - Burada sifre kurallarına bakılmadı cünkü DTO'da zaten kontrol ediliyor
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

       logger(user,"UPDATE","Kullanici sifre kaydi guncellendi");
        
        userRepository.save(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true) // Cache temizleme
    public void updateEmail(Long userId, UserUpdateEmailRequestDto requestDto) {

       User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));


        // Aynı email başka kullanıcıya ait mi?
          if (requestDto.getNewEmail() != null &&
            !requestDto.getNewEmail().isEmpty() &&
            !user.getEmail().equals(requestDto.getNewEmail()) &&
            userRepository.existsByEmail(requestDto.getNewEmail())) {
            throw new UserAlreadyExistsException("Bu email zaten kullanılıyor.");
        }

        user.setEmail(requestDto.getNewEmail());

        logger(user, "UPDATE", "Kullanici email kaydi guncellendi");

        userRepository.save(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true) // Cache temizleme
    public void updateUsername(Long userId, UserUpdateUsernameRequestDto requestDto) {

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));

        if (requestDto.getUsername() != null &&
            !requestDto.getUsername().isEmpty() &&
            !user.getUsername().equals(requestDto.getUsername()) &&
            userRepository.existsByUsername(requestDto.getUsername())) {
            throw new UserAlreadyExistsException("Bu kullanıcı adı zaten kullanılıyor.");
        }

        user.setUsername(requestDto.getUsername());

        logger(user, "UPDATE", "Kullanici username kaydi guncellendi");

        userRepository.save(user);
    }


    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(String currentAccessToken) {
        
        // Kimliği doğrulanmış kullanıcı
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("Kullanıcı kimliği doğrulanmamış.");
        }

        User user = (User) authentication.getPrincipal();

        user.softDelete();

        // Token versiyonu artırılıyor
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);

        //  mevcut access token blacklist’e ekleniyor
        if (currentAccessToken != null && jwtUtil.validateToken(currentAccessToken)) {
            String jti = jwtUtil.getJtiFromToken(currentAccessToken);
            long ttl = jwtUtil.getRemainingTtlSeconds(currentAccessToken);
            jwtBlacklistService.blacklist(jti, ttl);
        }

        logger(user,"DELETE","Kullanici kaydi silindi");

    }

    @Transactional
    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new AuthenticationException("Geçersiz kullanıcı adı veya şifre"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationException("Geçersiz kullanıcı adı veya şifre");
        }

        logger(user,"LOGIN","Kullanici giris yapti");

        return jwtUtil.generateToken(user);
    }


    @CacheEvict(value = "users", allEntries = true) // Silme işlemlerinde cache temizleme
    public void clearCache() {
        System.out.println("Cache temizlendi.");
    }

    @Override
    public User findById(Long fromUserId) {
      
        return userRepository.findById(fromUserId).orElseThrow(
            () -> new IllegalArgumentException(fromUserId + " Id'li kullanici bulunamadi")
        );
    }

    @Override
    public User findByUsername(String username) {
        
        return userRepository.findByUsernameAndDeletedAtIsNull(username).orElseThrow(
            () -> new IllegalArgumentException(username + " kullanicisi bulunamadi")
        );
    }
}
