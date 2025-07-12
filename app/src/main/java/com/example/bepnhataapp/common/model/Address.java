package com.example.bepnhataapp.common.model;

public class Address {
    private long addressID;
    private long customerID;
    private String receiverName;
    private String phone;
    private String addressLine;
    private String district;
    private String province;
    private boolean isDefault;
    private String note;

    public Address() {}

    public Address(long addressID, long customerID, String receiverName, String phone, String addressLine,
                   String district, String province, boolean isDefault, String note) {
        this.addressID = addressID;
        this.customerID = customerID;
        this.receiverName = receiverName;
        this.phone = phone;
        this.addressLine = addressLine;
        this.district = district;
        this.province = province;
        this.isDefault = isDefault;
        this.note = note;
    }

    public long getAddressID() { return addressID; }
    public void setAddressID(long addressID) { this.addressID = addressID; }

    public long getCustomerID() { return customerID; }
    public void setCustomerID(long customerID) { this.customerID = customerID; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
} 
