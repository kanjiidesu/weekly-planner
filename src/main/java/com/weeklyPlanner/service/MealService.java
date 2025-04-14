package com.weeklyPlanner.service;

import com.weeklyPlanner.model.Meal;
import com.weeklyPlanner.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (meal == null) {
            throw new IllegalArgumentException("Meal cannot be null");
        }

        return mealRepository.save(meal);
    }

    public boolean deleteMeal(Long mealId) {
        Optional<Meal> mealOpt = mealRepository.findById(mealId);
        if (mealOpt.isPresent()) {
            mealRepository.delete(mealOpt.get());
            return true;
        } else {
            return false;
        }
    }
}
