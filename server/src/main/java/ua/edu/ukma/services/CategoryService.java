package ua.edu.ukma.services;

import ua.edu.ukma.dto.category.CategoryCreationDto;
import ua.edu.ukma.dto.category.CategoryDto;
import ua.edu.ukma.dto.category.CategoryPriceDto;
import ua.edu.ukma.dto.category.CategoryUpdateDto;
import ua.edu.ukma.entities.Category;

import ua.edu.ukma.exceptions.ConstraintViolationException;
import ua.edu.ukma.exceptions.EntityNotFountException;
import ua.edu.ukma.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;

public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryDto createCategory(CategoryCreationDto categoryDto) {
        if (repository.findCategoryByName(categoryDto.getName()).isPresent()) {
            throw new ConstraintViolationException("Category with such name already exists");
        }
        Category category = repository.createCategory(categoryDto);
        return new CategoryDto(category);
    }

    public CategoryDto updateCategory(Integer categoryId, CategoryUpdateDto categoryDto) {
        Category category = findCategoryOrThrow(categoryId);
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        repository.updateCategory(categoryId, category);
        return new CategoryDto(category);
    }

    public void deleteCategory(Integer categoryId) {
        findCategoryOrThrow(categoryId);
        repository.deleteCategory(categoryId);
    }

    public List<CategoryDto> findAllCategories() {
        return repository.findAllCategories().stream().map(CategoryDto::new).toList();
    }

    public List<CategoryPriceDto> findAllCategoriesPrice() {
        return repository.findAllCategoriesPrice();
    }

    private Category findCategoryOrThrow(Integer productId) {
        Optional<Category> categoryOptional = repository.findCategoryById(productId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        throw new EntityNotFountException("Category with such id does not exist");
    }
}
