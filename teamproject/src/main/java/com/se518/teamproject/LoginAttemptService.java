package com.se518.teamproject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import com.google.common.cache.LoadingCache;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int MINUTES = 15;
    private final LoadingCache<String, Integer> attemptsCache;

    @Autowired
    private AppService appService;

    public LoginAttemptService(){
        attemptsCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(15))
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void loginFailed(String key) {
        /*
        LoginAttempts loginAttempts = appService.getLoginAttemptsByEmail(email);
        if(loginAttempts == null){
            loginAttempts = new LoginAttempts(email, 0, null);
        }
        int attempts = loginAttempts.getAttempts() + 1;
        loginAttempts.setAttempts(attempts);
        if(attempts >= MAX_ATTEMPTS){
            loginAttempts.setLockTime(Timestamp.valueOf(LocalDateTime.now().plusMinutes(MINUTES)));
        }
        LoginAttempts newLoginAttempts = appService.addLoginAttempts(loginAttempts);
        System.out.println("UPDATED LOGIN ATTEMPTS " + newLoginAttempts.toString());*/
        int attempts = attemptsCache.getUnchecked(key);
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        /*
        try {
            return appService.getLoginAttemptsByEmail(email).getAttempts() >= MAX_ATTEMPTS;
        } catch (Exception e) {
            System.out.println("IS BLOCKED EXCEPTION " + e.getMessage());
            return false;
        }*/
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPTS;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
