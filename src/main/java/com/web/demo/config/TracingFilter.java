package com.web.demo.config;
import jakarta.servlet.*;
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
            if (tracer != null && tracer.currentSpan() != null) {
                var context = tracer.currentSpan().context();
                MDC.put("traceId", context.traceId());
                MDC.put("spanId", context.spanId());
            }
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}