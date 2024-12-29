package com.example.ecommerce.entity;

import java.io.Serializable;

public class Store implements Serializable {
    private int id;
    private String name;
    private String address;
    private String image;
    private User owner;

    public Store() {
    }

    public Store(int id, String name, String address, String image, User owner) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.image = image;
        this.owner = owner;
    }

    public Store(String name, String address, String image, User owner) {
        this.name = name;
        this.address = address;
        this.image = image;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
