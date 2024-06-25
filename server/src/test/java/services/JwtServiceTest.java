package services;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.services.JwtService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;
    private String validToken;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        validToken = jwtService.generateToken(1L);
    }

    @Test
    void testGenerateToken() {
        assertNotNull(validToken);
    }

    @Test
    void testGetClaims() {
        Claims claims = jwtService.getClaims(validToken);
        assertEquals("1", claims.getSubject());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void testInvalidToken() {
        assertTrue(jwtService.isExpired("123"));
    }
}