import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PurchaseListService, PurchaseItem } from '../../service/purchase-list.service';
import { AuthService } from '../../service/auth-guard.component'
import { Router } from '@angular/router';

interface DisplayItem {
  name: string;
  quantity: number;
  purchaseListId?: number;
}

interface ListWithItems {
  name: string;
  items: DisplayItem[];
  newItemName?: string;
  newItemQuantity?: number | null; // <-- updated this line
}

@Component({
  selector: 'app-purchase-list',
  standalone: true,
  templateUrl: './purchase-list.component.html',
  styleUrl: './purchase-list.component.css',
  imports: [CommonModule, FormsModule]
})
export class PurchaseListComponent {
  newListName = '';
  allLists: ListWithItems[] = [];

  constructor(
    private purchaseListService: PurchaseListService,
    private authService: AuthService, // Assuming you have an auth service
    private router: Router // To navigate the user
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.clearPurchaseLists();
      this.router.navigate(['/login']); // Redirect to login page
    } else {
      this.loadAllLists();
    }
  }

  clearPurchaseLists() {
    this.allLists = [];
    localStorage.removeItem('lastListName'); // Clear any stored list data
  }

  loadAllLists() {
    this.purchaseListService.getAllItems().subscribe(items => {
      const grouped: { [key: string]: DisplayItem[] } = {};

      items.forEach(item => {
        const listName = item.purchaseListName;
        if (!grouped[listName]) {
          grouped[listName] = [];
        }
        grouped[listName].push({
          name: item.itemName,
          quantity: Number(item.quantity),
          purchaseListId: item.purchaseListId
        });
      });

      this.allLists = Object.entries(grouped).map(([name, items]) => ({
        name,
        items,
        newItemName: '',
        newItemQuantity: null
      }));
    });
  }

  createList() {
    if (!this.newListName.trim()) return;

    const existing = this.allLists.find(list => list.name === this.newListName);
    if (existing) return; // prevent duplicate lists

    this.allLists.push({
      name: this.newListName,
      items: [],
      newItemName: '',
      newItemQuantity: null
    });

    this.newListName = '';
  }

  addItemToList(list: ListWithItems) {
    const name = list.newItemName?.trim();
    const qty = list.newItemQuantity;

    if (!name || !qty || qty < 1) return;

    const item: PurchaseItem = {
      purchaseListName: list.name,
      itemName: name,
      quantity: String(qty)
    };

    this.purchaseListService.addItem(item).subscribe(savedItem => {
      list.items.push({
        name: savedItem.itemName,
        quantity: Number(savedItem.quantity),
        purchaseListId: savedItem.purchaseListId
      });

      list.newItemName = '';
      list.newItemQuantity = null;
    });
  }

  removeItemFromList(list: ListWithItems, index: number) {
    const item = list.items[index];
    if (!item) return;
  
    if (item.purchaseListId) {
      this.purchaseListService.removeItem(item.purchaseListId).subscribe(() => {
        // Remove the item from the list
        list.items.splice(index, 1);
  
        // If the list is now empty, remove the list from allLists
        if (list.items.length === 0) {
          const listIndex = this.allLists.findIndex(existingList => existingList.name === list.name);
          if (listIndex !== -1) {
            this.allLists.splice(listIndex, 1);  // Remove the list from allLists
          }
        }
      });
    } else {
      list.items.splice(index, 1);
  
      // If the list is now empty, remove the list from allLists
      if (list.items.length === 0) {
        const listIndex = this.allLists.findIndex(existingList => existingList.name === list.name);
        if (listIndex !== -1) {
          this.allLists.splice(listIndex, 1);  // Remove the list from allLists
        }
      }
    }
  }  
}
