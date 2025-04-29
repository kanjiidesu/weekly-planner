import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Meal } from '../model/meal';  // Assuming Meal model is defined in 'model/meal'
import { Day } from '../model/day';

@Injectable({
  providedIn: 'root'
})
export class MealService {
  private apiUrl = 'http://86.52.114.30:8081/api/v1/meals';
  //private apiUrl = 'http://localhost:8080/api/v1/meals';  // Your Spring Boot API URL
  //private apiUrl = 'http://localhost:8081/api/v1/meals';

  constructor(private http: HttpClient) {}

  // Add a meal
  addMeal(meal: Meal): Observable<Meal> {
    return this.http.post<Meal>(this.apiUrl, meal);
  }

  // Get meals by dayId
  getMealsByDay(dayId: number): Observable<Meal[]> {
    return this.http.get<Meal[]>(`${this.apiUrl}/day/${dayId}`);
  }

  // Fetch the days assigned to a specific user
  getDaysByUser(userId: number): Observable<Day[]> {
    return this.http.get<Day[]>(`${this.apiUrl}/days?userId=${userId}`);
  }

  deleteMeal(mealId: number): Observable<void> {
    return this.http.delete<void>(`http://86.52.114.30:8081/api/v1/meals/${mealId}`);
    //return this.http.delete<void>(`http://localhost:8080/api/v1/meals/${mealId}`);
  }
}
