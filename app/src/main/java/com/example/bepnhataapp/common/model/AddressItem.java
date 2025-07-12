package com.example.bepnhataapp.common.model;

import java.io.Serializable;

public class AddressItem implements Serializable {
    private long id;
    private String name;
    private String phone;
    private String address;
    private boolean isDefault;
    private boolean selected;

    public AddressItem(long id, String name, String phone, String address, boolean isDefault){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
        this.selected = isDefault;
    }

    public long getId(){return id;}
    public String getName(){return name;}
    public String getPhone(){return phone;}
    public String getAddress(){return address;}
    public boolean isDefault(){return isDefault;}
    public boolean isSelected(){return selected;}
    public void setSelected(boolean s){this.selected=s;}
} 
