package com.se518.teamproject;

import java.sql.Timestamp;

public class LoginAttempts {

    private String email;
    private int attempts;
    private Timestamp lockTime;

    public LoginAttempts(String email, int attempts, Timestamp lockTime) {
        this.email = email;
        this.attempts = attempts;
        this.lockTime = lockTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts){
        this.attempts = attempts;
    }

    public Timestamp getLockTime() {
        return lockTime;
    }

    public void setLockTime(Timestamp lockTime) {
        this.lockTime = lockTime;
    }

    @Override
    public String toString(){
        if(lockTime == null){
            return getAttempts() + " Login Attempts for " + getEmail();
        } else {
            return getAttempts() + " Login Attempts for " + getEmail() + " Time when unblocked: " + getLockTime().toString();
        }
    }
}
