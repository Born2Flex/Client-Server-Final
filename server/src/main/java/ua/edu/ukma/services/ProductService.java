package ua.edu.ukma.services;

import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductDto;
import ua.edu.ukma.dto.product.ProductExtDto;
import ua.edu.ukma.dto.product.ProductPriceDto;
import ua.edu.ukma.entities.Product;
import ua.edu.ukma.exceptions.ConstraintViolationException;
import ua.edu.ukma.exceptions.EntityNotFountException;
import ua.edu.ukma.repositories.CategoryRepository;
import ua.edu.ukma.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDto createProduct(ProductCreationDto productDto) {
        if (productRepository.findProductByName(productDto.getName()).isPresent()) {
            throw new ConstraintViolationException("Product with such name already exists");
        }
        if (categoryRepository.findCategoryById(productDto.getCategoryId()).isEmpty()) {
            throw new ConstraintViolationException("Category with such id does not exist");
        }
        if (productDto.getAmount() < 0 || productDto.getPrice() <= 0) {
            throw new ConstraintViolationException("Invalid product data");
        }
        Product product = productRepository.createProduct(productDto);
        return new ProductDto(product);
    }

    public ProductDto updateProduct(Integer productId, ProductDto productDto) {
        if (productDto.getAmount() < 0 || productDto.getPrice() <= 0) {
            throw new ConstraintViolationException("Invalid product data");
        }
        Optional<Product> productOptional = productRepository.findProductByName(productDto.getName());
        if (productOptional.isPresent() && !productId.equals(productOptional.get().getId())) {
            throw new ConstraintViolationException("Product with such name already exists");
        }
        if (categoryRepository.findCategoryById(productDto.getCategoryId()).isEmpty()) {
            throw new ConstraintViolationException("Category with such id does not exist");
        }
        Product product = findProductOrThrow(productId);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setProducer(productDto.getProducer());
        product.setPrice(productDto.getPrice());
        product.setAmount(productDto.getAmount());
        productRepository.updateProduct(product);
        return new ProductDto(product);
    }

    public void deleteProduct(Integer productId) {
        findProductOrThrow(productId);
        productRepository.deleteProduct(productId);
    }

    public List<ProductDto> findAllProducts() {
        return productRepository.findAllProducts().stream().map(ProductDto::new).toList();
    }

    public List<ProductPriceDto> findAllProductsWithPrice() {
        return productRepository.findAllProductsWithPrice();
    }

    public List<ProductExtDto> findAllProductsByCategory(Integer categoryId) {
        return productRepository.findAllProductsByCategory(categoryId);
    }

    public List<ProductDto> findAllProductsWithNameLike(String productName) {
        return productRepository.findProductsWhereNameLike(productName).stream().map(ProductDto::new).toList();
    }

    public ProductDto findProductById(Integer productId) {
        return new ProductDto(findProductOrThrow(productId));
    }

    private Product findProductOrThrow(Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        throw new EntityNotFountException("Product with such id does not exist");
    }
}
