package com.example.ecommerce.entity;

public enum OrderStatus {
    PENDING("Chờ thanh toán"),
    ONGOING("Chờ lấy hàng"),
    SHIPPING("Chờ giao hàng"),
    CANCEL("Hủy"),
    DONE("Hoàn thành");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
