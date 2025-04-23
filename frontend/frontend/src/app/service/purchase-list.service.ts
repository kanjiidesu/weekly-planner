import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface PurchaseItem {
  purchaseListId?: number;
  purchaseListName: string;
  itemName: string;
  quantity: string;
}

@Injectable({
  providedIn: 'root'
})
export class PurchaseListService {
  private apiUrl = 'http://localhost:8080/api/v1/purchase-list';

  constructor(private http: HttpClient) { }

  addItem(item: PurchaseItem): Observable<PurchaseItem> {
    return this.http.post<PurchaseItem>(this.apiUrl, item);
  }

  removeItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getAllItems(): Observable<PurchaseItem[]> {
    return this.http.get<PurchaseItem[]>(this.apiUrl);
  }

  getItemsByListName(listName: string): Observable<PurchaseItem[]> {
    return this.http.get<PurchaseItem[]>(`${this.apiUrl}/by-list/${listName}`);
  }
  
  getGroupedLists(): Observable<Record<string, PurchaseItem[]>> {
    return this.http.get<Record<string, PurchaseItem[]>>(`${this.apiUrl}/grouped`);
  }
}
