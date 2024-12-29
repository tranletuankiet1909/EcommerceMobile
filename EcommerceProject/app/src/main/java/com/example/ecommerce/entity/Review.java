package com.example.ecommerce.entity;

public class Review {
    private int id;
    private int rating;
    private String content;
    private User user;
    private PurchaseOrder purchaseOrder;
    private Review parent;

    public Review() {
    }

    public Review(int id, int rating, String content, User user, PurchaseOrder purchaseOrder) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.purchaseOrder = purchaseOrder;
        this.parent = null;
    }

    public Review(int id, int rating, String content, User user, PurchaseOrder purchaseOrder, Review parent) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.purchaseOrder = purchaseOrder;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Review getParent() {
        return parent;
    }

    public void setParent(Review parent) {
        this.parent = parent;
    }
}
