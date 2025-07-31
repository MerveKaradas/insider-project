package com.web.demo.service.concretes;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.demo.dto.Request.UserRequestDto;
import com.web.demo.dto.Response.UserResponseDto;
import com.web.demo.exception.UserAlreadyExistsException;
import com.web.demo.mapper.UserMapper;
import com.web.demo.repository.abstracts.UserRepository;
import com.web.demo.security.JwtUtil;
import com.web.demo.service.abstracts.UserService;
import org.springframework.transaction.annotation.Transactional;
import com.web.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
   
    private static final Logger logger = LoggerFactory.getLogger(UserServiceManager.class);

    public UserServiceManager(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
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
        logger.info("Yeni kullanıcı kaydedildi: {}", user.getEmail());

        // // Audit loglama
        // auditPublisher.publish("User", user.getId(), "CREATE", 
        //     "Kullanıcı oluşturuldu: " + user.getEmail(),
        //     GlobalContext.getCurrentUsername(),
        //     GlobalContext.getIpAddress(),
        //     GlobalContext.getUserAgent());
        

        // Entity'den Response DTO 
        return UserMapper.toDto(user);
    }

    @Cacheable(value = "users")
    public List<UserResponseDto> getAllUsers() {
        logger.info("Tüm kullanıcılar getiriliyor.");
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));
    }


    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));

        if (!user.getEmail().equals(requestDto.getEmail()) &&
            userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserAlreadyExistsException("Bu email zaten kullanılıyor.");
        }

        if (!user.getUsername().equals(requestDto.getUsername()) &&
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
        return UserMapper.toDto(updatedUser);
    
    }


    
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));
        userRepository.delete(user);
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Geçersiz kullanıcı adı veya şifre"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationException("Geçersiz kullanıcı adı veya şifre");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
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
        
        return userRepository.findByUsername(username).orElseThrow(
            () -> new IllegalArgumentException(username + " kullanicisi bulunamadi")
        );
    }
}
