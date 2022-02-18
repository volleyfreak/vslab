package com.example.productmanagement.model;

public class ProductObject {

    private int id;
    private String name;
    private double price;
    private Category category;
    private String details;
    private String categoryName;

    public ProductObject (int id, String name, double price, Category category, String details, String categoryName){
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.details = details;
        this.categoryName = categoryName;
    }

    public int getId() { return this.id;}

    public void setId(int id) { this.id = id; }

    public String getName() {return this.name; }

    public void setName(String name) {this.name = name; }

    public double getPrice() {return this.price;}

    public void setPrice(double price) { this.price = price; }

    public Category getCategory() {return this.category;}

    public void setCategory(Category categorie) {this.category = categorie; }

    public String getDetails(){ return this.details;}

    public void setDetails(String details) { this.details = details;}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}