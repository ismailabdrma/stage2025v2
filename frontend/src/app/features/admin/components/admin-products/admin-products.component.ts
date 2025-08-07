import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { RouterModule } from "@angular/router"
import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatTableModule } from "@angular/material/table"
import { MatPaginatorModule } from "@angular/material/paginator"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatDialog, MatDialogModule } from "@angular/material/dialog"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ProductService } from "@core/services/product.service"
import type { Product } from "@core/models/product.model"
import { CreateProductDialogComponent } from "./create-product-dialog/create-product-dialog.component"
import { ConfirmDialogComponent } from "../../../../shared/components/confirm-dialog/confirm-dialog.component"

@Component({
  selector: "app-admin-products",
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
  ],
  templateUrl: "./admin-products.component.html",
  styleUrl: "./admin-products.component.scss",
})
export class AdminProductsComponent implements OnInit {
  private productService = inject(ProductService)
  private dialog = inject(MatDialog)
  private snackBar = inject(MatSnackBar)

  products: Product[] = []
  displayedColumns: string[] = ["id", "name", "category", "price", "stock", "actions"]
  loading = false
  
  ngOnInit(): void {
 this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true
    this.productService.getAllProducts({ includeInactive: true }).subscribe({
      next: (products) => {
        this.products = products
        this.loading = false
      },
      error: (error) => {
        this.snackBar.open("Error loading products.", "Close", { duration: 3000 })
        this.products = []
        this.loading = false
      },
    })
  }

  openCreateProductDialog(): void {
    const dialogRef = this.dialog.open(CreateProductDialogComponent, {
      width: "500px",
      data: null,
    })

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadProducts()
      }
    })
  }

  openEditProductDialog(product: Product): void {
    const dialogRef = this.dialog.open(CreateProductDialogComponent, {
      width: "500px",
      data: product,
    })

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadProducts()
      }
    })
  }

  deleteProduct(id: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: "Confirm Deletion",
        message: "Are you sure you want to delete this product?",
      },
    })

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.productService.deleteProduct(id).subscribe({
          next: () => {
            this.snackBar.open("Product deleted successfully.", "Close", { duration: 3000 })
            this.loadProducts()
          },
          error: (error) => {
            this.snackBar.open("Error deleting product.", "Close", { duration: 3000 })
          },
        },
        )
      }
    })
  }

  toggleProductStatus(product: Product): void {
    this.productService.updateProductStatus(product.id, !product.isActive).subscribe({
      next: (updatedProduct) => {
        this.snackBar.open(`Product ${updatedProduct.isActive ? 'activated' : 'deactivated'} successfully.`, "Close", { duration: 3000 })
        this.loadProducts()
      },
      error: (error) => {
        this.snackBar.open("Error updating product status.", "Close", { duration: 3000 })
        },
      })
    }
  }
}
