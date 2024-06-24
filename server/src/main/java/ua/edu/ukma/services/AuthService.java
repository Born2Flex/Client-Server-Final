package ua.edu.ukma.services;

import ua.edu.ukma.dto.login.LoginRequest;
import ua.edu.ukma.exceptions.UnauthorisedException;

public class AuthService {
    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String login(LoginRequest loginRequest) {
        if ("user".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            System.out.println("User authenticated successfully");
            return jwtService.generateToken(1L);
        }
        System.out.println("Invalid username or password");
        throw new UnauthorisedException("Invalid username or password");
    }
}
