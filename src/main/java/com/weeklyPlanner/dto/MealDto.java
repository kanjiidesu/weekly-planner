package com.weeklyPlanner.dto;

public class MealDto {
        private long mealId;
        private long dayId;
        private String type;
        private String description;

        public MealDto() {
        }

        public MealDto(long mealId, long dayId, String type, String description) {
            this.mealId = mealId;
            this.dayId = dayId;
            this.type = type;
            this.description = description;
        }

        public long getMealId() {
            return mealId;
        }

        public void setMealId(long mealId) {
            this.mealId = mealId;
        }

        public long getDayId() {
            return dayId;
        }

        public void setDayId(long dayId) {
            this.dayId = dayId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
}