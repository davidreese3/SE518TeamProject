package com.se518.teamproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;


public class AccountLockFilter extends OncePerRequestFilter {

    private final LoginAttemptService loginAttemptService;

    public AccountLockFilter(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the authenticated user's email
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();

            // Check if the user is locked
            if (loginAttemptService.isBlocked(email)) {
                response.sendRedirect("/403");
                return;
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
