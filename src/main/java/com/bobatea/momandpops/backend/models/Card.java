package com.bobatea.momandpops.backend.models;

public class Card {
    private int id;
    private int customerId;
    private String cardNumber;
    private String securityCode;
    private String expDate;

    public Card(int id, int customerId, String cardNumber, String securityCode, String expDate) {
        this.id = id;
        this.customerId = customerId;
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
        this.expDate = expDate;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
}
