import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface PurchaseItem {
  userId: number;
  purchaseListName: string;
  itemName: string;
  quantity: number;
  purchaseListId?: number;
}

export interface PurchaseListResponse {
  purchaseListId: number;
  purchaseListName: string;
  items: {
    itemName: string;
    quantity: number;
  }[];
}

@Injectable({
  providedIn: 'root'
})
export class PurchaseListService {
  private apiUrl = 'http://localhost:8080/api/v1/purchase-list';

  constructor(private http: HttpClient) { }

  createPurchaseList(item: PurchaseItem): Observable<PurchaseListResponse> {
    return this.http.post<PurchaseListResponse>(this.apiUrl, item);
  }

  createEmptyList(userId: number, purchaseListName: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/create`, {
      userId,
      purchaseListName
    });
  }  

  removeItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getAllItems(): Observable<PurchaseItem[]> {
    return this.http.get<PurchaseItem[]>(this.apiUrl, { withCredentials: true });
  }

  getUserPurchaseLists(userId: number): Observable<PurchaseItem[]> {
    return this.http.get<PurchaseItem[]>(`${this.apiUrl}/user/${userId}`);
  }

  getItemsByListName(listName: string): Observable<PurchaseItem[]> {
    return this.http.get<PurchaseItem[]>(`${this.apiUrl}/by-list/${listName}`);
  }
  
  getGroupedLists(): Observable<Record<string, PurchaseItem[]>> {
    return this.http.get<Record<string, PurchaseItem[]>>(`${this.apiUrl}/grouped`);
  }

  shareListWithUser(listId: number, username: string): Observable<string> {
    return this.http.post(
      `${this.apiUrl}/${listId}/share?username=${username}`,
      {},
      { responseType: 'text' }
    );
  }
}
