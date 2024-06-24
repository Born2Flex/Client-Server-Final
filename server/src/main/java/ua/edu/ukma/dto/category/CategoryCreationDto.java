package ua.edu.ukma.dto.category;

public class CategoryCreationDto {
    private String name;
    private String description;

    public CategoryCreationDto() {
    }

    public CategoryCreationDto(String name, String description) {
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
