package com.weeklyPlanner.service;

import com.weeklyPlanner.model.Day;
import com.weeklyPlanner.repository.DayRepository;
import org.springframework.stereotype.Service;

@Service
public class DayService {

    private final DayRepository dayRepository;

    public DayService(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
    }

    public Day getDayById(Long dayId) {
        return dayRepository.findById(dayId)
                .orElseThrow(() -> new RuntimeException("Day not found"));
    }
}

