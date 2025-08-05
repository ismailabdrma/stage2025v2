import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { RouterModule, ActivatedRoute } from "@angular/router"
import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { MatChipsModule } from "@angular/material/chips"
import { MatTableModule } from "@angular/material/table"

@Component({
  selector: "app-order-detail",
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatTableModule,
  ],
  template: `
    <div class="order-detail-container">
      <div *ngIf="loading" class="loading-container">
        <mat-spinner></mat-spinner>
        <p>Loading order details...</p>
      </div>

      <div *ngIf="!loading && order">
        <mat-card>
          <mat-card-header>
            <mat-card-title>Order #{{ order.id }}</mat-card-title>
            <mat-card-subtitle>
              Placed on {{ order.createdDate | date:'medium' }}
            </mat-card-subtitle>
          </mat-card-header>

          <mat-card-content>
            <div class="order-status">
              <mat-chip [color]="getStatusColor(order.status)">
                {{ order.status }}
              </mat-chip>
            </div>

            <div class="order-items">
              <h3>Order Items</h3>
              <table mat-table [dataSource]="order.items" class="items-table">
                <ng-container matColumnDef="product">
                  <th mat-header-cell *matHeaderCellDef>Product</th>
                  <td mat-cell *matCellDef="let item">{{ item.productName }}</td>
                </ng-container>

                <ng-container matColumnDef="quantity">
                  <th mat-header-cell *matHeaderCellDef>Quantity</th>
                  <td mat-cell *matCellDef="let item">{{ item.quantity }}</td>
                </ng-container>

                <ng-container matColumnDef="price">
                  <th mat-header-cell *matHeaderCellDef>Unit Price</th>
                  <td mat-cell *matCellDef="let item">\${{ item.unitPrice | number:'1.2-2' }}</td>
                </ng-container>

                <ng-container matColumnDef="total">
                  <th mat-header-cell *matHeaderCellDef>Total</th>
                  <td mat-cell *matCellDef="let item">\${{ (item.quantity * item.unitPrice) | number:'1.2-2' }}</td>
                </ng-container>

                <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
                <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
              </table>
            </div>

            <div class="order-summary">
              <div class="summary-row">
                <span>Subtotal:</span>
                <span>\${{ order.subtotal | number:'1.2-2' }}</span>
              </div>
              <div class="summary-row">
                <span>Shipping:</span>
                <span>\${{ order.shipping | number:'1.2-2' }}</span>
              </div>
              <div class="summary-row">
                <span>Tax:</span>
                <span>\${{ order.tax | number:'1.2-2' }}</span>
              </div>
              <div class="summary-row total">
                <span><strong>Total:</strong></span>
                <span><strong>\${{ order.total | number:'1.2-2' }}</strong></span>
              </div>
            </div>

            <div class="shipping-info" *ngIf="order.shippingAddress">
              <h3>Shipping Address</h3>
              <p>
                {{ order.shippingAddress.firstName }} {{ order.shippingAddress.lastName }}<br>
                {{ order.shippingAddress.address }}<br>
                {{ order.shippingAddress.city }}, {{ order.shippingAddress.postalCode }}
              </p>
            </div>
          </mat-card-content>

          <mat-card-actions>
            <button mat-button routerLink="/orders">
              <mat-icon>arrow_back</mat-icon>
              Back to Orders
            </button>
            <button mat-button *ngIf="order.status === 'PENDING'">
              Cancel Order
            </button>
          </mat-card-actions>
        </mat-card>
      </div>

      <div *ngIf="!loading && !order" class="not-found">
        <mat-icon>error</mat-icon>
        <h3>Order not found</h3>
        <p>The order you're looking for doesn't exist.</p>
        <button mat-raised-button color="primary" routerLink="/orders">
          Back to Orders
        </button>
      </div>
    </div>
  `,
  styles: [
    `
    .order-detail-container {
      padding: 20px;
      max-width: 1000px;
      margin: 0 auto;
    }

    .loading-container, .not-found {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 40px;
    }

    .order-status {
      margin-bottom: 24px;
    }

    .order-items {
      margin-bottom: 24px;
    }

    .items-table {
      width: 100%;
      margin-top: 16px;
    }

    .order-summary {
      border-top: 1px solid #ddd;
      padding-top: 16px;
      margin-bottom: 24px;
    }

    .summary-row {
      display: flex;
      justify-content: space-between;
      padding: 4px 0;
    }

    .summary-row.total {
      border-top: 1px solid #ddd;
      padding-top: 8px;
      margin-top: 8px;
      font-size: 1.1rem;
    }

    .shipping-info {
      margin-bottom: 24px;
    }
  `,
  ],
})
export class OrderDetailComponent implements OnInit {
  private route = inject(ActivatedRoute)

  order: any = null
  loading = true
  displayedColumns = ["product", "quantity", "price", "total"]

  ngOnInit(): void {
    const id = this.route.snapshot.params["id"]
    if (id) {
      this.loadOrder(+id)
    }
  }

  loadOrder(id: number): void {
    // Mock data for now
    setTimeout(() => {
      this.order = {
        id: id,
        createdDate: new Date(),
        status: "PENDING",
        items: [
          {
            productName: "Sample Product",
            quantity: 2,
            unitPrice: 49.99,
          },
        ],
        subtotal: 99.98,
        shipping: 9.99,
        tax: 8.99,
        total: 118.96,
        shippingAddress: {
          firstName: "John",
          lastName: "Doe",
          address: "123 Main St",
          city: "Anytown",
          postalCode: "12345",
        },
      }
      this.loading = false
    }, 1000)
  }

  getStatusColor(status: string): string {
    switch (status) {
      case "PENDING":
        return "warn"
      case "SHIPPED":
        return "primary"
      case "DELIVERED":
        return "accent"
      default:
        return ""
    }
  }
}
