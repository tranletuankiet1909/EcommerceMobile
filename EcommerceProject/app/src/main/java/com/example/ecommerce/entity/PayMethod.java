package com.example.ecommerce.entity;

public enum PayMethod {
    ZALOPAY("ZaloPay"),
    MOMO("Momo"),
    CODE("Code");
    private final String displayName;

    PayMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
