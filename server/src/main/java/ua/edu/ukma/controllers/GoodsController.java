package ua.edu.ukma.controllers;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductDto;
import ua.edu.ukma.services.GoodsService;
import ua.edu.ukma.services.JsonMapper;

import java.io.IOException;

public class GoodsController extends BaseController {
    private final GoodsService goodsService;
    private final JsonMapper mapper;

    public GoodsController(GoodsService goodsService, JsonMapper mapper) {
        this.goodsService = goodsService;
        this.mapper = mapper;
    }

    @Override
    protected void processGet(HttpExchange exchange) {
        System.out.println("Processing GET request on Goods Controller");
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length > 3) {
            Integer productId = getPathVariableOrThrow(exchange, 3);
            String productJson = mapper.toJson(goodsService.getProductById(productId));
            setResponseBody(exchange, productJson, 200);
        } else {
            String productJson = mapper.toJson(goodsService.getAllProducts());
            setResponseBody(exchange, productJson, 200);
        }
    }

    @Override
    protected void processPost(HttpExchange exchange) {
        System.out.println("Processing POST request on Goods Controller");
        Integer productId = getPathVariableOrThrow(exchange, 3);
        ProductDto productCreationDto = mapper.parseObject(getRequestBody(exchange), ProductDto.class);
        String productJson = mapper.toJson(goodsService.updateProduct(productId, productCreationDto));
        setResponseBody(exchange, productJson, 200);
    }

    @Override
    protected void processPut(HttpExchange exchange) throws IOException {
        System.out.println("Processing PUT request on Goods Controller");
        ProductCreationDto productCreationDto = mapper.parseObject(getRequestBody(exchange), ProductCreationDto.class);
        String productJson = mapper.toJson(goodsService.createProduct(productCreationDto));
        setResponseBody(exchange, productJson, 201);
    }

    @Override
    protected void processDelete(HttpExchange exchange) throws IOException {
        System.out.println("Processing DELETE request on Goods Controller");
        goodsService.deleteProduct(getPathVariableOrThrow(exchange, 3));
        setResponseBody(exchange, 204);
    }
}
