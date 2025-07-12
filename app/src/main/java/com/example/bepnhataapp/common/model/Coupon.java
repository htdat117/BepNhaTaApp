package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class Coupon implements Serializable {
    private long couponID;
    private Long customerID; // nullable if general
    private String couponTitle;
    private int minPrice;
    private String validDate;
    private String expireDate;
    private Integer maxDiscount; // nullable
    private int couponValue;
    private int isGeneral; // 1|0
    private Integer exchangePoints; // nullable

    public Coupon() {}

    public Coupon(long couponID, Long customerID, String couponTitle, int minPrice, String validDate, String expireDate, Integer maxDiscount, int couponValue, int isGeneral, Integer exchangePoints) {
        this.couponID = couponID;
        this.customerID = customerID;
        this.couponTitle = couponTitle;
        this.minPrice = minPrice;
        this.validDate = validDate;
        this.expireDate = expireDate;
        this.maxDiscount = maxDiscount;
        this.couponValue = couponValue;
        this.isGeneral = isGeneral;
        this.exchangePoints = exchangePoints;
    }

    // getters & setters
    public long getCouponID() { return couponID; }
    public void setCouponID(long couponID) { this.couponID = couponID; }
    public Long getCustomerID() { return customerID; }
    public void setCustomerID(Long customerID) { this.customerID = customerID; }
    public String getCouponTitle() { return couponTitle; }
    public void setCouponTitle(String couponTitle) { this.couponTitle = couponTitle; }
    public int getMinPrice() { return minPrice; }
    public void setMinPrice(int minPrice) { this.minPrice = minPrice; }
    public String getValidDate() { return validDate; }
    public void setValidDate(String validDate) { this.validDate = validDate; }
    public String getExpireDate() { return expireDate; }
    public void setExpireDate(String expireDate) { this.expireDate = expireDate; }
    public Integer getMaxDiscount() { return maxDiscount; }
    public void setMaxDiscount(Integer maxDiscount) { this.maxDiscount = maxDiscount; }
    public int getCouponValue() { return couponValue; }
    public void setCouponValue(int couponValue) { this.couponValue = couponValue; }
    public int getIsGeneral() { return isGeneral; }
    public void setIsGeneral(int isGeneral) { this.isGeneral = isGeneral; }
    public Integer getExchangePoints() { return exchangePoints; }
    public void setExchangePoints(Integer exchangePoints) { this.exchangePoints = exchangePoints; }
} 
