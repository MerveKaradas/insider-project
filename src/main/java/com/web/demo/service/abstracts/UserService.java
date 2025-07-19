package com.web.demo.service.abstracts;

import com.web.demo.dto.Request.UserRequestDto;
import com.web.demo.dto.Response.UserResponseDto;
import java.util.List;

public interface UserService {

UserResponseDto register(UserRequestDto requestDto);
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserRequestDto requestDto);

    void deleteUser(Long id);
    
    // boolean existsByEmail(String email);
    
    
}