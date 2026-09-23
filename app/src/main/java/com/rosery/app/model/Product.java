package com.rosery.app.model;

public class Product {
    public int id;
    public String name;
    public String category;
    public String price;
    public String icon;
    public String origin;
    public String description;

    public Product(int id, String name, String category, String price, String icon, String origin, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.icon = icon;
        this.origin = origin;
        this.description = description;
    }
}
