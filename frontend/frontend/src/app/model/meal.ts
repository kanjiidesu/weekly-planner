export interface Meal {
  mealId: number;
  dayId: number;
  type: string;  // e.g., 'BREAKFAST', 'LUNCH', 'DINNER'
  description: string;
}