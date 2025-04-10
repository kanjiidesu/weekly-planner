package com.weeklyPlanner.controller;

import com.weeklyPlanner.config.JwtTokenUtil;
import com.weeklyPlanner.exception.ResourceNotFoundException;
import com.weeklyPlanner.model.LoginRequest;
import com.weeklyPlanner.model.User;
import com.weeklyPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired  // Autowire PasswordEncoder
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // get all users
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // Create user rest api
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        // Hash the password before saving
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);  // Set the hashed password

        // Save the user with the hashed password
        return userRepository.save(user);
    }

    // In your controller
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Instead of casting, directly use the principal (which should be an instance of UserDetails)
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                // Generate JWT token
                String jwtToken = jwtTokenUtil.generateToken(authentication); // Pass the Authentication object directly

                System.out.println("Attempting to authenticate user: " + loginRequest.getUsername());
                System.out.println("with password: " + loginRequest.getPassword());

                return ResponseEntity.ok(new JwtResponse(jwtToken));
            } else {
                // If the principal is not of type UserDetails, return a failure response
                return ResponseEntity.status(401).body("Authentication failed: Principal is not a valid UserDetails instance.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }


    // Helper class for response
    public static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User either could not be found or does not exist with id: " + userId));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User userInfo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User either could not be found or does not exist with id: " + userId));

        // If password is being updated, hash it
        if (userInfo.getPassword() != null && !userInfo.getPassword().isEmpty()) {
            String encodedPassword = bCryptPasswordEncoder.encode(userInfo.getPassword());
            user.setPassword(encodedPassword);  // Set the hashed password
        }

        user.setUsername(userInfo.getUsername());  // Update username

        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User either could not be found or does not exist with id: " + userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
