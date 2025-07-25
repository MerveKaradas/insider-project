package com.web.demo.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.web.demo.model.Role;
import com.web.demo.model.User;
import com.web.demo.repository.abstracts.UserRepository;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        String systemUsername = "system";
        
        // system kullanicisi aranir
        userRepository.findByUsername(systemUsername).orElseGet(() -> {

            //yoksa olusturulur
            User systemUser = new User();
            systemUser.setId(-1L);
            systemUser.setEmail("system@gmail.com"); // login olamayacak ama nullable=false old. icin ekledik
            systemUser.setUsername(systemUsername);
            systemUser.setRole(Role.ROLE_SYSTEM);
            
            String rawPassword = "sYsteM123!"; // login olmayacak ama model sınıfı hata donmemesi adina sifre belirlendi
            String encodedPassword = passwordEncoder.encode(rawPassword); 
            systemUser.setPasswordHash(encodedPassword); //veritabanına hashlenerek eklenecek

            return userRepository.save(systemUser);

        });
    }
}