package com.bobatea.momandpops.backend.models;

public class UserSession {
    private static UserSession instance;
    private boolean isLoggedIn = false;
    private String username;

    public UserSession(){}

    public static UserSession getInstance() {
        if(instance == null){
            instance = new UserSession();
        }
        return instance;
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    // TODO: Handle login and logout procedures
        // Intake username and password
    public void login(){
        // Extra for DEVELOPMENT
        isLoggedIn = true;
    }

    public void logout(){
        // Extra for DEVELOPMENT
        isLoggedIn = false;
    }
}