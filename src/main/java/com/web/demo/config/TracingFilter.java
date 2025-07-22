package com.web.demo.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import org.springframework.stereotype.Component;
import io.micrometer.tracing.Tracer;
import org.slf4j.MDC;
import java.io.IOException;

@Component
@WebFilter("/*")
public class TracingFilter implements Filter {

    private final Tracer tracer;

    public TracingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            TracingMDCConfig.updateMDC(tracer);
            chain.doFilter(request, response);
        } finally {
            // Temizle
            MDC.clear();
        }
    }
}
