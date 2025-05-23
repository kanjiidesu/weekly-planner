import { Component, OnInit } from '@angular/core';
import { MealService } from '../../service/meal.service';
import { DayService } from '../../service/day.service';
import { AuthService } from '../../service/auth-service.service';
import { Meal } from '../../model/meal';
import { Day } from '../../model/day';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-weekplan',
  templateUrl: './weekplan.component.html',
  styleUrls: ['./weekplan.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule, MatIconModule, HttpClientModule]
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
    if (this.authService.isLoggedIn()) {
      this.weekNumber = this.getWeekNumber(new Date());
      this.getLoggedInUserId();  // Get logged-in user ID
    } else {
      console.error('User is not logged in');
    }
  }
  getMealObject(day: Day, type: string): Meal | undefined {
    return day.meals.find((m: Meal) => m.type === type);
  }

  // Get the logged-in user's ID
  getLoggedInUserId(): void {
    this.authService.getUser().subscribe((user) => {
      if (user) {
        this.userId = user.userId;  // Set userId if user is found
        console.log("Fetching days for user ID:", this.userId);
        this.loadDays();  // Proceed to load days after getting user ID
        console.log("i got days")
      } else {
        console.error('User not found in localStorage');
      }
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

  printPage() {
    window.print();
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
    const target = new Date(date.valueOf());
    const dayNr = (date.getDay() + 6) % 7; // Make Monday=0, Sunday=6
    target.setDate(target.getDate() - dayNr + 3); // Nearest Thursday
    const firstThursday = new Date(target.getFullYear(), 0, 4);
    const diff = target.getTime() - firstThursday.getTime();
    return 1 + Math.round(diff / (7 * 24 * 60 * 60 * 1000));
  }

  // Load days for the logged-in user
  loadDays(): void {
    console.log("Hello from loadDays method", this.dayService.getUserDays)
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
      this.meal.dayId = this.selectedDay.dayId; // Send only the dayId
      console.log('Meal to be added:', this.meal);  // Log the meal object before sending
  
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
    } else {
      console.error('No selected day!');
    }
  }

  deleteMeal(mealId: number, day: Day): void {
    this.mealService.deleteMeal(mealId).subscribe(
      () => {
        day.meals = day.meals.filter((m: Meal) => m.mealId !== mealId);
        console.log(`Meal with ID ${mealId} deleted.`);
      },
      (error) => {
        console.error('Error deleting meal:', error);
      }
    );
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
