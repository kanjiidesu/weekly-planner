package com.weeklyPlanner.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "days")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dayId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "weekday", nullable = false)
    private String weekday;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals;

    public Day() {
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public long getDayId() {
        return dayId;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    // Corrected getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
