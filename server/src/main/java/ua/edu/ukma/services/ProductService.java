package ua.edu.ukma.services;

import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductDto;
import ua.edu.ukma.dto.product.ProductPriceDto;
import ua.edu.ukma.dto.product.ProductUpdateDto;
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
            throw new EntityNotFountException("Category with such id does not exist");
        }
        if (productDto.getAmount() < 0 || productDto.getPrice() <= 0) {
            throw new ConstraintViolationException("Invalid product data");
        }
        Product product = productRepository.createProduct(productDto);
        return new ProductDto(product);
    }

    public ProductDto updateProduct(Integer productId, ProductUpdateDto productDto) {
        Optional<Product> productOptional = productRepository.findProductByName(productDto.getName());
        if (productOptional.isPresent() && !productId.equals(productOptional.get().getId())) {
            throw new ConstraintViolationException("Product with such name already exists");
        }
        if (categoryRepository.findCategoryById(productDto.getCategoryId()).isEmpty()) {
            throw new EntityNotFountException("Category with such id does not exist");
        }
        Product product = findProductOrThrow(productId);
        if (productDto.getIncrement() < 0 && product.getAmount() + productDto.getIncrement() < 0) {
            throw new ConstraintViolationException("Can't write off too much products");
        }
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setProducer(productDto.getProducer());
        product.setPrice(productDto.getPrice());
        product.setAmount(product.getAmount() + productDto.getIncrement());
        product.setCategoryId(productDto.getCategoryId());
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

    public List<ProductDto> findAllProductsByCategory(Integer categoryId) {
        return productRepository.findAllProductsByCategory(categoryId).stream().map(ProductDto::new).toList();
    }

    public List<ProductDto> findAllProductsWithNameLike(String productName) {
        return productRepository.findProductsWhereNameLike(productName).stream().map(ProductDto::new).toList();
    }

    public ProductPriceDto findProductWithPriceById(Integer productId) {
        Optional<ProductPriceDto> product = productRepository.findProductWithPriceById(productId);
        if (product.isPresent()) {
            return product.get();
        }
        throw new EntityNotFountException("Product with such id does not exist");
    }

    private Product findProductOrThrow(Integer productId) {
        Optional<Product> productOptional = productRepository.findProductById(productId);
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        throw new EntityNotFountException("Product with such id does not exist");
    }
}
