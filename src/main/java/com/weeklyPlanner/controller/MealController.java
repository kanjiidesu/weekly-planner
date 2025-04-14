package com.weeklyPlanner.controller;

import com.weeklyPlanner.dto.MealDto;
import com.weeklyPlanner.maps.MealMapper;
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
    public ResponseEntity<MealDto> addMeal(@RequestBody MealDto mealDto) {
        try {
            long dayId = mealDto.getDayId();
            System.out.println("Day ID received: " + dayId);

            Optional<Day> dayOpt = dayRepository.findById(dayId);
            if (dayOpt.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            Day day = dayOpt.get();
            Meal meal = MealMapper.toEntity(mealDto, day); // Convert DTO to entity

            Meal savedMeal = mealService.addMeal(meal);
            MealDto responseDto = MealMapper.toDto(savedMeal); // Convert entity back to DTO

            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long mealId) {
        try {
            boolean deleted = mealService.deleteMeal(mealId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 - successfully deleted
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 - meal not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/day/{dayId}")
    public ResponseEntity<List<Meal>> getMealsByDay(@PathVariable Long dayId) {
        return ResponseEntity.ok(mealService.getMealsByDay(dayId));
    }
}

