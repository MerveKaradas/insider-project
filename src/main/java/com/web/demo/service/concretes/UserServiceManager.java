package com.web.demo.service.concretes;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.demo.dto.Request.UserRequestDto;
import com.web.demo.dto.Response.UserResponseDto;
import com.web.demo.exception.UserAlreadyExistsException;
import com.web.demo.mapper.UserMapper;
import com.web.demo.repository.abstracts.UserRepository;
import com.web.demo.service.abstracts.UserService;
import org.springframework.transaction.annotation.Transactional;
import com.web.demo.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceManager implements  UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        // Entity'den Response DTO 
        return UserMapper.toDto(user);
    }

    public List<UserResponseDto> getAllUsers() {
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

        // Email kontrolü
        if (!user.getEmail().equals(requestDto.getEmail()) && userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserAlreadyExistsException("Bu email zaten kullanılıyor.");
        }

        // Parola güncelleme
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(requestDto.getPassword()));
        }

        // DTO'dan Entity'ye güncelleme
        UserMapper.toEntity(requestDto, user.getPasswordHash());

       
        userRepository.save(user);

        // Güncellenmiş Entity'den Response DTO 
        return UserMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı."));
        userRepository.delete(user);
    }
}
