package com.bobatea.momandpops.backend.models;

import java.util.ArrayList;

public class Cart {
    private ArrayList<CustomizedItem> itemsInCart = new ArrayList<>();
    private static Cart instance;

    public Cart(){}

    public static Cart getInstance(){
        if(instance == null){
            instance = new Cart();
        }
        return instance;
    }

    public void addItem(CustomizedItem item){
        itemsInCart.add(item);
    }

    public ArrayList<CustomizedItem> getItems() {
        return itemsInCart;
    }
}
