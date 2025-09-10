package es.agonzalez.assistant.recipe.api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rate limiting interceptor to prevent abuse and improve performance
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<String, RequestCounter> clientRequests = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private static final long TIME_WINDOW_MS = 60000; // 1 minute

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String clientIp = getClientIpAddress(request);
        
        RequestCounter counter = clientRequests.computeIfAbsent(clientIp, k -> new RequestCounter());
        
        long currentTime = System.currentTimeMillis();
        
        // Reset counter if time window has passed
        if (currentTime - counter.getFirstRequestTime() > TIME_WINDOW_MS) {
            counter.reset(currentTime);
        }
        
        // Check if limit exceeded
        if (counter.getRequestCount() >= MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(429); // Too Many Requests
            response.setHeader("Retry-After", "60");
            response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
            response.setContentType("application/json");
            return false;
        }
        
        counter.increment();
        return true;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || "unknown".equalsIgnoreCase(xfHeader)) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }

    /**
     * Counter class to track requests per client
     */
    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private volatile long firstRequestTime;

        public RequestCounter() {
            this.firstRequestTime = System.currentTimeMillis();
        }

        public void increment() {
            count.incrementAndGet();
        }

        public int getRequestCount() {
            return count.get();
        }

        public long getFirstRequestTime() {
            return firstRequestTime;
        }

        public void reset(long newTime) {
            count.set(0);
            firstRequestTime = newTime;
        }
    }
}
