/*package com.se518.teamproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDAOImpl userDAO;

    @Autowired
    private BruteForceProtectionService bruteForceProtectionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserDAOImpl userDAO, BruteForceProtectionService bruteForceProtectionService) {
        this.userDAO = userDAO;
        this.bruteForceProtectionService = bruteForceProtectionService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // Check if the user is blocked
        if (bruteForceProtectionService.isBlocked(username)) {
            throw new BadCredentialsException("Too many failed attempts. You have been locked out for too many attempts.");
        }

        WebUser user = (WebUser) userDAO.getUserByEmail(username);
        if(user == null || !passwordEncoder.matches(password, user.getPassword())){
            bruteForceProtectionService.loginFailed(username);
            throw new BadCredentialsException("Invalid username or password.");
        }

        bruteForceProtectionService.loginSucceeded(username);
        return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}*/
