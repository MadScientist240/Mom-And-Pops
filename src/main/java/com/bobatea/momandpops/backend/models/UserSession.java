package com.bobatea.momandpops.backend.models;

import java.util.Optional;

public class UserSession {
    private static UserSession instance;

    private boolean isLoggedIn = false;
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

    public void login(Optional<Customer> customer){
        if(customer.isPresent()){
            this.currentCustomer = customer.get();
            this.isLoggedIn = true;
        }
    }

    public void logout(){
        this.currentCustomer = null;
        this.isLoggedIn = false;
    }
}
