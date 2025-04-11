import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Day } from '../model/day';

@Injectable({
  providedIn: 'root'
})
export class DayService {
  private apiUrl = 'http://localhost:8080/api/v1/days';  // Your Spring Boot API URL

  constructor(private http: HttpClient) {}

  getUserDays(userId: number): Observable<Day[]> {
    return this.http.get<Day[]>(`${this.apiUrl}/${userId}`);
  }
}
