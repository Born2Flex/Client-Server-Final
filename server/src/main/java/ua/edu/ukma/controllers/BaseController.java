package ua.edu.ukma.controllers;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.exceptions.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class BaseController {
    public void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        System.out.println("Request method: " + method);
        try {
            switch (method) {
                case "GET" -> processGet(exchange);
                case "POST" -> processPost(exchange);
                case "PUT" -> processPut(exchange);
                case "DELETE" -> processDelete(exchange);
                default -> methodNotAllowed(exchange);
            }
        } catch (ResponseStatusException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            setResponseBody(exchange, e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    protected void processGet(HttpExchange exchange) throws IOException {
        methodNotAllowed(exchange);
    }

    protected void processPost(HttpExchange exchange) throws IOException {
        methodNotAllowed(exchange);
    }

    protected void processPut(HttpExchange exchange) throws IOException {
        methodNotAllowed(exchange);
    }

    protected void processDelete(HttpExchange exchange) throws IOException {
        methodNotAllowed(exchange);
    }

    protected String getRequestBody(HttpExchange exchange) {
        return new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining());
    }

    protected void setResponseBody(HttpExchange exchange, String content, int statusCode) {
        byte[] responseBody = content.getBytes();
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseHeaders().add("Content-Length", String.valueOf(responseBody.length));
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(statusCode, responseBody.length);
            outputStream.write(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Integer getPathVariableOrThrow(HttpExchange exchange, int index) {
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length > index) {
            return Integer.parseInt(path[index]);
        }
        throw new RuntimeException();
    }

    protected void setResponseBody(HttpExchange exchange, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, -1);
    }

    private void methodNotAllowed(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(405, -1);
    }
}
