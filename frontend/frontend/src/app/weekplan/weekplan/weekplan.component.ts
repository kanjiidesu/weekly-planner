import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Meal {
  type: string;
  name: string;
}

interface Day {
  name: string;
  meals: Meal[];
}

@Component({
  selector: 'app-weekplan',
  templateUrl: './weekplan.component.html',
  standalone: true,
  styleUrls: ['./weekplan.component.css'],
  imports: [CommonModule]
})
export class WeekplanComponent {
  days: Day[] = [
    { name: 'Monday', meals: [] },
    { name: 'Tuesday', meals: [] },
    { name: 'Wednesday', meals: [] },
    { name: 'Thursday', meals: [] },
    { name: 'Friday', meals: [] },
    { name: 'Saturday', meals: [] },
    { name: 'Sunday', meals: [] }
  ];

  // To add meal to a specific day
  addMeal(day: Day, type: string, name: string): void {
    day.meals.push({ type, name });
  }
}
