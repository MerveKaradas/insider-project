package com.web.demo.security;

import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.web.demo.model.User;
import com.web.demo.repository.abstracts.UserRepository;
import com.web.demo.service.concretes.CustomUserDetailsService;
import com.web.demo.service.concretes.JwtBlacklistService;
import com.web.demo.service.concretes.UserServiceManager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Objects;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final JwtBlacklistService blacklistService;

    List<String> publicPaths = List.of(
        "/api/v1/users/login",
        "/api/v1/users/register"
       
    );

    public JwtAuthFilter(JwtUtil jwtUtil, UserRepository userService, JwtBlacklistService blacklistService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userService;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

          String path = request.getRequestURI();

        // 1️⃣ Public path kontrolü
        if (publicPaths.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // 2️⃣ Token parse ve exception yönetimi
        Claims claims;
        try {
            claims = jwtUtil.parseClaims(token);
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token süresi dolmuş.");
            return;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Geçersiz token.");
            return;
        }

        String jti = claims.getId();

        // 3️⃣ Blacklist kontrolü
        if (blacklistService.isBlacklisted(jti)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token iptal edilmiş.");
            return;
        }

        Long userId = Long.parseLong(claims.getSubject());
        Integer tokenVer = claims.get("ver", Integer.class);
        if (tokenVer == null) tokenVer = 0;

        // 4️⃣ Kullanıcıyı sadece aktif olanlardan getir
        User user;
        try {
            user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı")); // Soft delete kontrolü 
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Kullanıcı bulunamadı veya silinmiş.");
            return;
        }

        // 5️⃣ Token version kontrolü
        if (!Objects.equals(tokenVer, user.getTokenVersion())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token geçersiz (versiyon uyuşmuyor).");
            return;
        }

        // 6️⃣ SecurityContext set et
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    

    }
}
