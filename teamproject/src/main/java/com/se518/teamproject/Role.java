package com.se518.teamproject;

public class Role {

    private String email;
    private String role;

    public Role(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public Role(){}
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
