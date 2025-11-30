package com.bobatea.momandpops.backend.models;

import java.util.ArrayList;

public class Cart {
    private ArrayList<CustomizedItem> itemsInCart = new ArrayList<>();
    private static Cart instance;
    private double totalTax = 0.00;
    private double subtotal = 0.00;
    private double deliveryFee = 0.00;
    private double total = 0.00;
    private double totalDiscount = 0.00;

    public Cart(){}

    public static Cart getInstance(){
        if(instance == null){
            instance = new Cart();
        }
        return instance;
    }

    public static void terminateInstance(){
        instance = null;
    }

    public void addItem(CustomizedItem item){
        itemsInCart.add(item);
        subtotal += item.getTotalCost();
        update();
    }

    public ArrayList<CustomizedItem> getItems() {
        return itemsInCart;
    }

    public double getSubtotal() {
        return Math.abs(subtotal);
    }

    public double getTotalTax() {
        return Math.abs(totalTax);
    }

    public double getDeliveryFee() {
        return Math.abs(deliveryFee);
    }

    public double getTotal() {
        return Math.abs(total);
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
        update();
    }

    public void addDiscount(double discountAmount){
        totalDiscount += discountAmount;
        UserSession.getInstance().getCurrentCustomer().decrementRewardPoints();
    }

    private void update(){
        totalTax = (subtotal * 0.02);
        total = subtotal + totalTax;
        total += deliveryFee;
        total -=totalDiscount;
    }

    public boolean removeItem(CustomizedItem item) {
        boolean result = itemsInCart.remove(item);
        subtotal -= item.getTotalCost();
        System.out.println(result);
        update();
        System.out.println(this.total);
        return result;
    }
}
