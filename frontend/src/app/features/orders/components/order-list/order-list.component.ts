import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { RouterModule } from "@angular/router"
import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { MatChipsModule } from "@angular/material/chips"
import { MatTableModule } from "@angular/material/table";
import { OrderService } from "@core/services/order.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: "app-order-list",
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule,
 @angular/material/snack-bar";
    MatTableModule,
  ],
  template: `
    <div class="order-list-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>My Orders</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <div *ngIf="loading" class="loading-container">
            <mat-spinner></mat-spinner>
            <p>Loading orders...</p>
          </div>

          <div *ngIf="!loading && orders.length > 0">
            <table mat-table [dataSource]="orders" class="orders-table">
              <ng-container matColumnDef="id">
                <th mat-header-cell *matHeaderCellDef>Order ID</th>
                <td mat-cell *matCellDef="let order">#{{ order.id }}</td>
              </ng-container>

              <ng-container matColumnDef="date">
                <th mat-header-cell *matHeaderCellDef>Date</th>
                <td mat-cell *matCellDef="let order">{{ order.createdDate | date:'medium' }}</td>
              </ng-container>

              <ng-container matColumnDef="status">
                <th mat-header-cell *matHeaderCellDef>Status</th>
                <td mat-cell *matCellDef="let order">
                  <mat-chip [color]="getStatusColor(order.status)">
                    {{ order.status }}
                  </mat-chip>
                </td>
              </ng-container>

              <ng-container matColumnDef="total">
                <th mat-header-cell *matHeaderCellDef>Total</th>
                <td mat-cell *matCellDef="let order">\${{ order.total | number:'1.2-2' }}</td>
              </ng-container>

              <ng-container matColumnDef="actions">
                <th mat-header-cell *matHeaderCellDef>Actions</th>
                <td mat-cell *matCellDef="let order">
                  <button mat-button [routerLink]="['/orders', order.id]">
                    <mat-icon>visibility</mat-icon>
                    View Details
                  </button>
                </td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
            </table>
          </div>

          <div *ngIf="!loading && orders.length === 0" class="no-orders">
            <mat-icon>receipt</mat-icon>
            <h3>No orders found</h3>
            <p>You haven't placed any orders yet.</p>
            <button mat-raised-button color="primary" routerLink="/products">
              Start Shopping
            </button>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [
    `
    .order-list-container {
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

    .orders-table {
      width: 100%;
    }

    .no-orders {
      text-align: center;
      padding: 40px;
      color: #666;
    }

    .no-orders mat-icon {
      font-size: 48px;
      height: 48px;
      width: 48px;
      margin-bottom: 16px;
    }
  `,
  ],
})
export class OrderListComponent implements OnInit {
  private orderService = inject(OrderService);
  private snackBar = inject(MatSnackBar);

  orders: any[] = []
  loading = true
  displayedColumns = ["id", "date", "status", "total", "actions"]

  ngOnInit(): void {
 this.orderService.getOrders().subscribe(
      (orders) => {
 this.orders = orders;
 this.loading = false;
 },
      (error) => {
 this.snackBar.open("Failed to load orders.", "Close", {
 duration: 3000,
 });
 this.loading = false;
 }
 );
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
