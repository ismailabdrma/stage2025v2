
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel
import { ProductService } from '../../core/services/product.service';
import { Product, Category } from '../../core/models/product.model'; // Assuming Product and Category models exist
import { AuthService } from '../../core/services/auth.service'; // Import AuthService
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule], // Add FormsModule here
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [CommonModule, RouterModule, FormsModule, MatProgressSpinnerModule, MatCardModule, MatButtonModule],
})
export class HomeComponent implements OnInit {
  categories: Category[] = [];
  featuredProducts: Product[] = [];
  searchKeyword: string = '';
  isAdmin: boolean = false; // Add isAdmin property
  loadingCategories = true;
  loadingFeaturedProducts = true;

  private productService = inject(ProductService);
  private router = inject(Router);
  private authService = inject(AuthService); // Inject AuthService
  private snackBar = inject(MatSnackBar);
  
  ngOnInit(): void {
    this.loadCategories();
    this.loadFeaturedProducts();
  }

  loadCategories(): void {
    this.productService.getAllCategories().subscribe({
      this.loadingCategories = true;
      next: (data) => {
        this.categories = data;
        this.loadingCategories = false;
      },
      error: (error) => {
        console.error('Error fetching categories:', error);
        this.snackBar.open('Failed to load categories.', 'Close', { duration: 3000 });
        this.loadingCategories = false;
      },
    );
  }

  loadFeaturedProducts(): void {
    this.productService.getFeaturedProducts().subscribe({
      this.loadingFeaturedProducts = true;
      next: (data) => {
        this.featuredProducts = data;
        this.loadingFeaturedProducts = false;
      },
      error: (error) => {
        console.error('Error fetching featured products:', error);
        this.snackBar.open('Failed to load featured products.', 'Close', { duration: 3000 });
        this.loadingFeaturedProducts = false;
      }
    );
  }

 navigateToCategory(categoryId: number): void {
 this.router.navigate(['/products'], { queryParams: { category: categoryId } });
  }

 navigateToProductDetail(productId: number): void {
 this.router.navigate(['/products', productId]);
  }
}