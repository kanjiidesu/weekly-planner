import { Component, OnInit } from '@angular/core';
import { MealService } from '../../service/meal.service';  // Import MealService
import { AuthService } from '../../service/auth-service.service';  // Import AuthService for getting logged-in user
import { Meal } from '../../model/meal';  // Import Meal interface
import { Day } from '../../model/day';  // Import Day interface
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-weekplan',
  templateUrl: './weekplan.component.html',
  styleUrls: ['./weekplan.component.css'],
  standalone: true,  // Mark as standalone
  imports: [CommonModule, FormsModule]
})
export class WeekplanComponent implements OnInit {
  weekNumber: number = 0;
  days: Day[] = [];  // Array to hold the days for the logged-in user
  meal: Meal = {  // The meal object for the form
    mealId: 0,
    dayId: 0,
    type: '',
    description: ''
  };

  selectedDay: Day | null = null;
  userId: number = 0;  // User ID to fetch their days

  constructor(
    private mealService: MealService,  // MealService to fetch meal data
    private authService: AuthService  // Inject AuthService to get logged-in user
  ) {}

  ngOnInit(): void {
    this.weekNumber = this.getWeekNumber(new Date());
    this.getLoggedInUserId();  // Get logged-in user ID
  }

  // Get the logged-in user's ID
  getLoggedInUserId(): void {
    this.authService.getUser().subscribe((user) => {
      this.userId = user.userId;  // Store user ID
      this.loadDays();  // Fetch the days for the logged-in user
    });
  }

  // Select a day and prepare the meal form
  selectDay(day: Day): void {
    this.selectedDay = day;
    this.meal = {  // Initialize the meal object to ensure it's empty when selecting a new day
      mealId: 0,
      dayId: day.dayId,  // Set the dayId for the selected day
      type: '',
      description: ''
    };
  }

  // Get the week number for the current date
  getWeekNumber(date: Date): number {
    const oneJan = new Date(date.getFullYear(), 0, 1);
    const dayOfYear = Math.floor(
      (date.getTime() - oneJan.getTime()) / (24 * 60 * 60 * 1000)
    ) + 1;
    return Math.ceil(dayOfYear / 7);
  }

  // Load days for the logged-in user
  loadDays(): void {
    this.mealService.getDaysByUser(this.userId).subscribe((days) => {
      this.days = days;  // Store the days assigned to the user

      // Fetch meals for each day and populate the `meals` array
      this.days.forEach((day) => {
        this.mealService.getMealsByDay(day.dayId).subscribe((meals) => {
          day.meals = meals;  // Update meals for each day
        });
      });
    });
  }

  // Get the meal description for a specific type (e.g., BREAKFAST)
  getMeal(day: Day, type: string): string {
    const meal = day.meals.find((m: Meal) => m.type === type);
    return meal ? meal.description : '-';  // Return the description or "-" if no meal found
  }

  // Add a new meal for the selected day
  addMeal(): void {
    if (this.selectedDay) {
      this.meal.dayId = this.selectedDay.dayId;  // Assign the selected day ID to the meal
      this.mealService.addMeal(this.meal).subscribe(
        (response) => {
          console.log('Meal added:', response);
          this.selectedDay!.meals.push(response);  // Update the meals list for the selected day
          this.resetMealForm();  // Optionally reset the form after submission
        },
        (error) => {
          console.error('Error adding meal:', error);
        }
      );
    }
  }

  // Reset the meal form
  resetMealForm() {
    this.meal = { mealId: 0, dayId: 0, type: '', description: '' };  // Reset meal form
  }

  // Cancel meal form and reset
  cancelMealForm(): void {
    this.resetMealForm();  // Reset the meal form on cancel
  }
}
