package com.example.productmanagement.model;

import javax.persistence.*;

/**
 * This class contains details about products.
 */
@Entity
@Table(name = "product")
public class Product implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "categoryName")
    private String categoryName;

    @Column(name = "details")
    private String details;

    public Product() {
    }

    public Product(String name, double price, int categoryID, String categoryName) {
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
    }

    public Product(String name, double price, int categoryID, String categoryName, String details) {
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
        this.details = details;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategoryName() { return this.categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
