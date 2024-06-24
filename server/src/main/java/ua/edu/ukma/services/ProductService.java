package ua.edu.ukma.services;

import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductDto;
import ua.edu.ukma.dto.product.ProductPriceDto;
import ua.edu.ukma.entities.Product;
import ua.edu.ukma.exceptions.ConstraintViolationException;
import ua.edu.ukma.exceptions.EntityNotFountException;
import ua.edu.ukma.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductDto createProduct(ProductCreationDto productDto) {
        if (repository.findProductByName(productDto.getName()).isPresent()) {
            throw new ConstraintViolationException("Product with such name already exists");
        }
        if (productDto.getAmount() < 0 || productDto.getPrice() <= 0) {
            throw new ConstraintViolationException("Invalid product data");
        }
        Product product = repository.createProduct(productDto);
        return new ProductDto(product);
    }

    public ProductDto updateProduct(Integer productId, ProductDto productDto) {
        if (productDto.getAmount() < 0 || productDto.getPrice() <= 0) {
            throw new ConstraintViolationException("Invalid product data");
        }
        Optional<Product> productOptional = repository.findProductByName(productDto.getName());
        if (productOptional.isPresent() && !productId.equals(productOptional.get().getId())) {
            throw new ConstraintViolationException("Product with such name already exists");
        }
        Product product = findProductOrThrow(productId);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setProducer(productDto.getProducer());
        product.setPrice(productDto.getPrice());
        product.setAmount(productDto.getAmount());
        repository.updateProduct(product);
        return new ProductDto(product);
    }

    public void deleteProduct(Integer productId) {
        findProductOrThrow(productId);
        repository.deleteProduct(productId);
    }

    public List<ProductDto> findAllProducts() {
        return repository.findAllProducts().stream().map(ProductDto::new).toList();
    }

    public List<ProductPriceDto> findAllProductsWithPrice() {
        return repository.findAllProductsWithPrice();
    }
    public ProductDto findProductById(Integer productId) {
        return new ProductDto(findProductOrThrow(productId));
    }

    private Product findProductOrThrow(Integer productId) {
        Optional<Product> productOptional = repository.findById(productId);
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        throw new EntityNotFountException("Product with such id does not exist");
    }
}
