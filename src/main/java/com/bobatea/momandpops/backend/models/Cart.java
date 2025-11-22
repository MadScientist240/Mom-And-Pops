package com.bobatea.momandpops.backend.models;

import impl.org.controlsfx.collections.MappingChange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private ArrayList<CustomizedItem> itemsInCart = new ArrayList<CustomizedItem>();
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
