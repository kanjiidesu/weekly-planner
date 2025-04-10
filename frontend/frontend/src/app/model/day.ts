import { Meal } from "./meal";

export interface Day {
    dayId: number;
    userId: number;
    weekDay: string;
    meals: Meal[];
  }
  