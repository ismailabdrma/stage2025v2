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
import { ProductService } from "@core/services/product.service"
import type { Product } from "@core/models/product.model"
import { CreateProductDialogComponent } from "./create-product-dialog/create-product-dialog.component"

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

  products: Product[] = []
  displayedColumns: string[] = ["id", "name", "category", "price", "stock", "actions"]

  ngOnInit(): void {
    this.loadProducts()
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products
      },
      error: (error) => {
        console.error("Error loading products:", error)
        this.products = []
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
    if (confirm("Are you sure you want to delete this product?")) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.loadProducts()
        },
        error: (error) => {
          console.error("Error deleting product:", error)
        },
      })
    }
  }
}
