package com.se518.teamproject;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private AppService appService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    /*public MyUserDetailsService(AppService appService, LoginAttemptService loginAttemptService, HttpServletRequest request) {
        this.appService = appService;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }*/

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /*String ipAddress = getClientIP();
        if(loginAttemptService.isBlocked(email)){
            throw new LockedException("Blocked due to too many failed login attempts");
        }

        WebUser webUser = appService.getUserByEmail(email);
        if(webUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(webUser.getEmail(), webUser.getPassword(), getAuthorities(email));*/

        String ipAddress = getClientIP();
        if (loginAttemptService.isBlocked(ipAddress)) {
            throw new RuntimeException("Blocked due to too many failed login attempts");
        }

        WebUser webUser = appService.getUserByEmail(email);
        if(webUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                webUser.getEmail(),
                webUser.getPassword(),
                getAuthorities(webUser.getEmail())
        );
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String email) {
        List<Role> roles = appService.getRolesByEmail(email);
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toSet());
    }
}
