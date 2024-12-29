package com.example.ecommerce.entity;

public class Cart {
    private int id;
    private int quantity;
    private Boolean selected;
    private User buyer;
    private Product product;

    public Cart() {
    }

    public Cart(int id, int quantity, Boolean selected, User buyer, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.selected = selected;
        this.buyer = buyer;
        this.product = product;
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

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
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
