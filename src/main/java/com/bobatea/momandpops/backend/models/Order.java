package com.bobatea.momandpops.backend.models;

public class Order {
    private int id;
    private int customerId;
    private Integer employeeId;
    private Integer receiptId;
    private double totalCost;
    private String paymentMethod;
    private String orderType;
    private boolean completionStatus;
    private String deliveryAddress;
    private String deliveryInstructions;
    private String arrivalTime;
    private int rewardPointsEarned;
    private int rewardPointsUsed;

    public Order(int id, int customerId, Integer employeeId, Integer receiptId,
                 double totalCost, String paymentMethod, String orderType,
                 boolean completionStatus, String deliveryAddress,
                 String deliveryInstructions, String arrivalTime,
                 int rewardPointsEarned, int rewardPointsUsed) {

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
    }
}

