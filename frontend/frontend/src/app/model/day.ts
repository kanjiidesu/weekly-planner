import { Meal } from "./meal";

export interface Day {
    dayId: number;
    userId: number;
    weekday: string;
    meals: Meal[];
  }
  