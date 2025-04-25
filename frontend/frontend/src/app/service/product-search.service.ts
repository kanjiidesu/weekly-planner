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

  constructor(private http: HttpClient) {}

  searchProducts(query: string): Observable<Offer[]> {
    return this.http.get<Offer[]>(`/api/v1/products/search?query=${query}`);
  }
}