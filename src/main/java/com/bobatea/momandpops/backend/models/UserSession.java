package com.bobatea.momandpops.backend.models;

import com.bobatea.momandpops.frontend.SceneManager;

import java.util.Optional;

public class UserSession {
    private static UserSession instance;

    private boolean isLoggedIn = true;
    private Customer currentCustomer;

    private UserSession(){}

    public static UserSession getInstance() {
        if(instance == null){
            instance = new UserSession();
        }
        return instance;
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public boolean login(Optional<Customer> customer, String password){
        if(customer.isPresent() && customer.get().getPassword().equals(password)){
            this.currentCustomer = customer.get();
            this.isLoggedIn = true;
            return true;
        }
        return false;
    }

    public void logout(){
        this.currentCustomer = null;
        this.isLoggedIn = false;
    }
}
