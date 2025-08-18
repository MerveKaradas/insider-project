package com.web.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.web.demo.model.User;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import javax.crypto.SecretKey;


@Component
public class JwtUtil  {

    private final SecretKey key;
    private final long expirationTime;

    // .env ve application.yml'den al
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }


    // JWT oluşturma 
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString()) 
                .claim("role", user.getRole().name()) // rol bilgisi
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // geçerlilik süresi
                .signWith(key)
                .compact();
    }

    // Token'dan userId alma
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }


    // Token'dan rol alma
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
