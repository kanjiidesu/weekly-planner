package com.weeklyPlanner.controller;

import com.weeklyPlanner.exception.ResourceNotFoundException;
import com.weeklyPlanner.model.Day;
import com.weeklyPlanner.model.User;
import com.weeklyPlanner.repository.DayRepository;
import com.weeklyPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class DayController {

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private UserRepository userRepository;

    // Fetch all days for the logged-in user
    @GetMapping("/days")
    public ResponseEntity<List<Day>> getAllDays() {
        // Extract the current logged-in user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // Assuming the username is used for login

        // Fetch the User object from the database using the username
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User not found with username: " + username));  // Handle the case where the user is not found

        // Fetch the days related to this user
        List<Day> days = dayRepository.findByUser(user);

        if (days.isEmpty()) {
            throw new ResourceNotFoundException("No days found for user with username: " + username);
        }

        return ResponseEntity.ok(days);
    }

    // Fetch days for a specific user by userId (legacy, but still useful)
    @GetMapping("/days/{userId}")
    public ResponseEntity<List<Day>> getUserDays(@PathVariable Long userId) {
        if (userId == null) {
            throw new ResourceNotFoundException("User ID from the URL is missing");
        }

        // Fetch the user by userId directly from the database
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Now check if the current userId matches
        if (!userId.equals(currentUser.getUserId())) {
            throw new ResourceNotFoundException("You are not authorized to view days for this user.");
        }

        // Fetch the days for the user
        List<Day> days = dayRepository.findByUser_UserId(userId);

        if (days.isEmpty()) {
            throw new ResourceNotFoundException("No days found for user with id: " + userId);
        }

        return ResponseEntity.ok(days);
    }
}
