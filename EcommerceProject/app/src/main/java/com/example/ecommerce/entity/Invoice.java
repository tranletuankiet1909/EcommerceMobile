package com.example.ecommerce.entity;

import java.util.Date;

public class Invoice {
    private int id;
    private String payMethod;
    private Date createdDate;
    private PurchaseOrder purchaseOrder;

    public Invoice(int id, String payMethod, PurchaseOrder purchaseOrder) {
        this.id = id;
        this.payMethod = payMethod;
        this.createdDate = new Date();
        this.purchaseOrder = purchaseOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
