package com.example.ecommerce.entity;

import android.net.wifi.aware.PublishConfig;

public class PurchaseOrderDetail {
    private int id;
    private int quantity;
    private String price;
    private Product product;
    private PurchaseOrder purchaseOrder;

    public PurchaseOrderDetail() {
    }

    public PurchaseOrderDetail(int id, int quantity, String price, Product product, PurchaseOrder purchaseOrder) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
        this.purchaseOrder = purchaseOrder;
    }
    public PurchaseOrderDetail(int quantity, String price, Product product, PurchaseOrder purchaseOrder) {
        this.quantity = quantity;
        this.price = price;
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
