package ua.edu.ukma.dto.category;

import ua.edu.ukma.dto.product.ProductExtDto;
import ua.edu.ukma.dto.product.ProductPriceDto;

import java.util.List;

public class CategoryFullDto {
    private Integer id;
    private String name;
    private String description;
    private Double totalPrice;
    private List<ProductExtDto> products;

    public CategoryFullDto() {
    }

    public CategoryFullDto(Integer id, String name, String description, Double totalPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalPrice = totalPrice;
    }

    public CategoryFullDto(CategoryPriceDto categoryPriceDto) {
        this.id = categoryPriceDto.getId();
        this.name = categoryPriceDto.getName();
        this.description = categoryPriceDto.getDescription();
        this.totalPrice = categoryPriceDto.getTotalPrice();
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

    public List<ProductExtDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductExtDto> products) {
        this.products = products;
    }
}
