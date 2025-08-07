

import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { RouterModule } from "@angular/router"
import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { MatTableModule } from "@angular/material/table"
import { MatInputModule } from "@angular/material/input"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatDialog, MatDialogRef } from "@angular/material/dialog"
import { MatSnackBar } from "@angular/material/snack-bar"
import { ConfirmDialogComponent } from "@shared/components/confirm-dialog/confirm-dialog.component" // Assuming path to ConfirmDialogComponent
import { CartService } from "@core/services/cart.service"
import type { Cart, CartItem } from "@core/models/cart.model"

@Component({
  selector: "app-cart",
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule, // Import MatDialogModule
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatInputModule,
    MatFormFieldModule,
  ],
  template: `
    <div class="cart-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Shopping Cart</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <div *ngIf="loading" class="loading-container">
            <mat-spinner></mat-spinner>
            <p>Loading cart...</p>
          </div>

          <div *ngIf="!loading && cart && cart.items.length > 0">
            <table mat-table [dataSource]="cart.items" class="cart-table">
              <ng-container matColumnDef="product">
                <th mat-header-cell *matHeaderCellDef>Product</th>
                <td mat-cell *matCellDef="let item">{{ item.productName }}</td>
              </ng-container>

              <ng-container matColumnDef="price">
                <th mat-header-cell *matHeaderCellDef>Price</th>
                <td mat-cell *matCellDef="let item">\${{ item.unitPrice | number:'1.2-2' }}</td>
              </ng-container>

              <ng-container matColumnDef="quantity">
                <th mat-header-cell *matHeaderCellDef>Quantity</th>
                <td mat-cell *matCellDef="let item">
                  <div class="quantity-controls">
                    <button mat-icon-button (click)="updateQuantity(item, item.quantity - 1)">
                      <mat-icon>remove</mat-icon>
                    </button>
                    <span class="quantity">{{ item.quantity }}</span>
                    <button mat-icon-button (click)="updateQuantity(item, item.quantity + 1)">
                      <mat-icon>add</mat-icon>
                    </button>
                  </div>
                </td>
              </ng-container>

              <ng-container matColumnDef="total">
                <th mat-header-cell *matHeaderCellDef>Total</th>
                <td mat-cell *matCellDef="let item">\${{ (item.unitPrice * item.quantity) | number:'1.2-2' }}</td>
              </ng-container>

              <ng-container matColumnDef="actions">
                <th mat-header-cell *matHeaderCellDef>Actions</th>
                <td mat-cell *matCellDef="let item">
                  <button mat-icon-button color="warn" (click)="removeItem(item)">
                    <mat-icon>delete</mat-icon>
                  </button>
                </td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
            </table>

            <div class="cart-summary">
              <h3>Total: \${{ getCartTotal() | number:'1.2-2' }}</h3>
              <div class="cart-actions">
                <button mat-button (click)="clearCart()">Clear Cart</button>
                <button mat-raised-button color="primary" routerLink="/orders/checkout">
                  Proceed to Checkout
                </button>
              </div>
            </div>
          </div>

          <div *ngIf="!loading && (!cart || cart.items.length === 0)" class="no-items">
            <mat-icon>shopping_cart</mat-icon>
            <h3>Your cart is empty</h3>
            <p>Add some products to get started!</p>
            <button mat-raised-button color="primary" routerLink="/products">
              Browse Products
            </button>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [
    `
    .cart-container {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }

    .loading-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 40px;
    }

    .cart-table {
      width: 100%;
      margin-bottom: 20px;
    }

    .quantity-controls {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .quantity {
      min-width: 30px;
      text-align: center;
    }

    .cart-summary {
      text-align: right;
      padding: 20px 0;
      border-top: 1px solid #ddd;
    }

    .cart-actions {
      display: flex;
      gap: 16px;
      justify-content: flex-end;
      margin-top: 16px;
    }

  `,
  ],
})
export class CartComponent implements OnInit {
  private cartService = inject(CartService)

  private dialog = inject(MatDialog) // Inject MatDialog
  private snackBar = inject(MatSnackBar) // Inject MatSnackBar
  cart: Cart | null = null
  loading = true
  displayedColumns = ["product", "price", "quantity", "total", "actions"]

  ngOnInit(): void {
    this.loadCart()
  }

  loadCart(): void {
    this.cartService.getCart().subscribe({
      next: (cart) => {
        this.cart = cart
        this.loading = false
      },
      error: (error) => {
        console.error("Error loading cart:", error)
        this.loading = false
      },
    })
  }

  updateQuantity(item: CartItem, newQuantity: number): void {
    if (newQuantity <= 0) {
      this.removeItem(item)
      return
    }

    this.cartService.updateItemQuantity(item.id, newQuantity).subscribe({
      next: (cart) => {
        this.cart = cart
      },
      error: (error: any) => {
        let errorMessage = 'Failed to update quantity.';
        if (error.status === 400 && error.error?.message) { // Assuming backend sends a specific error message for insufficient stock
          errorMessage = error.error.message;
        }
        console.error("Error updating quantity:", error)
      },
    })
  }

  removeItem(item: CartItem): void {
    this.cartService.removeItemFromCart(item.id).subscribe({
      next: () => {
        this.loadCart()
      },
      error: (error: any) => {
 this.snackBar.open('Failed to remove item from cart.', 'Close', { duration: 3000 });

        console.error("Error removing item:", error)
      },
    })
  }

  clearCart(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, { // Open confirmation dialog
      data: {
        title: "Clear Cart",
        message: "Are you sure you want to clear your cart?",
      },
    })

    dialogRef.afterClosed().subscribe((result) => {
      if (result) { // If user confirms
        this.cartService.clearCart().subscribe({
          next: () => this.loadCart(),
          error: (error: any) => {
 this.snackBar.open('Failed to clear cart.', 'Close', { duration: 3000 });
 console.error("Error clearing cart:", error)},
        })
      }
    })
  }

  getCartTotal(): number {
    if (!this.cart || !this.cart.items) return 0
    return this.cart.items.reduce((total, item) => total + item.unitPrice * item.quantity, 0)
  }
}
