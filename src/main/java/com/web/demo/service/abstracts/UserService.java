package com.web.demo.service.abstracts;

import com.web.demo.dto.Request.UserRequestDto;
import com.web.demo.dto.Request.UserUpdateEmailRequestDto;
import com.web.demo.dto.Request.UserUpdatePasswordRequestDto;
import com.web.demo.dto.Request.UserUpdateRequestDto;
import com.web.demo.dto.Request.UserUpdateUsernameRequestDto;
import com.web.demo.dto.Response.UserResponseDto;
import com.web.demo.model.User;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

UserResponseDto register(UserRequestDto requestDto);
    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

  //  UserResponseDto updateUser(Long id, UserRequestDto requestDto);

    void deleteUser(Long id);

    String login(String email, String password);
    
    User findById(Long fromUserId);
    User findByUsername(String username);
    UserResponseDto updateUser(UserDetails userDetails, UserUpdateRequestDto requestDto);
    void updatePassword(Long userId, UserUpdatePasswordRequestDto requestDto);
    void updateEmail(Long userId, UserUpdateEmailRequestDto requestDto);
    void updateUsername(Long userId, UserUpdateUsernameRequestDto requestDto);



    
   
    
    
}