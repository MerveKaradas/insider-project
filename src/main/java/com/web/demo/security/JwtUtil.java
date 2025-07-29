package com.web.demo.security;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;


@Component
public class JwtUtil  {

    private final SecretKey key; // = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long EXPIRATION_TIME = 86400000; // 1 gün;

     public JwtUtil() {
        // Sabit bir Base64-encoded key (örneğin çevrimdışı ürettin)
        String secret = "MySuperSecretKeyMySuperSecretKey123!"; // min. 256 bit
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // JWT oluşturma 
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email) // jwt içerisinde email olacak
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

     // Token'dan kullanıcı adını alma
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Token'dan rolü alma
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    // Token'ın geçerliliğini kontrol etme
     public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
}
