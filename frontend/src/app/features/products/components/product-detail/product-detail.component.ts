import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { RouterModule, ActivatedRoute } from "@angular/router"
import { FormsModule } from "@angular/forms"
import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ProductService } from "@core/services/product.service"
import { CartService } from "@core/services/cart.service"
import type { Product } from "@core/models/product.model"

@Component({
  selector: "app-product-detail",
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
    MatProgressSpinnerModule,
  ],
  templateUrl: "./product-detail.component.html",
  styleUrl: "./product-detail.component.scss",
})
export class ProductDetailComponent implements OnInit {
  private route = inject(ActivatedRoute)
  private productService = inject(ProductService)
  private cartService = inject(CartService)
  private snackBar = inject(MatSnackBar)

  product: Product | null = null
  loading = true
  quantity = 1
  selectedImage = ""

  ngOnInit(): void {
    const id = this.route.snapshot.params["id"]
    if (id) {
      this.loadProduct(+id)
    }
  }

  loadProduct(id: number): void {
    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.product = product
        this.selectedImage = product.imageUrls[0] || ""
        this.loading = false
      },
      error: (error) => {
        console.error("Error loading product:", error)
        this.loading = false
      },
    })
  }

  selectImage(image: string): void {
    this.selectedImage = image
  }

  addToCart(): void {
    if (!this.product) return

    this.cartService
      .addItemToCart({
        productId: this.product.id,
        productName: this.product.name,
        quantity: this.quantity,
        unitPrice: this.product.displayedPrice,
      })
      .subscribe({
        next: () => {
          this.snackBar.open(`${this.product!.name} added to cart!`, "Close", {
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

  addToWishlist(): void {
    // TODO: Implement wishlist functionality
    this.snackBar.open("Wishlist feature coming soon!", "Close", {
      duration: 3000,
    })
  }
}
