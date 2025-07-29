package com.web.demo.service.concretes;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.demo.repository.abstracts.UserRepository;

@Service
public class CustomUserDetailsService  implements UserDetailsService{

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println(email + "geldi loduserbyusername calisti");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kayıtlı kullanıcı bulunamadı"));
    }
    
}
