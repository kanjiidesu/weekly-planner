package com.weeklyPlanner.maps;

import com.weeklyPlanner.dto.MealDto;
import com.weeklyPlanner.model.Day;
import com.weeklyPlanner.model.Meal;

public class MealMapper {

    public static MealDto toDto(Meal meal) {
        return new MealDto(
                meal.getMealId(),
                meal.getDayId(),
                meal.getType(),
                meal.getDescription()
        );
    }

    public static Meal toEntity(MealDto dto, Day day) {
        Meal meal = new Meal();
        meal.setMealId(dto.getMealId());
        meal.setDay(day);
        meal.setType(dto.getType());
        meal.setDescription(dto.getDescription());
        return meal;
    }
}

