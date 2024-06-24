package ua.edu.ukma.dto.product;

public class ProductCreationDto {
    private String name;
    private String description;
    private String producer;
    private Double price;
    private Integer amount;
    private Integer categoryId;

    public ProductCreationDto() {
    }

    public ProductCreationDto(String name, String description, String producer, Double price, Integer amount, Integer categoryId) {
        this.name = name;
        this.description = description;
        this.producer = producer;
        this.price = price;
        this.amount = amount;
        this.categoryId = categoryId;
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
