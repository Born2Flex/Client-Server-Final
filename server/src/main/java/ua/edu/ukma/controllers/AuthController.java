package ua.edu.ukma.controllers;

import com.sun.net.httpserver.HttpExchange;
import ua.edu.ukma.dto.login.LoginRequestDto;
import ua.edu.ukma.services.AuthService;
import ua.edu.ukma.services.JsonMapper;

public class AuthController extends BaseController {
    private final AuthService authService;
    private final JsonMapper mapper;

    public AuthController(AuthService authService, JsonMapper mapper) {
        this.authService = authService;
        this.mapper = mapper;
    }

    public void authorize(HttpExchange exchange) {
        System.out.println("Processing login request on AuthController");
        String requestBody = getRequestBody(exchange);
        LoginRequestDto loginRequest = mapper.parseObject(requestBody, LoginRequestDto.class);
        String token = authService.login(loginRequest);
        setResponseBody(exchange, token, 200);
    }
}
