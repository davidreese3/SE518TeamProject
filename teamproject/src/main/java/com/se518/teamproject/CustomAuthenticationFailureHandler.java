package com.se518.teamproject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    /*public CustomAuthenticationFailureHandler(MessageSource messageSource, LoginAttemptService loginAttemptService, HttpServletRequest request) {
        this.messageSource = messageSource;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }*/

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String ipAddress = getClientIP();
        loginAttemptService.loginFailed(ipAddress);

        String errorMessage;
        if (loginAttemptService.isBlocked(ipAddress)) {
            try {
                errorMessage = messageSource.getMessage("auth.message.blocked", null, request.getLocale());
            } catch (NoSuchMessageException e) {
                errorMessage = "Your account is locked due to too many failed login attempts.";
            }
        } else {
            errorMessage = "Invalid username or password.";
        }
        addErrorMessage(request, response, errorMessage);
    }

    private void addErrorMessage(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws IOException {
        response.sendRedirect("/login?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
    private String getEmail(HttpServletRequest request) {
        String email = request.getParameter("username");
        return email;
    }

}
