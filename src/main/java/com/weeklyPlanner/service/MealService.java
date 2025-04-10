package com.weeklyPlanner.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.weeklyPlanner.repository.MealRepository;
import com.weeklyPlanner.model.Meal;

import java.util.List;

@Service
public class MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public List<Meal> getMealsByDay(Long dayId) {
        return mealRepository.findByDay_dayId(dayId);
    }

    public Meal addMeal(Meal meal) {
        return mealRepository.save(meal);
    }
}
