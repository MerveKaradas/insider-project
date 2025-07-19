package com.web.demo.service.abstracts;

public interface UserService {
    // Define methods that UserServiceManager will implement
    void createUser(String username, String password);
    void deleteUser(String username);
    void updateUser(String username, String newPassword);
    String getUser(String username);
}