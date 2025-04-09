package com.weeklyPlanner.controller;

import com.weeklyPlanner.config.JwtTokenUtil;  // Import JwtTokenUtil
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")  // Allow CORS from Angular frontend
@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;  // Inject JwtTokenUtil

    // Constructor injection for AuthenticationManager and JwtTokenUtil
    public LoginController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Print out the request details for debugging
            System.out.println("Attempting to authenticate user: " + loginRequest.getUsername());

            // Authenticate the user with the provided username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // If authentication is successful, create a UserDetails object
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    loginRequest.getUsername(),
                    loginRequest.getPassword(),
                    null
            );

            // Print out the generated user details for debugging
            System.out.println("Authenticated user: " + userDetails.getUsername());

            // Generate the JWT token using JwtTokenUtil
            String jwtToken = jwtTokenUtil.generateToken(userDetails);

            // Print out the generated JWT token for debugging
            System.out.println("Generated JWT Token: " + jwtToken);

            // Return the JWT token in the response as a JwtResponse
            return ResponseEntity.ok(new JwtResponse(jwtToken));

        } catch (BadCredentialsException e) {
            // Return an Unauthorized response if authentication fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // Helper class for request body (username & password)
    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and Setters

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // JwtResponse to hold the JWT token
    public static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
