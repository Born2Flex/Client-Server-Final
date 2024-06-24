package ua.edu.ukma.controllers;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductDto;
import ua.edu.ukma.services.ProductService;
import ua.edu.ukma.services.JsonMapper;

import java.io.IOException;

public class GoodsController extends BaseController {
    private final ProductService productService;
    private final JsonMapper mapper;

    public GoodsController(ProductService productService, JsonMapper mapper) {
        this.productService = productService;
        this.mapper = mapper;
    }

    @Override
    protected void processGet(HttpExchange exchange) {
        System.out.println("Processing GET request on Goods Controller");
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length > 3) {
            Integer productId = getPathVariableOrThrow(exchange, 3);
            String productJson = mapper.toJson(productService.findProductById(productId));
            setResponseBody(exchange, productJson, 200);
        } else {
            String productJson = mapper.toJson(productService.findAllProducts());
            setResponseBody(exchange, productJson, 200);
        }
    }

    @Override
    protected void processPost(HttpExchange exchange) {
        System.out.println("Processing POST request on Goods Controller");
        Integer productId = getPathVariableOrThrow(exchange, 3);
        ProductDto productCreationDto = mapper.parseObject(getRequestBody(exchange), ProductDto.class);
        String productJson = mapper.toJson(productService.updateProduct(productId, productCreationDto));
        setResponseBody(exchange, productJson, 200);
    }

    @Override
    protected void processPut(HttpExchange exchange) throws IOException {
        System.out.println("Processing PUT request on Goods Controller");
        ProductCreationDto productCreationDto = mapper.parseObject(getRequestBody(exchange), ProductCreationDto.class);
        String productJson = mapper.toJson(productService.createProduct(productCreationDto));
        setResponseBody(exchange, productJson, 201);
    }

    @Override
    protected void processDelete(HttpExchange exchange) throws IOException {
        System.out.println("Processing DELETE request on Goods Controller");
        productService.deleteProduct(getPathVariableOrThrow(exchange, 3));
        setResponseBody(exchange, 204);
    }
}
