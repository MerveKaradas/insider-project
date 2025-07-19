package com.web.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                // Burada şifreyi şifreleme işlemi yapılabilir
                return rawPassword.toString(); // Örnek olarak, şifreyi düz metin olarak döndürüyoruz
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                // Şifrenin eşleşip eşleşmediğini kontrol etme işlemi
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }
    
}
