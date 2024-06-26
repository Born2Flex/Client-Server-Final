package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductDto;
import ua.edu.ukma.dto.product.ProductUpdateDto;
import ua.edu.ukma.entities.Category;
import ua.edu.ukma.entities.Product;
import ua.edu.ukma.exceptions.ConstraintViolationException;
import ua.edu.ukma.exceptions.EntityNotFountException;
import ua.edu.ukma.repositories.CategoryRepository;
import ua.edu.ukma.repositories.ProductRepository;
import ua.edu.ukma.services.ProductService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        productService = new ProductService(productRepository, categoryRepository);
    }

    @Test
    void testCreateProduct() {
        ProductCreationDto productCreationDto = new ProductCreationDto("New Product", "Description", "Producer", 100.0, 10, 1);
        Product product = new Product(1, "New Product", "Description", "Producer", 100.0, 10, 1);
        when(productRepository.findProductByName(productCreationDto.getName())).thenReturn(Optional.empty());
        when(categoryRepository.findCategoryById(productCreationDto.getCategoryId())).thenReturn(Optional.of(new Category(1, "Category", "Description")));
        when(productRepository.createProduct(productCreationDto)).thenReturn(product);
        ProductDto result = productService.createProduct(productCreationDto);

        assertEquals("New Product", result.getName());
        verify(productRepository, times(1)).createProduct(productCreationDto);
    }

    @Test
    void testCreateProductProductAlreadyExists() {
        ProductCreationDto productCreationDto = new ProductCreationDto("Existing Product", "Description", "Producer", 100.0, 10, 1);
        when(productRepository.findProductByName(productCreationDto.getName())).thenReturn(Optional.of(new Product(1, "New Product", "Description", "Producer", 100.0, 10, 1)));

        assertThrows(ConstraintViolationException.class, () -> productService.createProduct(productCreationDto));
    }

    @Test
    void testCreateProductCategoryNotFound() {
        ProductCreationDto productCreationDto = new ProductCreationDto("New Product", "Description", "Producer", 100.0, 10, 1);
        when(categoryRepository.findCategoryById(productCreationDto.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFountException.class, () -> productService.createProduct(productCreationDto));
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product(1, "Old Name", "Old Description", "Producer", 100.0, 10, 1);
        ProductUpdateDto productUpdateDto = new ProductUpdateDto("Updated Name", "Updated Description", "Producer", 100.0, 5, 1, 1);
        when(productRepository.findProductById(1)).thenReturn(Optional.of(product));
        when(productRepository.findProductByName(productUpdateDto.getName())).thenReturn(Optional.empty());
        when(categoryRepository.findCategoryById(productUpdateDto.getCategoryId())).thenReturn(Optional.of(new Category(1, "Category", "Description")));
        ProductDto result = productService.updateProduct(1, productUpdateDto);

        assertEquals("Updated Name", result.getName());
        verify(productRepository, times(1)).updateProduct(product);
    }

    @Test
    void testUpdateProductProductAlreadyExists() {
        Product product = new Product(1, "Old Name", "Old Description", "Producer", 100.0, 10, 1);
        ProductUpdateDto productUpdateDto = new ProductUpdateDto("Existing Product", "Updated Description", "Producer", 100.0, 5, 1, 1);
        Product existingProduct = new Product(2, "Existing Product", "Some Description", "Producer", 100.0, 10, 1);

        when(productRepository.findProductById(1)).thenReturn(Optional.of(product));
        when(productRepository.findProductByName(productUpdateDto.getName())).thenReturn(Optional.of(existingProduct));

        assertThrows(ConstraintViolationException.class, () -> productService.updateProduct(1, productUpdateDto));
    }

    @Test
    void testUpdateProductCategoryNotFound() {
        Product product = new Product(1, "Old Name", "Old Description", "Producer", 100.0, 10, 1);
        ProductUpdateDto productUpdateDto = new ProductUpdateDto("Updated Name", "Updated Description", "Producer", 100.0, 5, 2, 1);
        when(productRepository.findProductById(1)).thenReturn(Optional.of(product));
        when(categoryRepository.findCategoryById(productUpdateDto.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFountException.class, () -> productService.updateProduct(1, productUpdateDto));
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product(1, "Product Name", "Description", "Producer", 100.0, 10, 1);
        when(productRepository.findProductById(1)).thenReturn(Optional.of(product));
        productService.deleteProduct(1);

        verify(productRepository, times(1)).deleteProduct(1);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findProductById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFountException.class, () -> productService.deleteProduct(1));
    }

    @Test
    void testFindAllProducts() {
        Product product1 = new Product(1, "Product1", "Description1", "Producer1", 100.0, 10, 1);
        Product product2 = new Product(2, "Product2", "Description2", "Producer2", 200.0, 20, 2);
        when(productRepository.findAllProducts()).thenReturn(List.of(product1, product2));
        List<ProductDto> result = productService.findAllProducts();

        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals("Product2", result.get(1).getName());
    }

    @Test
    void testFindProductWithPriceByIdNotFound() {
        when(productRepository.findProductWithPriceById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFountException.class, () -> productService.findProductWithPriceById(1));
    }
}
