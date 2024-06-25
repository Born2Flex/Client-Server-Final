package ua.edu.ukma.dto.category;

import ua.edu.ukma.validator.annotations.NotBlank;

public class CategoryUpdateDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    public CategoryUpdateDto() {
    }

    public CategoryUpdateDto(String name, String description) {
        this.name = name;
        this.description = description;
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
