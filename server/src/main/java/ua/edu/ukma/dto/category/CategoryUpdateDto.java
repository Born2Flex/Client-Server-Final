package ua.edu.ukma.dto.category;

public class CategoryUpdateDto {
    private String name;
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
