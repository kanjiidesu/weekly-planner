import { Component } from '@angular/core';
import { Offer, ProductService } from '../../service/product-search.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-search',
  standalone: true,
  templateUrl: './product-search.component.html',
  styleUrl: './product-search.component.css',
  imports: [CommonModule, FormsModule]
})
export class ProductSearchComponent {
  query = '';
  offers: Offer[] = [];
  loading = false;

  constructor(private productService: ProductService) {}

  search() {
    if (!this.query.trim()) return;
  
    this.loading = true;
    this.productService.searchProducts(this.query).subscribe({
      next: data => {
        // Sort by price (lowest first)
        this.offers = data.sort((a, b) => Number(a.price) - Number(b.price));
        this.loading = false;
      },
      error: err => {
        console.error('Search failed', err);
        this.loading = false;
      }
    });
  }
}
