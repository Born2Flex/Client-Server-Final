package ua.edu.ukma.http.routing;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ua.edu.ukma.controllers.*;
import ua.edu.ukma.exceptions.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

public class RequestRouter extends BaseController implements HttpHandler {
    private final Map<Request, RequestHandler> routes = new HashMap<>();

    public RequestRouter(AuthController authController,
                         ProductController productController,
                         CategoryController categoryController) {
        routes.put(new Request("/api/login", "POST"), authController::authorize);

        routes.put(new Request("/api/products", "GET"), productController::findAllProducts);
        routes.put(new Request("/api/products/\\d+", "GET"), productController::findProductById);
        routes.put(new Request("/api/products/cost", "GET"), productController::findAllProductsWithPrice);
        routes.put(new Request("/api/categories/\\d+/products", "GET"), productController::findAllProductsByCategory);
        routes.put(new Request("/api/products", "POST"), productController::createProduct);
        routes.put(new Request("/api/products/\\d+", "PUT"), productController::updateProduct);
        routes.put(new Request("/api/products/\\d+", "DELETE"), productController::deleteProduct);

        routes.put(new Request("/api/categories", "GET"), categoryController::findAllCategories);
        routes.put(new Request("/api/categories/\\d+", "GET"), categoryController::findCategoryById);
        routes.put(new Request("/api/categories/cost", "GET"), categoryController::findAllCategoriesWithPrice);
        routes.put(new Request("/api/categories", "POST"), categoryController::createCategory);
        routes.put(new Request("/api/categories/\\d+", "PUT"), categoryController::updateCategory);
        routes.put(new Request("/api/categories/\\d+", "DELETE"), categoryController::deleteCategory);
    }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("Received request: " + exchange.getRequestURI());
        String uri = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        RequestHandler handler = routes.get(new Request(uri, method));
        try {
            if (handler == null) {
                System.out.println("Appropriate handler not found!");
                setResponseBody(exchange, 405);
            } else {
                System.out.println("Found appropriate handler!");
                handler.handleRequest(exchange);
            }
        } catch (ResponseStatusException e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
            setResponseBody(exchange, e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
