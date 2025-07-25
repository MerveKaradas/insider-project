package com.web.demo.service.abstracts;

import com.web.demo.dto.Request.UserRequestDto;
import com.web.demo.dto.Response.UserResponseDto;
import com.web.demo.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {

UserResponseDto register(UserRequestDto requestDto);
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserRequestDto requestDto);

    void deleteUser(Long id);

    User login(String email, String password);
    
    User findById(Long fromUserId);



    
   
    
    
}