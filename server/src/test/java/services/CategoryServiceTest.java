package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.dto.category.*;
import ua.edu.ukma.entities.Category;
import ua.edu.ukma.entities.Product;
import ua.edu.ukma.exceptions.ConstraintViolationException;
import ua.edu.ukma.exceptions.EntityNotFountException;
import ua.edu.ukma.repositories.CategoryRepository;
import ua.edu.ukma.repositories.ProductRepository;
import ua.edu.ukma.services.CategoryService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        productRepository = mock(ProductRepository.class);
        categoryService = new CategoryService(categoryRepository, productRepository);
    }

    @Test
    void testCreateCategory() {
        CategoryCreationDto categoryCreationDto = new CategoryCreationDto("New Category", "Description");
        Category category = new Category(1, "New Category", "Description");
        when(categoryRepository.findCategoryByName(categoryCreationDto.getName())).thenReturn(Optional.empty());
        when(categoryRepository.createCategory(categoryCreationDto)).thenReturn(category);
        CategoryDto result = categoryService.createCategory(categoryCreationDto);

        assertEquals("New Category", result.getName());
        verify(categoryRepository, times(1)).createCategory(categoryCreationDto);
    }

    @Test
    void testCreateCategoryCategoryAlreadyExists() {
        CategoryCreationDto categoryCreationDto = new CategoryCreationDto("Existing Category", "Description");
        when(categoryRepository.findCategoryByName(categoryCreationDto.getName())).thenReturn(Optional.of(new Category(1, "Category", "Description")));

        assertThrows(ConstraintViolationException.class, () -> categoryService.createCategory(categoryCreationDto));
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category(1, "Old Name", "Old Description");
        CategoryUpdateDto categoryUpdateDto = new CategoryUpdateDto("Updated Name", "Updated Description");
        when(categoryRepository.findCategoryById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.findCategoryByName(categoryUpdateDto.getName())).thenReturn(Optional.empty());
        CategoryDto result = categoryService.updateCategory(1, categoryUpdateDto);

        assertEquals("Updated Name", result.getName());
        verify(categoryRepository, times(1)).updateCategory(1, category);
    }

    @Test
    void testUpdateCategoryCategoryAlreadyExists() {
        Category category = new Category(1, "Old Name", "Old Description");
        CategoryUpdateDto categoryUpdateDto = new CategoryUpdateDto("Existing Category", "Updated Description");
        Category existingCategory = new Category(2, "Existing Category", "Some Description");
        when(categoryRepository.findCategoryById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.findCategoryByName(categoryUpdateDto.getName())).thenReturn(Optional.of(existingCategory));

        assertThrows(ConstraintViolationException.class, () -> categoryService.updateCategory(1, categoryUpdateDto));
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category(1, "Category Name", "Description");
        when(categoryRepository.findCategoryById(1)).thenReturn(Optional.of(category));
        categoryService.deleteCategory(1);

        verify(categoryRepository, times(1)).deleteCategory(1);
    }

    @Test
    void testDeleteCategoryNotFound() {
        when(categoryRepository.findCategoryById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFountException.class, () -> categoryService.deleteCategory(1));
    }

    @Test
    void testFindAllCategories() {
        Category category1 = new Category(1, "Category1", "Description1");
        Category category2 = new Category(2, "Category2", "Description2");
        when(categoryRepository.findAllCategories()).thenReturn(List.of(category1, category2));
        List<CategoryDto> result = categoryService.findAllCategories();

        assertEquals(2, result.size());
        assertEquals("Category1", result.get(0).getName());
        assertEquals("Category2", result.get(1).getName());
    }

    @Test
    void testFindCategoryWithPrice() {
        CategoryPriceDto categoryPriceDto = new CategoryPriceDto(1, "Category", "Description", 100.0);
        Product productDto = new Product(1, "Product Name", "Product Description", "Producer", 100.0, 1, 1);
        when(categoryRepository.findCategoryWithPriceById(1)).thenReturn(Optional.of(categoryPriceDto));
        when(productRepository.findAllProductsByCategory(1)).thenReturn(List.of(productDto));
        CategoryFullDto result = categoryService.findCategoryWithPrice(1);

        assertEquals("Category", result.getName());
        assertEquals(1, result.getProducts().size());
        assertEquals("Product Name", result.getProducts().getFirst().getName());
    }

    @Test
    void testFindCategoryWithPriceNotFound() {
        when(categoryRepository.findCategoryWithPriceById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFountException.class, () -> categoryService.findCategoryWithPrice(1));
    }
}
