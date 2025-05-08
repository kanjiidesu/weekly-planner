package com.weeklyPlanner.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "secret";  // Use a secure key for production
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;  // 10 hours expiration

    // Generate JWT token based on the user details
    public String generateToken(Authentication authentication) {

        // Extract the authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Generate a secure key with HS512 algorithm
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);  // Automatically generates a secure key with 512 bits

        // Build the token - get a random key every time they login
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(key)  // Use the generated secure key
                .compact();

        System.out.println("Generated JWT Token: " + token); // Log the token
        return token;
    }

    // Validate the token (Check if it's expired and the user is valid)
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Extract username from the token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
