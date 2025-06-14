package com.example.bepnhataapp.common.models;

public class AddressItem {
    private String name; private String phone; private String address; private boolean isDefault; private boolean selected;
    public AddressItem(String name,String phone,String address,boolean selected){this.name=name;this.phone=phone;this.address=address;this.selected=selected;this.isDefault=selected;}
    public String getName(){return name;} public String getPhone(){return phone;} public String getAddress(){return address;} public boolean isDefault(){return isDefault;} public boolean isSelected(){return selected;}
} 