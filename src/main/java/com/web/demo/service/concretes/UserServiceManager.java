package com.web.demo.service.concretes;

import org.springframework.stereotype.Service;

import com.web.demo.service.abstracts.UserService;

@Service
public class UserServiceManager implements  UserService {

    @Override
    public void createUser(String username, String password) {
        // Implementation for creating a user
        System.out.println("User created: " + username);
    }

    @Override
    public void deleteUser(String username) {
        // Implementation for deleting a user
        System.out.println("User deleted: " + username);
    }

    @Override
    public void updateUser(String username, String newPassword) {
        // Implementation for updating a user's password
        System.out.println("User updated: " + username + " with new password.");
    }

    @Override
    public String getUser(String username) {
        // Implementation for retrieving a user
        return "Retrieved user: " + username;
    }
    
}
