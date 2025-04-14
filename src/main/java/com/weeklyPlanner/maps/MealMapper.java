package com.weeklyPlanner.maps;

import com.weeklyPlanner.dto.MealDto;
import com.weeklyPlanner.model.Day;
import com.weeklyPlanner.model.Meal;

public class MealMapper {

    public static MealDto toDto(Meal meal) {
        return new MealDto(
                meal.getMealId(),
                meal.getDayId(),  // We use your custom getDayId() here
                meal.getType(),
                meal.getDescription()
        );
    }

    public static Meal toEntity(MealDto dto, Day day) {
        Meal meal = new Meal();
        meal.setMealId(dto.getMealId());
        meal.setDay(day); // Day should be fetched from DB or passed in
        meal.setType(dto.getType());
        meal.setDescription(dto.getDescription());
        return meal;
    }
}

