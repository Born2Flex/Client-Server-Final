package ua.edu.ukma.controllers;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductDto;
import ua.edu.ukma.services.JsonMapper;
import ua.edu.ukma.services.ProductService;

import java.io.IOException;
import java.util.List;

public class ProductController extends BaseController {
    private final ProductService productService;
    private final JsonMapper mapper;

    public ProductController(ProductService productService, JsonMapper mapper) {
        this.productService = productService;
        this.mapper = mapper;
    }

    public void createProduct(HttpExchange exchange) throws IOException {
        System.out.println("Processing PUT request on Goods ProductController");
        ProductCreationDto productCreationDto = mapper.parseObject(getRequestBody(exchange), ProductCreationDto.class);
        ProductDto createdProduct = productService.createProduct(productCreationDto);
        setResponseBody(exchange, mapper.toJson(createdProduct), 201);
    }

    public void findAllProducts(HttpExchange exchange) {
        System.out.println("Processing GET request on ProductController");
        List<ProductDto> products = productService.findAllProducts();
        setResponseBody(exchange, mapper.toJson(products), 200);
    }

    public void findProductById(HttpExchange exchange) throws IOException {
        System.out.println("Processing GET BY ID request on ProductController");
        Integer productId = getPathVariableOrThrow(exchange, 3);
        ProductDto product = productService.findProductById(productId);
        setResponseBody(exchange, mapper.toJson(product), 200);
    }

    public void updateProduct(HttpExchange exchange) {
        System.out.println("Processing POST request on ProductController");
        Integer productId = getPathVariableOrThrow(exchange, 3);
        ProductDto productCreationDto = mapper.parseObject(getRequestBody(exchange), ProductDto.class);
        ProductDto updatedProduct = productService.updateProduct(productId, productCreationDto);
        setResponseBody(exchange, mapper.toJson(updatedProduct), 200);
    }

    public void deleteProduct(HttpExchange exchange) throws IOException {
        System.out.println("Processing DELETE request on ProductController");
        productService.deleteProduct(getPathVariableOrThrow(exchange, 3));
        setResponseBody(exchange, 204);
    }
}