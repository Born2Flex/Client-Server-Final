package ua.edu.ukma.services;

import ua.edu.ukma.dto.category.*;
import ua.edu.ukma.entities.Category;

import ua.edu.ukma.exceptions.ConstraintViolationException;
import ua.edu.ukma.exceptions.EntityNotFountException;
import ua.edu.ukma.repositories.CategoryRepository;
import ua.edu.ukma.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository repository, ProductRepository productRepository) {
        this.categoryRepository = repository;
        this.productRepository = productRepository;
    }

    public CategoryDto createCategory(CategoryCreationDto categoryDto) {
        if (categoryRepository.findCategoryByName(categoryDto.getName()).isPresent()) {
            throw new ConstraintViolationException("Category with such name already exists");
        }
        Category category = categoryRepository.createCategory(categoryDto);
        return new CategoryDto(category);
    }

    public CategoryDto updateCategory(Integer categoryId, CategoryUpdateDto categoryDto) {
        Category category = findCategoryOrThrow(categoryId);
        Optional<Category> categoryOptional = categoryRepository.findCategoryByName(categoryDto.getName());
        if (categoryOptional.isPresent() && !categoryId.equals(categoryOptional.get().getId())) {
            throw new ConstraintViolationException("Category with such name already exists");
        }
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        categoryRepository.updateCategory(categoryId, category);
        return new CategoryDto(category);
    }

    public void deleteCategory(Integer categoryId) {
        findCategoryOrThrow(categoryId);
        categoryRepository.deleteCategory(categoryId);
    }

    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAllCategories().stream().map(CategoryDto::new).toList();
    }

    public List<CategoryPriceDto> findAllCategoriesWithPrice() {
        return categoryRepository.findAllCategoriesPrice();
    }

    public List<CategoryDto> findAllCategoriesWithNameLike(String name) {
        return categoryRepository.findCategoriesWhereNameLike(name).stream().map(CategoryDto::new).toList();
    }

    public CategoryFullDto findCategoryWithPrice(Integer categoryId) {
        Optional<CategoryPriceDto> category = categoryRepository.findCategoryWithPriceById(categoryId);
        if (category.isPresent()) {
            CategoryFullDto categoryFullDto = new CategoryFullDto(category.get());
            categoryFullDto.setProducts(productRepository.findAllProductsByCategory(categoryId));
            return categoryFullDto;
        }
        throw new EntityNotFountException("Category with such id does not exist");
    }

    private Category findCategoryOrThrow(Integer productId) {
        Optional<Category> categoryOptional = categoryRepository.findCategoryById(productId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        throw new EntityNotFountException("Category with such id does not exist");
    }
}
