package com.weeklyPlanner.repository;

import com.weeklyPlanner.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    // Correct query method using dot notation to reference userId in the User entity
    List<Day> findByUser_UserId(Long userId);
}
