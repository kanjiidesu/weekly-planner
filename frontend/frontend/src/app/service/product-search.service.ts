import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Offer {
  title: string;
  price: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://86.52.114.30:8081/api/v1/products/search?query=';


  constructor(private http: HttpClient) {}

  searchProducts(query: string): Observable<Offer[]> {
    return this.http.get<Offer[]>(`${this.apiUrl}${query}`);
  }
}