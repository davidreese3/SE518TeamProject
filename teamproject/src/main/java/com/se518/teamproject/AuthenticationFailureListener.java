package com.se518.teamproject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;
    /*public AuthenticationFailureListener(HttpServletRequest request, LoginAttemptService loginAttemptService) {
        this.request = request;
        this.loginAttemptService = loginAttemptService;
    }*/

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        final String ipAddress = getClientIP();
        final String email = getEmail();
        loginAttemptService.loginFailed(email);
    }

    private String getEmail() {
        String email = request.getParameter("username");
        return email;
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
