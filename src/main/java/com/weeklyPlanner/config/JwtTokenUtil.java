package com.weeklyPlanner.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "secret";  // Use a secure key for production
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;  // 10 hours expiration

    // Generate JWT token based on the user details
    public String generateToken(UserDetails userDetails) {
        System.out.println("Generating token for user: " + userDetails.getUsername());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // Set the username as the subject of the token
                .setIssuedAt(new Date())  // Set the issue date as the current date
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Set expiration date
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)  // Sign the token with the secret key
                .compact();
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
