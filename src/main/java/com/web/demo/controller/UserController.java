package com.web.demo.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.web.demo.dto.Request.LoginRequestDto;
import com.web.demo.dto.Request.UserRequestDto;
import com.web.demo.dto.Request.UserUpdateEmailRequestDto;
import com.web.demo.dto.Request.UserUpdatePasswordRequestDto;
import com.web.demo.dto.Request.UserUpdateRequestDto;
import com.web.demo.dto.Request.UserUpdateUsernameRequestDto;
import com.web.demo.dto.Response.UserResponseDto;
import com.web.demo.event.AuditEventPublisher;
import com.web.demo.model.User;
import com.web.demo.service.abstracts.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

     private final UserService userService;
     private final AuditEventPublisher auditPublisher;

    // Constructor Injection
    public UserController(UserService userService, AuditEventPublisher auditPublisher) {
        this.userService = userService;
        this.auditPublisher = auditPublisher;
    }
    
 
    @GetMapping("/")
   // @PreAuthorize("hasRole('ADMIN')") // sadece admin erişimi
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
        
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserUpdateRequestDto requestDto, 
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.updateUser(userDetails, requestDto));

    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> updateUserPassword(
            @Valid @RequestBody UserUpdatePasswordRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = ((User) userDetails).getId();
        userService.updatePassword(userId, requestDto);
        return ResponseEntity.noContent().build(); // Başarılı olursa HTTP 204 (No Content)
    }

    @PatchMapping("/me/email")
    public ResponseEntity<Void> updateUserEmail(
            @Valid @RequestBody UserUpdateEmailRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = ((User) userDetails).getId();
        userService.updateEmail(userId, requestDto);
        return ResponseEntity.noContent().build(); // Başarılı olursa HTTP 204 (No Content)
    }

    @PatchMapping("/me/username")
    public ResponseEntity<Void> updateUserUsername(
            @Valid @RequestBody UserUpdateUsernameRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

        Long userId = ((User) userDetails).getId();
        userService.updateUsername(userId, requestDto);
        return ResponseEntity.noContent().build(); // Başarılı olursa HTTP 204 (No Content)
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Giriş yapan kullanıcıya token dönecek
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            String token = userService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.register(requestDto);
        
        // Audit log publishing
        auditPublisher.publish("User", responseDto.getId(), "REGISTER", "User registered successfully",
                responseDto.getEmail(), null, null);
        
        return ResponseEntity.ok(responseDto);
    }



}
