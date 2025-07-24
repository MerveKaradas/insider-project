package com.web.demo.config;

import com.web.demo.util.GlobalContext;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;    
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;    
import java.io.IOException;
import org.springframework.stereotype.Component; 
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class RequestContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String username = "anonymous";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();
        } 
    
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        GlobalContext.set(username, ip, userAgent);

        try {
            filterChain.doFilter(request, response);
        } finally {
            GlobalContext.clear(); // Her istekten sonra context temizlenir
        }
    }
}
