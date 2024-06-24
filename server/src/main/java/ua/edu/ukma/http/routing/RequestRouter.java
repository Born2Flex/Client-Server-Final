package ua.edu.ukma.http.routing;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ua.edu.ukma.controllers.AuthController;
import ua.edu.ukma.controllers.BaseController;
import ua.edu.ukma.controllers.GoodsController;
import ua.edu.ukma.db.DBConnector;
import ua.edu.ukma.properties.PropertiesLoader;
import ua.edu.ukma.repositories.ProductRepository;
import ua.edu.ukma.services.AuthService;
import ua.edu.ukma.services.ProductService;
import ua.edu.ukma.services.JsonMapper;
import ua.edu.ukma.services.JwtService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestRouter implements HttpHandler {
    private static final String URI_SEPARATOR = "/";
    private final Map<String, BaseController> routes = new HashMap<>();

    public RequestRouter() {
        DBConnector connector = new DBConnector(new PropertiesLoader().loadProperties());
        routes.put("login", new AuthController(new AuthService(new JwtService()), new JsonMapper()));
        routes.put("good", new GoodsController(new ProductService(new ProductRepository(connector.getConnection())), new JsonMapper()));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Received request: " + exchange.getRequestURI());
        String[] path = exchange.getRequestURI().getPath().split(URI_SEPARATOR);
        BaseController controller = path.length > 2? routes.get(path[2]) : null;
        if (controller == null) {
            exchange.sendResponseHeaders(404, -1);
        } else {
            controller.handleRequest(exchange);
        }
    }
}
