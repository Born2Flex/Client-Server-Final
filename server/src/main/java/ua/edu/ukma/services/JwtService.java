package ua.edu.ukma.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtService {
    private String secret;
    private long expirationTime;

    public JwtService(String secret, long expirationTime) {
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    public JwtService() {
        this.secret = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855";
        this.expirationTime = 100;
    }

    public boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            System.out.println(("JWT token expired" + e));
            return true;
        } catch (JwtException e) {
            System.out.println(("Invalid JWT token" + e));
            return true;
        }
    }

    public String getUserId(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(Long id) {
        Date issuedDateTime = new Date(System.currentTimeMillis());
        Date expirationDateTime = new Date(System.currentTimeMillis() + expirationTime * 60 * 1000);
        return Jwts.builder()
                .subject(id.toString())
                .issuedAt(issuedDateTime)
                .expiration(expirationDateTime)
                .signWith(getSigningKey()).compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
