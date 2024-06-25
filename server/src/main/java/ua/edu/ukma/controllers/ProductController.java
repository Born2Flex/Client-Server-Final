package ua.edu.ukma.controllers;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductDto;
import ua.edu.ukma.dto.product.ProductExtDto;
import ua.edu.ukma.dto.product.ProductPriceDto;
import ua.edu.ukma.services.JsonMapper;
import ua.edu.ukma.services.ProductService;
import ua.edu.ukma.validator.Validator;
import ua.edu.ukma.validator.Violation;

import java.util.List;
import java.util.Map;

public class ProductController extends BaseController {
    private final ProductService productService;
    private final JsonMapper mapper;
    private final Validator validator;

    public ProductController(ProductService productService, JsonMapper mapper, Validator validator) {
        this.productService = productService;
        this.mapper = mapper;
        this.validator = validator;
    }

    public void createProduct(HttpExchange exchange) {
        System.out.println("Processing PUT request on Goods ProductController");
        ProductCreationDto productCreationDto = mapper.parseObject(getRequestBody(exchange), ProductCreationDto.class);
        List<Violation> violations = validator.validate(productCreationDto);
        if (violations.isEmpty()) {
            ProductDto createdProduct = productService.createProduct(productCreationDto);
            setResponseBody(exchange, mapper.toJson(createdProduct), 201);
        } else {
            setResponseBody(exchange, mapper.toJson(violations), 400);
        }
    }

    public void findAllProducts(HttpExchange exchange) {
        System.out.println("Processing GET request on ProductController");
        Map<String, String> requestParams = queryToMap(exchange.getRequestURI().getQuery());
        if (requestParams.containsKey("name")) {
            System.out.println("Processing GET BY NAME request on ProductController");
            List<ProductDto> products = productService.findAllProductsWithNameLike(requestParams.get("name"));
            setResponseBody(exchange, mapper.toJson(products), 200);
        } else {
            List<ProductDto> products = productService.findAllProducts();
            setResponseBody(exchange, mapper.toJson(products), 200);
        }
    }

    public void findAllProductsByCategory(HttpExchange exchange) {
        System.out.println("Processing GET request on ProductController");
        Integer categoryId = getPathVariableOrThrow(exchange, 3);
        List<ProductExtDto> products = productService.findAllProductsByCategory(categoryId);
        setResponseBody(exchange, mapper.toJson(products), 200);
    }

    public void findProductById(HttpExchange exchange) {
        System.out.println("Processing GET BY ID request on ProductController");
        Integer productId = getPathVariableOrThrow(exchange, 3);
        ProductPriceDto product = productService.findProductWithPriceById(productId);
        setResponseBody(exchange, mapper.toJson(product), 200);
    }

    public void findAllProductsWithPrice(HttpExchange exchange) {
        System.out.println("Processing GET CATEGORIES WITH PRICE request on CategoryController");
        List<ProductPriceDto> products = productService.findAllProductsWithPrice();
        setResponseBody(exchange, mapper.toJson(products), 200);
    }

    public void updateProduct(HttpExchange exchange) {
        System.out.println("Processing POST request on ProductController");
        Integer productId = getPathVariableOrThrow(exchange, 3);
        ProductDto productCreationDto = mapper.parseObject(getRequestBody(exchange), ProductDto.class);
        List<Violation> violations = validator.validate(productCreationDto);
        if (violations.isEmpty()) {
            ProductDto updatedProduct = productService.updateProduct(productId, productCreationDto);
            setResponseBody(exchange, mapper.toJson(updatedProduct), 200);
        } else {
            setResponseBody(exchange, mapper.toJson(violations), 400);
        }
    }

    public void deleteProduct(HttpExchange exchange) {
        System.out.println("Processing DELETE request on ProductController");
        productService.deleteProduct(getPathVariableOrThrow(exchange, 3));
        setResponseBody(exchange, 204);
    }
}
