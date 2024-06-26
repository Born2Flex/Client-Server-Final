package ua.edu.ukma.dto.product;

import ua.edu.ukma.entities.Product;
import ua.edu.ukma.validator.annotations.NotBlank;
import ua.edu.ukma.validator.annotations.NotNull;
import ua.edu.ukma.validator.annotations.Positive;

public class ProductDto {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String producer;
    @Positive
    private Double price;
    @Positive
    private Integer amount;
    @NotNull
    private Integer categoryId;

    public ProductDto() {
    }

    public ProductDto(Integer id, String name, String description, String producer, Double price, Integer amount, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.producer = producer;
        this.price = price;
        this.amount = amount;
        this.categoryId = categoryId;
    }

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.producer = product.getProducer();
        this.price = product.getPrice();
        this.amount = product.getAmount();
        this.categoryId = product.getCategoryId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}

