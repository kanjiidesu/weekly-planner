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
        List<Day> days = dayRepository.findByUser_UserId(userId);

        if (days.isEmpty()) {
            throw new ResourceNotFoundException("No days found for user with id: " + userId);
        }

        return ResponseEntity.ok(days);
    }

    @GetMapping("/users/{userId}/days")
    public List<Day> getDaysByUserId(@PathVariable Long userId) {
        return dayRepository.findByUser_UserId(userId);
    }

}
