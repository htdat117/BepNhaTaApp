package com.example.bepnhataapp.common.model;

public enum OrderStatus {
    WAIT_CONFIRM("Chờ xác nhận"),
    WAIT_PICKUP("Chờ lấy hàng"),
    OUT_FOR_DELIVERY("Đang vận chuyển"),
    DELIVERED("Đã giao"),
    RETURNED("Trả hàng"),
    CANCELED("Đã huỷ");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static OrderStatus fromString(String status) {
        for (OrderStatus s : OrderStatus.values()) {
            if (s.name().equalsIgnoreCase(status) || s.displayName.equalsIgnoreCase(status)) {
                return s;
            }
        }
        return null;
    }
} 
