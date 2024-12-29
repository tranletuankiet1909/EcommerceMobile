package com.example.ecommerce.entity;

import java.util.Date;
import java.util.UUID;

public class PurchaseOrder {
    private int id;
    private String totalPrice;
    private String status;
    private User buyer;
    private Store store;

    public PurchaseOrder() {
    }

    public PurchaseOrder(int id, String totalPrice, String status, User buyer, Store store) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
        this.buyer = buyer;
        this.store = store;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
