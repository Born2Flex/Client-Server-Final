package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.dto.login.LoginRequestDto;
import ua.edu.ukma.exceptions.UnauthorisedException;
import ua.edu.ukma.services.AuthService;
import ua.edu.ukma.services.JwtService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    private JwtService jwtService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        authService = new AuthService(jwtService);
    }

    @Test
    void testLogin() {
        LoginRequestDto loginRequest = new LoginRequestDto("user", "password");
        String expectedToken = "token";
        when(jwtService.generateToken(1L)).thenReturn(expectedToken);
        String token = authService.login(loginRequest);
        assertNotNull(token);
        assertEquals(expectedToken, token);
        verify(jwtService, times(1)).generateToken(1L);
    }

    @Test
    void testLoginInvalidUsername() {
        LoginRequestDto loginRequest = new LoginRequestDto("invalid", "password");
        UnauthorisedException exception = assertThrows(UnauthorisedException.class,
                () -> authService.login(loginRequest));
        assertEquals("Invalid username or password", exception.getMessage());
        verify(jwtService, times(0)).generateToken(anyLong());
    }

    @Test
    void testLoginInvalidPassword() {
        LoginRequestDto loginRequest = new LoginRequestDto("user", "invalid");
        UnauthorisedException exception = assertThrows(UnauthorisedException.class,
                () -> authService.login(loginRequest));
        assertEquals("Invalid username or password", exception.getMessage());
        verify(jwtService, times(0)).generateToken(anyLong());
    }
}