package ua.edu.ukma.dto.category;

public class CategoryPriceDto {
    private Integer id;
    private String name;
    private String description;
    private Double totalPrice;

    public CategoryPriceDto() {
    }

    public CategoryPriceDto(Integer id, String name, String description, Double totalPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalPrice = totalPrice;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
