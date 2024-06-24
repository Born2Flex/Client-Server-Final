package ua.edu.ukma.http;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import ua.edu.ukma.auth.JwtAuthenticator;
import ua.edu.ukma.controllers.AuthController;
import ua.edu.ukma.controllers.CategoryController;
import ua.edu.ukma.controllers.ProductController;
import ua.edu.ukma.db.DBConnector;
import ua.edu.ukma.db.DBInitializer;
import ua.edu.ukma.http.routing.RequestRouter;
import ua.edu.ukma.properties.PropertiesLoader;
import ua.edu.ukma.repositories.CategoryRepository;
import ua.edu.ukma.repositories.ProductRepository;
import ua.edu.ukma.services.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executors;

public class Server extends Thread {
    private final int port;
    private final int numOfThreads;

    public Server(int port, int numOfThreads) {
        this.port = port;
        this.numOfThreads = numOfThreads;
    }

    @Override
    public void run() {
        try {
            Properties properties = new PropertiesLoader().loadProperties();
            DBConnector connector = new DBConnector(properties);
            setUpDatabase(connector);

            ProductRepository productRepository = new ProductRepository(connector.getConnection());
            CategoryRepository categoryRepository = new CategoryRepository(connector.getConnection());

            ProductService productService = new ProductService(productRepository);
            CategoryService categoryService = new CategoryService(categoryRepository);
            JwtService jwtService = new JwtService();
            AuthService authService = new AuthService(jwtService);

            JsonMapper mapper = new JsonMapper();

//          Creating HttpServer
            JwtAuthenticator jwtAuthenticator = new JwtAuthenticator(jwtService);
            RequestRouter router = new RequestRouter(new AuthController(authService, mapper),
                                                    new ProductController(productService, mapper),
                                                    new CategoryController(categoryService, mapper));

            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(port), 0);
            HttpContext context = server.createContext("/api", router);
            context.setAuthenticator(jwtAuthenticator);
            server.setExecutor(Executors.newFixedThreadPool(numOfThreads));
            server.start();

            System.out.println("Http server started on port " + port);
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
    }

    private void setUpDatabase(DBConnector connector) {
        new DBInitializer(connector.getConnection()).initialize();
    }
}
