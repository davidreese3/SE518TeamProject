package com.se518.teamproject;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitingFilter implements Filter {

    private static final int MAX_REQUESTS = 10; // Max requests allowed
    private static final long TIME_WINDOW = 60; // Time window in seconds
    private final ConcurrentHashMap<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("FILTERING REQUEST");
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession session = httpRequest.getSession();

            String sessionId = session.getId();
            LocalDateTime now = LocalDateTime.now();

            RateLimitInfo rateLimitInfo = rateLimitMap.computeIfAbsent(sessionId, k -> new RateLimitInfo(now, new AtomicInteger(0)));

            synchronized (rateLimitInfo) {
                if (now.isAfter(rateLimitInfo.getResetTime().plusSeconds(TIME_WINDOW))) {
                    // Reset rate limit
                    rateLimitInfo.setResetTime(now);
                    rateLimitInfo.getRequestCount().set(0);
                }

                if (rateLimitInfo.getRequestCount().incrementAndGet() > MAX_REQUESTS) {
                    // Invalidate session and redirect to login page
                    session.invalidate();
                    httpResponse.sendRedirect("/login?error=Too many requests. Please try again later.");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    /*@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }*/

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }

    private static class RateLimitInfo {
        private LocalDateTime resetTime;
        private final AtomicInteger requestCount;

        public RateLimitInfo(LocalDateTime resetTime, AtomicInteger requestCount) {
            this.resetTime = resetTime;
            this.requestCount = requestCount;
        }

        public LocalDateTime getResetTime() {
            return resetTime;
        }

        public void setResetTime(LocalDateTime resetTime) {
            this.resetTime = resetTime;
        }

        public AtomicInteger getRequestCount() {
            return requestCount;
        }
    }
}
