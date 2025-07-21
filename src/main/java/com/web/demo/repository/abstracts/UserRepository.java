package com.web.demo.repository.abstracts;

import com.web.demo.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    
boolean existsByEmail(String email);
List<User> findAll();
Optional<User> findByEmail(String email);
   
    
}
