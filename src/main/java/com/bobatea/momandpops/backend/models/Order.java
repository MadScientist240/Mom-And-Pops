package com.bobatea.momandpops.backend.models;

public class Order {
    private int id;
    private int customerId;
    private Integer employeeId; // can be null
    private Integer receiptId;  // can be null
    private double totalCost;
    private String paymentMethod;
    private String orderType;
    private boolean completionStatus = false;
    private String deliveryAddress;
    private String deliveryInstructions;
    private String arrivalTime;
    private int rewardPointsEarned;
    private int rewardPointsUsed;
    private String date;

    public Order(int id, int customerId, Integer employeeId, Integer receiptId, double totalCost,
                 String paymentMethod, String orderType, boolean completionStatus, String deliveryAddress,
                 String deliveryInstructions, String arrivalTime, int rewardPointsEarned, int rewardPointsUsed, String date) {
        this.id = id;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.receiptId = receiptId;
        this.totalCost = totalCost;
        this.paymentMethod = paymentMethod;
        this.orderType = orderType;
        this.completionStatus = completionStatus;
        this.deliveryAddress = deliveryAddress;
        this.deliveryInstructions = deliveryInstructions;
        this.arrivalTime = arrivalTime;
        this.rewardPointsEarned = rewardPointsEarned;
        this.rewardPointsUsed = rewardPointsUsed;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public boolean getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(boolean completionStatus) {
        this.completionStatus = completionStatus;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getRewardPointsEarned() {
        return rewardPointsEarned;
    }

    public void setRewardPointsEarned(int rewardPointsEarned) {
        this.rewardPointsEarned = rewardPointsEarned;
    }

    public int getRewardPointsUsed() {
        return rewardPointsUsed;
    }

    public void setRewardPointsUsed(int rewardPointsUsed) {
        this.rewardPointsUsed = rewardPointsUsed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
