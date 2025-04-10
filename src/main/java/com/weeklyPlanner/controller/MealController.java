package com.weeklyPlanner.controller;

import com.weeklyPlanner.model.Meal;
import com.weeklyPlanner.repository.MealRepository;
import com.weeklyPlanner.service.DayService;
import com.weeklyPlanner.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<Meal> addMeal(@RequestBody Meal meal) {
        Meal savedMeal = mealService.addMeal(meal);
        return new ResponseEntity<>(savedMeal, HttpStatus.CREATED);
    }

    @GetMapping("/day/{dayId}")
    public ResponseEntity<List<Meal>> getMealsByDay(@PathVariable Long dayId) {
        return ResponseEntity.ok(mealService.getMealsByDay(dayId));
    }
}

