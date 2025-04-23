package com.weeklyPlanner.repository;

import com.weeklyPlanner.model.Day;
import com.weeklyPlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    // auto-generates the implementation at runtime, so you do not have to create a class
    List<Day> findByUser_UserId(Long userId);

    Optional<Day> findById(Long dayId);

    List<Day> findAllByUser(User user);

    List<Day> findByUser(User user);
}
