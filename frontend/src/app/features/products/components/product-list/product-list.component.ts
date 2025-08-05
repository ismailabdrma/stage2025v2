import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { RouterModule } from "@angular/router"
import { FormsModule } from "@angular/forms"
import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatSelectModule } from "@angular/material/select"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ProductService } from "@core/services/product.service"
import { CartService } from "@core/services/cart.service"
import type { Product, Category } from "@core/models/product.model"

@Component({
  selector: "app-product-list",
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: "./product-list.component.html",
  styleUrl: "./product-list.component.scss",
})
export class ProductListComponent implements OnInit {
  private productService = inject(ProductService)
  private cartService = inject(CartService)
  private snackBar = inject(MatSnackBar)

  products: Product[] = []
  categories: Category[] = []
  filteredProducts: Product[] = []
  loading = true
  searchTerm = ""
  selectedCategory = ""

  ngOnInit(): void {
    this.loadProducts()
    this.loadCategories()
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products
        this.filteredProducts = products
        this.loading = false
      },
      error: (error) => {
        console.error("Error loading products:", error)
        this.loading = false
      },
    })
  }

  loadCategories(): void {
    this.productService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories
      },
      error: (error) => {
        console.error("Error loading categories:", error)
      },
    })
  }

  onSearch(): void {
    this.filterProducts()
  }

  onCategoryChange(): void {
    this.filterProducts()
  }

  filterProducts(): void {
    this.filteredProducts = this.products.filter((product) => {
      const matchesSearch =
        product.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        product.description.toLowerCase().includes(this.searchTerm.toLowerCase())

      const matchesCategory = !this.selectedCategory || product.categoryName === this.selectedCategory

      return matchesSearch && matchesCategory
    })
  }

  addToCart(product: Product): void {
    this.cartService
      .addItemToCart({
        productId: product.id,
        productName: product.name,
        quantity: 1,
        unitPrice: product.displayedPrice,
      })
      .subscribe({
        next: () => {
          this.snackBar.open(`${product.name} added to cart!`, "Close", {
            duration: 3000,
          })
        },
        error: (error) => {
          console.error("Error adding to cart:", error)
          this.snackBar.open("Error adding product to cart", "Close", {
            duration: 3000,
          })
        },
      })
  }
}
