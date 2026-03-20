package io.gianmarco.cleanArchitecture.infrastructure.config;

import io.bucket4j.Bandwidth;
import io.bucket4j.Bucket;
import io.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Map<String, Bucket> BUCKETS = new ConcurrentHashMap<>();

    private static final int REQUESTS_PER_MINUTE = 60;
    private static final int REQUESTS_PER_SECOND = 10;
    private static final int AUTH_REQUESTS_PER_MINUTE = 10;
    private static final int AUTH_REQUESTS_PER_SECOND = 3;

    private Bucket createBucket(int perMinute, int perSecond) {
        Bandwidth perMinuteLimit = Bandwidth.classic(perMinute,
            Refill.greedy(perMinute, Duration.ofMinutes(1)));
        Bandwidth perSecondLimit = Bandwidth.classic(perSecond,
            Refill.greedy(perSecond, Duration.ofSeconds(1)));
        return Bucket.builder()
            .addLimit(perMinuteLimit)
            .addLimit(perSecondLimit)
            .build();
    }

    private Bucket getBucket(String key) {
        return BUCKETS.computeIfAbsent(key, k -> createBucket(REQUESTS_PER_MINUTE, REQUESTS_PER_SECOND));
    }

    private Bucket getAuthBucket(String key) {
        return BUCKETS.computeIfAbsent(key, k -> createBucket(AUTH_REQUESTS_PER_MINUTE, AUTH_REQUESTS_PER_SECOND));
    }

    private boolean isAuthEndpoint(String path) {
        return path.startsWith("/api/v1/auth/");
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String clientIp = getClientIp(request);
        String path = request.getRequestURI();

        Bucket bucket = isAuthEndpoint(path) ? getAuthBucket(clientIp) : getBucket(clientIp);

        if (bucket.tryConsume(1)) {
            response.addHeader("X-RateLimit-Remaining",
                String.valueOf(bucket.getAvailableTokens()));
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"success\":false,\"error\":\"Too many requests. Please try again later.\"}"
            );
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
