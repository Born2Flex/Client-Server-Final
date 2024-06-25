package auth;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.ukma.auth.JwtAuthenticator;
import ua.edu.ukma.services.JwtService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticatorTest {

    private JwtService jwtServiceMock;
    private JwtAuthenticator jwtAuthenticator;

    @BeforeEach
    void setUp() {
        jwtServiceMock = mock(JwtService.class);
        jwtAuthenticator = new JwtAuthenticator(jwtServiceMock);
    }

    @Test
    void testAuthenticate_loginEndpoint() {
        HttpExchange exchangeMock = mock(HttpExchange.class);
        when(exchangeMock.getRequestURI()).thenReturn(URI.create("/api/login"));
        Authenticator.Result result = jwtAuthenticator.authenticate(exchangeMock);

        assertTrue(result instanceof Authenticator.Success);
        assertEquals(":user", ((Authenticator.Success) result).getPrincipal().getName());
    }

    @Test
    void testAuthenticate_missingAuthHeader() throws IOException {
        HttpExchange exchangeMock = mock(HttpExchange.class);
        when(exchangeMock.getRequestURI()).thenReturn(URI.create("/api/test"));
        Headers headersMock = mock(Headers.class);
        when(exchangeMock.getRequestHeaders()).thenReturn(headersMock);
        when(headersMock.getFirst("Authorization")).thenReturn(null);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        when(exchangeMock.getResponseHeaders()).thenReturn(headersMock);
        doNothing().when(headersMock).set(any(), any());
        doNothing().when(exchangeMock).sendResponseHeaders(anyInt(), anyInt());
        when(exchangeMock.getResponseBody()).thenReturn(os);

        Authenticator.Result result = jwtAuthenticator.authenticate(exchangeMock);

        assertTrue(result instanceof Authenticator.Failure);
        verify(exchangeMock).sendResponseHeaders(401, "Missing Authorization header".length());
        assertEquals("Missing Authorization header", os.toString());
    }

    @Test
    void testAuthenticate_expiredToken() throws IOException {
        HttpExchange exchangeMock = mock(HttpExchange.class);
        when(exchangeMock.getRequestURI()).thenReturn(URI.create("/api/test"));
        Headers headersMock = mock(Headers.class);
        when(exchangeMock.getRequestHeaders()).thenReturn(headersMock);
        when(headersMock.getFirst("Authorization")).thenReturn("Bearer expiredToken");

        when(jwtServiceMock.isExpired("expiredToken")).thenReturn(true);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        when(exchangeMock.getResponseHeaders()).thenReturn(headersMock);
        doNothing().when(headersMock).set(any(), any());
        doNothing().when(exchangeMock).sendResponseHeaders(anyInt(), anyInt());
        when(exchangeMock.getResponseBody()).thenReturn(os);

        Authenticator.Result result = jwtAuthenticator.authenticate(exchangeMock);

        assertTrue(result instanceof Authenticator.Failure);
        verify(exchangeMock).sendResponseHeaders(401, "Token is expired or invalid".length());
        assertEquals("Token is expired or invalid", os.toString());
    }

    @Test
    void testAuthenticate_validToken() {
        HttpExchange exchangeMock = mock(HttpExchange.class);
        when(exchangeMock.getRequestURI()).thenReturn(URI.create("/api/test"));
        Headers headersMock = mock(Headers.class);
        when(exchangeMock.getRequestHeaders()).thenReturn(headersMock);
        when(headersMock.getFirst("Authorization")).thenReturn("Bearer validToken");

        when(jwtServiceMock.isExpired("validToken")).thenReturn(false);

        Authenticator.Result result = jwtAuthenticator.authenticate(exchangeMock);

        assertTrue(result instanceof Authenticator.Success);
        assertEquals(":user", ((Authenticator.Success) result).getPrincipal().getName());
    }
}
