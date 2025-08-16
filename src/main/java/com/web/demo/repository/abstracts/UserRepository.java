package com.web.demo.repository.abstracts;

import com.web.demo.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    
boolean existsByEmail(String email);
boolean existsByUsername(String username);
List<User> findAll();
Optional<User> findByEmail(String email);
Optional<User> findByUsername(String username);


Optional<User> findByIdAndDeletedAtIsNull(Long id);
Optional<User> findByEmailAndDeletedAtIsNull(String email);
Optional<User> findByUsernameAndDeletedAtIsNull(String username);
List<User> findAllByDeletedAtIsNull();

   
    
}
