package com.weeklyPlanner.controller;

import com.weeklyPlanner.model.Day;
import com.weeklyPlanner.model.Meal;
import com.weeklyPlanner.repository.DayRepository;
import com.weeklyPlanner.repository.MealRepository;
import com.weeklyPlanner.service.DayService;
import com.weeklyPlanner.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/meals")
public class MealController {

    private final MealRepository mealRepository;
    private final DayService dayService;

    public MealController(MealRepository mealRepository, DayService dayService) {

        this.mealRepository = mealRepository;
        this.dayService = dayService;
    }

    @Autowired
    private MealService mealService;

    @Autowired
    private DayRepository dayRepository;

    @PostMapping
    public ResponseEntity<Meal> addMeal(@RequestBody Meal meal) {
        try {
            // Access the dayId directly from the meal object
            long dayId = meal.getDayId();  // Use getDayId() instead of getDay().getDayId()

            System.out.println("Day ID received: " + dayId);  // Debug log to check

            // Fetch the Day entity based on the dayId
            Optional<Day> dayOpt = dayRepository.findById(dayId);
            if (dayOpt.isEmpty()) {
                // If the Day with the provided dayId doesn't exist, return BAD_REQUEST
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            Day day = dayOpt.get();
            System.out.println("Fetched Day: " + day);

            // Set the fetched Day entity to the meal
            meal.setDay(day);

            // Now save the meal
            Meal savedMeal = mealService.addMeal(meal);
            return new ResponseEntity<>(savedMeal, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();  // Log the full exception for better debugging
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/day/{dayId}")
    public ResponseEntity<List<Meal>> getMealsByDay(@PathVariable Long dayId) {
        return ResponseEntity.ok(mealService.getMealsByDay(dayId));
    }
}

