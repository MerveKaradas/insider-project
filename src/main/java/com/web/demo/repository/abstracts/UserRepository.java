package com.web.demo.repository.abstracts;

import com.web.demo.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    
boolean existsByEmail(String email);
List<User> findAll();
   
    
}
