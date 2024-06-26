package ua.edu.ukma.controllers;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.dto.category.*;
import ua.edu.ukma.services.CategoryService;
import ua.edu.ukma.services.JsonMapper;
import ua.edu.ukma.validator.Validator;
import ua.edu.ukma.validator.Violation;

import java.util.List;
import java.util.Map;

public class CategoryController extends BaseController {
    private final CategoryService categoryService;
    private final JsonMapper mapper;
    private final Validator validator;

    public CategoryController(CategoryService categoryService, JsonMapper mapper, Validator validator) {
        this.categoryService = categoryService;
        this.mapper = mapper;
        this.validator = validator;
    }

    public void createCategory(HttpExchange exchange) {
        System.out.println("Processing POST request on CategoryController");
        CategoryCreationDto categoryDto = mapper.parseObject(getRequestBody(exchange), CategoryCreationDto.class);
        List<Violation> violations = validator.validate(categoryDto);
        if (violations.isEmpty()) {
            CategoryDto createdCategory = categoryService.createCategory(categoryDto);
            setResponseBody(exchange, mapper.toJson(createdCategory), 201);
        } else {
            setResponseBody(exchange, mapper.toJson(violations), 400);
        }
    }

    public void findAllCategories(HttpExchange exchange) {
        System.out.println("Processing GET request on CategoryController");
        Map<String, String> requestParams = queryToMap(exchange.getRequestURI().getQuery());
        if (requestParams.containsKey("name")) {
            System.out.println("Processing GET BY NAME request on CategoryController");
            List<CategoryDto> products = categoryService.findAllCategoriesWithNameLike(requestParams.get("name"));
            setResponseBody(exchange, mapper.toJson(products), 200);
        } else {
            List<CategoryDto> allCategories = categoryService.findAllCategories();
            setResponseBody(exchange, mapper.toJson(allCategories), 200);
        }
    }

    public void findAllCategoriesWithPrice(HttpExchange exchange) {
        System.out.println("Processing GET CATEGORIES WITH PRICE request on CategoryController");
        List<CategoryPriceDto> categories = categoryService.findAllCategoriesWithPrice();
        setResponseBody(exchange, mapper.toJson(categories), 200);
    }

    public void findCategoryById(HttpExchange exchange) {
        System.out.println("Processing GET BY ID request on CategoryController");
        Integer categoryId = getPathVariableOrThrow(exchange, 3);
        CategoryFullDto category = categoryService.findCategoryWithPrice(categoryId);
        setResponseBody(exchange, mapper.toJson(category), 200);
    }

    public void updateCategory(HttpExchange exchange) {
        System.out.println("Processing PUT request on CategoryController");
        int categoryId = getPathVariableOrThrow(exchange, 3);
        CategoryUpdateDto categoryDto = mapper.parseObject(getRequestBody(exchange), CategoryUpdateDto.class);
        List<Violation> violations = validator.validate(categoryDto);
        if (violations.isEmpty()) {
            CategoryDto updatedCategory = categoryService.updateCategory(categoryId, categoryDto);
            setResponseBody(exchange, mapper.toJson(updatedCategory), 200);
        } else {
            setResponseBody(exchange, mapper.toJson(violations), 400);
        }
    }

    public void deleteCategory(HttpExchange exchange) {
        System.out.println("Processing DELETE request on CategoryController");
        int categoryId = getPathVariableOrThrow(exchange, 3);
        categoryService.deleteCategory(categoryId);
        setResponseBody(exchange, 204);
    }
}
