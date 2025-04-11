import { Component, OnInit } from '@angular/core';
import { MealService } from '../../service/meal.service'; 
import { DayService } from '../../service/day.service';
import { AuthService } from '../../service/auth-service.service'; 
import { Meal } from '../../model/meal'; 
import { Day } from '../../model/day'; 
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-weekplan',
  templateUrl: './weekplan.component.html',
  styleUrls: ['./weekplan.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule]
})
export class WeekplanComponent implements OnInit {
  showModal = false;  // Flag to control the modal visibility

  weekNumber: number = 0;
  days: Day[] = [];  // Array to hold the days for the logged-in user
  meal: Meal = {  
    mealId: 0,
    dayId: 0,
    type: '',
    description: ''
  };

  selectedDay: Day | null = null;
  userId: number = 0;  // User ID to fetch their days

  constructor(
    private mealService: MealService,  
    private authService: AuthService,  
    private dayService: DayService
  ) {}

  ngOnInit(): void {
    this.weekNumber = this.getWeekNumber(new Date());
    this.getLoggedInUserId();  // Get logged-in user ID
  }

  // Get the logged-in user's ID
  getLoggedInUserId(): void {
    this.authService.getUser().subscribe((user) => {
      this.userId = user.userId;  
      this.loadDays();  
    });
  }

  // Open the modal to add a meal
  openModal() {
    this.showModal = true;
  }

  // Close the modal
  closeModal() {
    this.showModal = false;
  }

  // Select a day and prepare the meal form, then open the modal
  selectDay(day: Day): void {
    this.selectedDay = day;
    this.meal = {  
      mealId: 0,
      dayId: day.dayId,  
      type: '',
      description: ''
    };
    this.openModal();  // Open the modal after selecting a day
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
    this.dayService.getUserDays(this.userId).subscribe((days) => {
      console.log('Days for user:', days);  
      this.days = days;  

      // Optionally, you can also fetch meals for each day as you were doing before
      this.days.forEach((day) => {
        this.mealService.getMealsByDay(day.dayId).subscribe((meals) => {
          day.meals = meals;  
        });
      });
    });
  }  

  // Get the meal description for a specific type (e.g., BREAKFAST)
  getMeal(day: Day, type: string): string {
    const meal = day.meals.find((m: Meal) => m.type === type);
    return meal ? meal.description : '-';  
  }

  // Add a new meal for the selected day
  addMeal(): void {
    if (this.selectedDay) {
      this.meal.dayId = this.selectedDay.dayId;  
      this.mealService.addMeal(this.meal).subscribe(
        (response) => {
          console.log('Meal added:', response);
          this.selectedDay!.meals.push(response);  
          this.resetMealForm();  
        },
        (error) => {
          console.error('Error adding meal:', error);
        }
      );
    }
  }

  // Reset the meal form
  resetMealForm() {
    this.meal = { mealId: 0, dayId: 0, type: '', description: '' };  
  }

  // Cancel meal form and reset
  cancelMealForm(): void {
    this.showModal = false;
    this.resetMealForm();  
  }
}
