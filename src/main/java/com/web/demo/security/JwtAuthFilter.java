package com.web.demo.security;

import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.web.demo.service.concretes.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    List<String> publicPaths = List.of(
        "/api/v1/users/login",
        "/api/v1/users/register"
       
    );

    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String path = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");
        Long userId = null;
        String token = null;

        // Public path'ler için filtreyi atla
        if (publicPaths.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization header kontrol
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
             try {
                userId = jwtUtil.getUserIdFromToken(token);
                System.out.println("Açılmış userId from JWT: " + userId);
            } catch (Exception e) {
                System.out.println("Token'dan userId alma hatası: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token geçersiz veya süresi dolmuş.");
                return;
            }
        }

        // Email varsa ve SecurityContext boşsa, kimlik doğrulama yapılır
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               try {
                UserDetails userDetails = userDetailsService.loadUserById(userId);

                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // Kullanıcı bulunamadı veya silinmiş
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Kullanıcı bulunamadı veya silinmiş.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    

    }
}
