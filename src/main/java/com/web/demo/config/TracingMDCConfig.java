package com.web.demo.config;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Span;
import jakarta.annotation.PostConstruct;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration
public class TracingMDCConfig implements AsyncConfigurer  {

    private final Tracer tracer;

    public TracingMDCConfig(Tracer tracer) {
        this.tracer = tracer;
    }

    @PostConstruct
    public void init() {
        // Bu init sadece uygulama ayağa kalkınca kontrol eder, MDC için aşağıdaki metod önemlidir.
    }

    public static void updateMDC(Tracer tracer) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            MDC.put("traceId", currentSpan.context().traceId());
            MDC.put("spanId", currentSpan.context().spanId());
        }
    }
}
