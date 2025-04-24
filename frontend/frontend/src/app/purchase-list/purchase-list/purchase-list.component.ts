import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PurchaseListService, PurchaseItem } from '../../service/purchase-list.service';
import { AuthService } from '../../service/auth-guard.component';
import { Router } from '@angular/router';

interface DisplayItem {
  id: number;
  name: string;
  quantity: number;
  purchaseListId?: number;
}

interface ListWithItems {
  id: number;  // List ID should be from the backend as purchaseListId
  name: string;
  items: DisplayItem[];
  newItemName?: string;
  newItemQuantity?: number | null;
  shareWithUsername?: string;
}

@Component({
  selector: 'app-purchase-list',
  standalone: true,
  templateUrl: './purchase-list.component.html',
  styleUrls: ['./purchase-list.component.css'],
  imports: [CommonModule, FormsModule]
})
export class PurchaseListComponent implements OnInit {
  newListName = '';
  allLists: ListWithItems[] = [];
  userId: number = 0;

  constructor(
    private purchaseListService: PurchaseListService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.clearPurchaseLists();
      this.router.navigate(['/login']);
    } else {
      this.authService.getUser().subscribe((user) => {
        if (user) {
          this.userId = user.userId;
          this.loadAllLists();
        } else {
          console.error('User not found');
        }
      });
    }
  }

  clearPurchaseLists() {
    this.allLists = [];
    localStorage.removeItem('lastListName');
  }

  loadAllLists() {
    this.purchaseListService.getUserPurchaseLists(this.userId).subscribe(purchaseLists => {
      console.log('Received purchase lists:', purchaseLists);

      this.allLists = purchaseLists.map((purchaseList: any) => {
        const items: DisplayItem[] = (purchaseList.items || []).map((item: any) => ({
          id: item.id,  // Correctly use the item's 'id'
          name: item.itemName,
          quantity: item.quantity ?? 0,
          purchaseListId: purchaseList.purchaseListId  // Use 'purchaseListId' for list association
        }));

        return {
          id: purchaseList.purchaseListId,  // Use 'purchaseListId' for the list ID
          name: purchaseList.purchaseListName,
          items,
          newItemName: '',
          newItemQuantity: null
        };
      });

      console.log('Formatted lists:', this.allLists);
    });
  }

  shareList(list: ListWithItems) {
    const username = list.shareWithUsername?.trim();
    if (!username || !list.items.length || !list.items[0].purchaseListId) return;

    const listId = list.items[0].purchaseListId;

    this.purchaseListService.shareListWithUser(listId, username).subscribe({
      next: (res) => alert(`Shared with ${username}`),
      error: (err) => alert(`Failed to share list: ${err.error}`),
    });

    list.shareWithUsername = ''; // Clear input
  }

  createList() {
    const trimmedName = this.newListName.trim();
    if (!trimmedName) {
      alert('Please provide a valid list name!');
      return;
    }

    this.purchaseListService.createEmptyList(this.userId, trimmedName).subscribe({
      next: (res) => {
        const newList: ListWithItems = {
          id: res.purchaseListId, // Use 'purchaseListId' here
          name: res.purchaseListName,
          items: [],
          newItemName: '',
          newItemQuantity: 1,
          shareWithUsername: ''
        };

        this.allLists.push(newList);
        this.newListName = ''; // Reset input
      },
      error: (err) => {
        console.error('Failed to create list:', err);
        alert('Could not create the list. Please try again.');
      }
    });
  }

  addItemToList(list: ListWithItems) {
    const name = list.newItemName?.trim();
    const qty = list.newItemQuantity;

    if (!name || !qty || qty < 1) {
      alert('Please provide a valid item name and quantity.');
      return;
    }

    const nameLower = name.toLowerCase();
    const existingItem = list.items.find(
      item => item.name && item.name.toLowerCase() === nameLower
    );

    if (existingItem) {
      existingItem.quantity += qty;
      alert(`Item "${name}" already exists. Quantity updated.`);
    } else {
      const newItem: PurchaseItem = {
        userId: this.userId,
        purchaseListName: list.name,
        itemName: name,
        quantity: qty
      };

      this.purchaseListService.createPurchaseList(newItem).subscribe({
        next: (savedList) => {
          const lastItem = savedList.items[savedList.items.length - 1];

          if (!lastItem?.itemName || lastItem.quantity == null) {
            alert('Something went wrong — received invalid item data.');
            return;
          }

          list.items.push({
            id: lastItem.id,                 
            name: lastItem.itemName,
            quantity: Number(lastItem.quantity),
            purchaseListId: savedList.purchaseListId
          });

          list.newItemName = '';
          list.newItemQuantity = null;
        },
        error: (err) => {
          console.error('Failed to save item', err);
          alert('Failed to save item. Please try again.');
        }
      });
    }
  }

  removeItemFromList(list: ListWithItems, index: number) {
    const item = list.items[index];
    if (!item) return;

    if (item.id) {
      this.purchaseListService.removeItem(item.id).subscribe((response) => {
        console.log(response);

        list.items.splice(index, 1);

        if (list.items.length === 0) {
          const listIndex = this.allLists.findIndex(existingList => existingList.id === list.id);
          if (listIndex !== -1) {
            this.allLists.splice(listIndex, 1);
          }
        }
      }, (error) => {
        console.error('Error removing item:', error);
      });
    } else {
      list.items.splice(index, 1);
      if (list.items.length === 0) {
        const listIndex = this.allLists.findIndex(existingList => existingList.id === list.id);
        if (listIndex !== -1) {
          this.allLists.splice(listIndex, 1);
        }
      }
    }
  }

  removeWholeList(list: ListWithItems) {
    console.log("Trying to remove list", list.id);

    if (!list.id) {
      console.error('List does not have an ID!');
      return;
    }

    if (confirm(`Are you sure you want to delete the list "${list.name}" and all its items?`)) {
      this.purchaseListService.deleteList(list.id).subscribe(() => {
        const index = this.allLists.findIndex(l => l.id === list.id);
        if (index !== -1) {
          this.allLists.splice(index, 1);
        }
      }, error => {
        console.error('Failed to delete list:', error);
      });
    }
  }
}
