package com.wanderlanka.dto;

import com.wanderlanka.user.UserRole;

public class UserStatusResponse {

    private boolean authenticated;
    private String email;
    private UserRole role;
    private boolean profileCompleted;


    public UserStatusResponse(boolean authenticated, String email, UserRole role, boolean profileCompleted){
        this.authenticated = authenticated;
        this.email = email;
        this.role = role;
        this.profileCompleted = profileCompleted;
    }


    public boolean isAuthenticated(){
        return authenticated;
    }

    public String getEmail(){
        return email;
    }

    public UserRole role(){
        return role;
    }

    public boolean isProfileCompleted(){
        return profileCompleted;
    }
}
