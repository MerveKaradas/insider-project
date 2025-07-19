package com.web.demo.dto.Response;

import java.time.LocalDateTime;

import com.web.demo.model.Role;

public class UserResponseDto {
    
   
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public UserResponseDto(String username, String email, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }   


    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

  
    


}
