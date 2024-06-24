package ua.edu.ukma.http.routing;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

@FunctionalInterface
public interface RequestHandler {
    void handleRequest(HttpExchange exchange) throws IOException;
}
