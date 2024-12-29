package com.example.ecommerce.entity;

public class PurchaseOrderDetail {
    private int id;
    private int quantity;
    private String price;
    private User buyer;
    private PurchaseOrder purchaseOrder;

    public PurchaseOrderDetail() {
    }

    public PurchaseOrderDetail(int id, int quantity, String price, User buyer, PurchaseOrder purchaseOrder) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.buyer = buyer;
        this.purchaseOrder = purchaseOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
