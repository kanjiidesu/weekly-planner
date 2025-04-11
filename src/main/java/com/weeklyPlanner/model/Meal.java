package com.weeklyPlanner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dayId", nullable = false)
    @JsonBackReference
    private Day day;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "description", nullable = false)
    private String description;

    public Meal() {

    }


    public long getMealId() {
        return mealId;
    }

    public void setMealId(long mealId) {
        this.mealId = mealId;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public long getDayId() {
        return (day != null) ? day.getDayId() : 0;  // If day is null, return 0
    }

    public void setDayId(long dayId) {
        // Optional: If you want to set dayId directly without fetching Day object
        // you can manually set the day object based on the dayId (but you may want to keep it consistent with the database)
    }
}
