package com.example.ecommerce.entity;

public enum Role {
    ADMIN("Quản trị viên"),
    BUYER("Người mua"),
    SELLER("Người bán");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
