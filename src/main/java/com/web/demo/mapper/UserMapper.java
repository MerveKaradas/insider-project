package com.web.demo.mapper;

import com.web.demo.dto.Request.UserRequestDto;
import com.web.demo.dto.Response.UserResponseDto;
import com.web.demo.model.User;
import com.web.demo.model.Role;

public class UserMapper {

     // DTO → Entity 
    public static User toEntity(UserRequestDto dto, String hashedPassword) {
        return new User(
            dto.getUsername(),
            dto.getEmail(),
            hashedPassword,
            Role.ROLE_USER // default
        );
    }

    // Entity → DTO 
    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    
}
