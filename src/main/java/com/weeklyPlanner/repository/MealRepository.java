package com.weeklyPlanner.repository;

import com.weeklyPlanner.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    // This will query the Meal entities where the associated Day's dayId matches the provided dayId
    List<Meal> findByDay_dayId(Long dayId);

    void delete(Meal meal);
    Optional<Meal> findById(Long id);
}
