package com.example.bepnhataapp.common.model;

public class Customer {
    private long customerID;
    private String fullName;
    private String gender;
    private String birthday;
    private String email;
    private String password;
    private String phone;
    private byte[] avatar;
    private String avatarLink; // URL or resource name for avatar when not stored as byte[]
    private String customerType;
    private int loyaltyPoint;
    private String createdAt;
    private String status;

    public Customer() {
    }

    public Customer(String fullName, String gender, String birthday, String email, String password,
                    String phone, byte[] avatar, String customerType, int loyaltyPoint, String createdAt,
                    String status) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.avatar = avatar;
        this.customerType = customerType;
        this.loyaltyPoint = loyaltyPoint;
        this.createdAt = createdAt;
        this.status = status;
    }

    public long getCustomerID() { return customerID; }
    public void setCustomerID(long customerID) { this.customerID = customerID; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public byte[] getAvatar() { return avatar; }
    public void setAvatar(byte[] avatar) { this.avatar = avatar; }
    public String getAvatarLink(){ return avatarLink; }
    public void setAvatarLink(String link){ this.avatarLink = link; }
    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
    public int getLoyaltyPoint() { return loyaltyPoint; }
    public void setLoyaltyPoint(int loyaltyPoint) { this.loyaltyPoint = loyaltyPoint; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 
