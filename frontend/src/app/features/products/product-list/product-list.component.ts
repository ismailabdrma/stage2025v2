import { Component, OnInit, inject, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { ProductService } from '@core/services/product.service';
import { ProductCardComponent } from '@shared/components/product-card/product-card.component'; // Assuming this path
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel
import { MatSnackBar } from '@angular/material/snack-bar';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { SupplierService } from '@core/services/supplier.service';
import { MatInputModule } from '@angular/material/input';
import { Subject, debounceTime, distinctUntilChanged, Subscription } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';

@Component({ // Added OnDestroy
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ProductCardComponent, FormsModule, MatProgressSpinnerModule, MatPaginatorModule, MatSelectModule, MatOptionModule, MatFormFieldModule, MatInputModule], // Add FormsModule
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
})
export class ProductListComponent implements OnInit {
  private snackBar = inject(MatSnackBar);
  private productService = inject(ProductService);
  private route = inject(ActivatedRoute);
  private router = inject(Router); // Inject Router
  private supplierService = inject(SupplierService);

  private searchTerms = new Subject<string>(); // Subject for search terms
  private subscriptions: Subscription[] = []; // Array to hold subscriptions

  products: any[] = [];
  categories: any[] = [];
  suppliers: any[] = []; // Added suppliers array
  selectedCategory: string | null = null;
  selectedSupplier: string | null = null;
  searchTerm: string | null = null;
  sortBy: string = 'name'; // Default sort
  sortDirection: string = 'asc'; // Default sort direction


  currentPage: number = 1;
  pageSize: number = 12; // Default page size
  totalProducts: number = 0;
  totalPages: number = 0;
 loadingProducts = true;
 loadingSuppliers = true;
 // Removed loadingCategories as it's not used

  ngOnInit(): void { // Implemented debouncing for search
    // Subscribe to query params to load initial state and products
    this.subscriptions.push(this.route.queryParams.subscribe(params => {
      this.searchTerm = params['search'] || ''; // Initialize searchTerm
      this.selectedCategory = params['category'] || null;
      this.selectedSupplier = params['supplier'] || null;
      this.sortBy = params['sortBy'] || 'name';
      this.sortDirection = params['sortDirection'] || 'asc';
      this.currentPage = +params['page'] || 1; // Convert to number
      this.pageSize = +params['size'] || 12; // Convert to number

      this.loadProducts();
    }));

    // Debounce search terms and trigger product loading
    this.subscriptions.push(this.searchTerms.pipe(
      debounceTime(300), // wait 300ms after the last event before emitting
      distinctUntilChanged(), // only emit if value has changed
    ).subscribe(() => this.applyFilters())); // Apply filters when search term changes
  }

  loadProducts(): void {
    this.loadingProducts = true;
    const params: any = {
      page: this.currentPage - 1, // Backend usually expects 0-based page index
      size: this.pageSize,
      sortBy: this.sortBy
      // sortDirection: this.sortDirection // Uncomment if backend supports sort direction
     };

    if (this.selectedCategory) params['category'] = this.selectedCategory;
    if (this.selectedSupplier) params['supplier'] = this.selectedSupplier;
    if (this.searchTerm) params['search'] = this.searchTerm;

    this.productService.getAllProducts(params).subscribe(
      (data: any) => { // Adjust type based on API response structure
        this.products = data.content; // Assuming paginated response with 'content'
        this.totalProducts = data.totalElements; // Assuming paginated response with 'totalElements'
       // this.totalPages = data.totalPages; // Assuming paginated response with 'totalPages'
       // Calculate totalPages manually if not provided
        this.totalPages = Math.ceil(this.totalProducts / this.pageSize);
        this.loadingProducts = false;

        // Update query params in URL
        this.updateQueryParams();
      },
      (error) => {
        this.snackBar.open('Error fetching products: ' + error.message, 'Close', { duration: 3000 });
      }
    );
  }

  // Removed this method as categories are loaded in ngOnInit
  loadCategories(): void {
    this.productService.getAllCategories().subscribe(
      (data) => {
        this.categories = data;
      },
      (error) => {
        console.error('Error fetching categories:', error);
        this.snackBar.open('Failed to load categories.', 'Close', { duration: 3000 });
      }
    );
  }

  loadSuppliers(): void {
 this.loadingSuppliers = true;
 this.supplierService.getAllSuppliers().subscribe({
      next: (suppliers) => {
 this.suppliers = suppliers;
 this.loadingSuppliers = false;
      },
      error: (error) => {
 console.error('Error fetching suppliers:', error);
 this.snackBar.open('Failed to load suppliers for filtering.', 'Close', { duration: 3000 });
        this.loadingSuppliers = false;
      },
  }

  onCategoryChange(categoryId: string): void {
    this.selectedCategory = categoryId === '' ? null : categoryId;
    this.applyFilters();
  }

  onSupplierChange(supplierId: string): void {
    this.selectedSupplier = supplierId === '' ? null : supplierId;
    this.applyFilters();
  }

  // Method to handle search input changes
  onSearchInput(event: Event): void {
    const term = (event.target as HTMLInputElement).value;
    this.searchTerms.next(term); // Push the new term to the subject
  }

 onSortChange(sortValue: string): void {
 this.sortBy = sortValue;
 this.currentPage = 1; // Reset to first page on sort change
 this.loadProducts();
  }

  // Method to handle supplier select changes (corrected signature)
  onSupplierChange(supplierId: string | null): void {
    this.selectedSupplier = supplierId === '' ? null : supplierId;
    this.applyFilters();
  }

  applyFilters(): void {
    this.currentPage = 1; // Reset to first page on applying filters
    this.loadProducts();
  }

  // Method to update query params in the URL
  private updateQueryParams(): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        search: this.searchTerm,
        category: this.selectedCategory,
        supplier: this.selectedSupplier,
        sortBy: this.sortBy,
        // sortDirection: this.sortDirection, // Uncomment if sort direction is added
        page: this.currentPage,
        size: this.pageSize
      },
      queryParamsHandling: 'merge', // Merge with existing query params
      replaceUrl: true // Replace the current URL in history
    });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadProducts();
  }
}