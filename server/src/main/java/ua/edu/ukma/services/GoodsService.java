package ua.edu.ukma.services;

import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductDto;
import ua.edu.ukma.entities.Product;
import ua.edu.ukma.exceptions.ConstraintViolationException;
import ua.edu.ukma.exceptions.EntityNotFountException;
import ua.edu.ukma.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

public class GoodsService {
    private ProductRepository productRepository;

    public GoodsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDto getProductById(Integer productId) {
        return new ProductDto(getProductOrThrow(productId));
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(ProductDto::new).toList();
    }

    public ProductDto createProduct(ProductCreationDto productDto) {
        if (productRepository.findByName(productDto.getName()).isPresent()) {
            throw new ConstraintViolationException("Product with such name already exists");
        }
        if (productDto.getAmount() < 0 || productDto.getPrice() <= 0) {
            throw new ConstraintViolationException("Invalid product data");
        }
        Product product = productRepository.saveProduct(productDto);
        return new ProductDto(product);
    }

    public ProductDto updateProduct(Integer productId, ProductDto productDto) {
        if (productDto.getAmount() < 0 || productDto.getPrice() <= 0) {
            throw new ConstraintViolationException("Invalid product data");
        }
        Optional<Product> productOptional = productRepository.findByName(productDto.getName());
        if (productOptional.isPresent() && !productId.equals(productOptional.get().getId())) {
            throw new ConstraintViolationException("Product with such name already exists");
        }
        Product product = getProductOrThrow(productId);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setProducer(productDto.getProducer());
        product.setPrice(productDto.getPrice());
        product.setAmount(productDto.getAmount());
        productRepository.updateProduct(product);
        return new ProductDto(product);
    }

    public void deleteProduct(Integer productId) {
        getProductOrThrow(productId);
        productRepository.deleteProduct(productId);
    }

    private Product getProductOrThrow(Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        throw new EntityNotFountException("Product with such id does not exist");
    }
}
