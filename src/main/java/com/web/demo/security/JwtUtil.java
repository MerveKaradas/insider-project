package com.web.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.web.demo.model.User;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

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
        
        String jti = UUID.randomUUID().toString();
        return Jwts.builder()
                .setId(jti) 
                .setSubject(user.getId().toString()) 
                .claim("role", user.getRole().name()) // rol bilgisi
                .claim("ver", user.getTokenVersion()) // bu versiyon bilgisini daha sonra token geçerliliği için kullanacağız
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
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Token'ın içindeki tüm claim'leri döner
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }


    // Token'ın versiyonu - claim tokenVersion karşılaştırması için 
    public Integer getVersionFromToken(String token) {
        Number n = parseClaims(token).get("ver", Number.class);
        return n == null ? 0 : n.intValue();
    }

    // Token'ın jti'sini döner - blacklist veya logging için kullanılacak
    public String getJtiFromToken(String token) {
        return parseClaims(token).getId();
    }

    // Token'ın kalan geçerlilik süresi(saniye) - blacklist'e TTL koymak için
    public long getRemainingTtlSeconds(String token) { 
        Date exp = parseClaims(token).getExpiration();
        long diff = exp.getTime() - System.currentTimeMillis();
        return Math.max(0L, diff / 1000L);
    }

    


    
}
