package ua.edu.ukma.controllers;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.dto.category.CategoryCreationDto;
import ua.edu.ukma.dto.category.CategoryDto;
import ua.edu.ukma.dto.category.CategoryUpdateDto;
import ua.edu.ukma.services.CategoryService;
import ua.edu.ukma.services.JsonMapper;

import java.util.List;

public class CategoryController extends BaseController {
    private final CategoryService categoryService;
    private final JsonMapper mapper;

    public CategoryController(CategoryService categoryService, JsonMapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    public void createCategory(HttpExchange exchange) {
        System.out.println("Processing POST request on CategoryController");
        CategoryCreationDto categoryDto = mapper.parseObject(getRequestBody(exchange), CategoryCreationDto.class);
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        setResponseBody(exchange, mapper.toJson(createdCategory), 201);
    }

    public void findAllCategories(HttpExchange exchange) {
        System.out.println("Processing GET request on CategoryController");
        List<CategoryDto> allCategories = categoryService.findAllCategories();
        setResponseBody(exchange, mapper.toJson(allCategories), 200);
    }

    public void updateCategory(HttpExchange exchange) {
        System.out.println("Processing PUT request on CategoryController");
        int categoryId = getPathVariableOrThrow(exchange, 3);
        CategoryUpdateDto categoryDto = mapper.parseObject(getRequestBody(exchange), CategoryUpdateDto.class);
        CategoryDto updatedCategory = categoryService.updateCategory(categoryId, categoryDto);
        setResponseBody(exchange, mapper.toJson(updatedCategory), 200);
    }

    public void deleteCategory(HttpExchange exchange) {
        System.out.println("Processing DELETE request on CategoryController");
        int categoryId = getPathVariableOrThrow(exchange, 3);
        categoryService.deleteCategory(categoryId);
        setResponseBody(exchange, 204);
    }
}