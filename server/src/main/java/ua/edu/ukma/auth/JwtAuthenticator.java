package ua.edu.ukma.auth;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import ua.edu.ukma.services.JwtService;

import java.io.IOException;
import java.io.OutputStream;

public class JwtAuthenticator extends Authenticator {
    private static final String BEARER_HEADER = "Bearer ";
    private final JwtService jwtService;

    public JwtAuthenticator(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Result authenticate(HttpExchange exchange) {
        System.out.println("Authenticating request: " + exchange.getRequestURI());
        if (exchange.getRequestURI().getPath().equals("/api/login")) {
            System.out.println("Request authenticated: Permit all URI");
            return new Success(new HttpPrincipal("user", ""));
        }

        Headers headers = exchange.getRequestHeaders();
        String authHeader = headers.getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith(BEARER_HEADER)) {
            System.out.println("Request unauthorized: Missing Bearer header");
            return unauthorizedResponse(exchange, "Missing Authorization header");
        }

        String token = authHeader.substring(BEARER_HEADER.length());
        if (jwtService.isExpired(token)) {
            System.out.println("Request unauthorized: Token expired");
            return unauthorizedResponse(exchange, "Token is expired or invalid");
        }
        System.out.println("Request authenticated: Valid JWT token");
        return new Success(new HttpPrincipal("user", ""));
    }

    private Result unauthorizedResponse(HttpExchange exchange, String message) {
        try {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(401, message.length());
            OutputStream os = exchange.getResponseBody();
            os.write(message.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Failure(401);
    }
}
