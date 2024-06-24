package ua.edu.ukma.http;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import ua.edu.ukma.auth.JwtAuthenticator;
import ua.edu.ukma.http.routing.RequestRouter;
import ua.edu.ukma.services.JwtService;

import java.io.IOException;
import java.net.InetSocketAddress;
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
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(port), 0);
            HttpContext context = server.createContext("/api", new RequestRouter());
            context.setAuthenticator(new JwtAuthenticator(new JwtService()));
            server.setExecutor(Executors.newFixedThreadPool(numOfThreads));
            server.start();
            System.out.println("Http server started on port " + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
