package ua.edu.ukma.http;

import com.sun.net.httpserver.*;
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
import ua.edu.ukma.validator.Validator;

import javax.net.ssl.*;
import java.net.InetSocketAddress;
import java.security.KeyStore;
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

            ProductService productService = new ProductService(productRepository, categoryRepository);
            CategoryService categoryService = new CategoryService(categoryRepository, productRepository);
            JwtService jwtService = new JwtService();
            AuthService authService = new AuthService(jwtService);

            JsonMapper mapper = new JsonMapper();
            Validator validator = new Validator();

            JwtAuthenticator jwtAuthenticator = new JwtAuthenticator(jwtService);
            RequestRouter router = new RequestRouter(new AuthController(authService, mapper),
                    new ProductController(productService, mapper, validator),
                    new CategoryController(categoryService, mapper, validator));

            HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);
            HttpContext context = server.createContext("/api", router);

            char[] password = properties.getProperty("keystorePassword").toCharArray();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(Server.class.getResourceAsStream("/keystore.jks"), password);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, password);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                @Override
                public void configure(HttpsParameters params) {
                    SSLContext context = getSSLContext();
                    SSLEngine engine = context.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    SSLParameters sslParameters = engine.getSSLParameters();
                    params.setSSLParameters(sslParameters);
                }
            });

            context.setAuthenticator(jwtAuthenticator);
            server.setExecutor(Executors.newFixedThreadPool(numOfThreads));
            server.start();
            System.out.println("Https server started on port " + port);
        } catch (Exception e) {
            throw new RuntimeException("Failed to start server", e);
        }
    }

    private void setUpDatabase(DBConnector connector) {
        new DBInitializer(connector.getConnection()).initialize();
    }
}
