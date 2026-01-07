package com.wanderlanka.dto;

public class LoginResponse {

    private String token;
    private boolean emailVerified;
    private boolean roleSelected;
    private boolean profileCompleted;
    private String role;

    public LoginResponse(String token,
                        boolean emailVerified,
                        boolean roleSelected,
                        boolean profileCompleted,
                        String role){
        this.token = token;
        this.emailVerified = emailVerified;
        this.roleSelected = roleSelected;
        this.profileCompleted = profileCompleted;
        this.role = role;
    }

    public String getToken(){
        return token;
    }

    public boolean isEmailVerified(){
        return emailVerified;
    }

    public boolean isRoleSelected(){
        return roleSelected;
    }

    public boolean isProfileCompleted(){
        return profileCompleted;
    }

    public String getRole(){
        return role;
    }
}
