package ua.edu.ukma.dto;

import java.io.Serializable;

public class ProductCreateRequest implements Serializable {
    private String name;
    private String description;
    private String producer;
    private int amount;
    private double price;
    private int categoryId;

    public ProductCreateRequest(String name, String description, String producer, int amount, double price, int categoryId) {
        this.name = name;
        this.description = description;
        this.producer = producer;
        this.amount = amount;
        this.price = price;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProducer() {
        return producer;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
