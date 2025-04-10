package com.weeklyPlanner.controller;

import com.weeklyPlanner.exception.ResourceNotFoundException;
import com.weeklyPlanner.model.Day;
import com.weeklyPlanner.repository.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class DayController {

    @Autowired
    private DayRepository dayRepository;

    @GetMapping("/days")
    public List<Day> getAllDays() { return dayRepository.findAll(); }

    @GetMapping("/days/{userId}")
    public ResponseEntity<List<Day>> getUserDays(@PathVariable Long userId) {
        // Get all the days for the user
        List<Day> days = dayRepository.findByUser_UserId(userId);

        // Check if the list of days is empty, and if so, throw an exception
        if (days.isEmpty()) {
            throw new ResourceNotFoundException("No days found for user with id: " + userId);
        }

        // Return the list of days in the response
        return ResponseEntity.ok(days);
    }
}
