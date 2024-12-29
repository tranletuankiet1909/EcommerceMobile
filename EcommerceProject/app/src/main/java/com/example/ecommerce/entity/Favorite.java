package com.example.ecommerce.entity;

public class Favorite {
    private int id;
    private User buyer;
    private Product product;

    public Favorite(int id, User buyer, Product product) {
        this.id = id;
        this.buyer = buyer;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
