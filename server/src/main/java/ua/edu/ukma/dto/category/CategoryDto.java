package ua.edu.ukma.dto.category;

import ua.edu.ukma.entities.Category;

public class CategoryDto {
    private Integer id;
    private String name;
    private String description;

    public CategoryDto() {
    }

    public CategoryDto(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
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
}
