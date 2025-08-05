import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormBuilder, type FormGroup, Validators, ReactiveFormsModule } from "@angular/forms"
import { RouterModule, Router } from "@angular/router"
import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatSelectModule } from "@angular/material/select"
import { MatStepperModule } from "@angular/material/stepper"
import { CartService } from "@core/services/cart.service"
import type { Cart } from "@core/models/cart.model"

@Component({
  selector: "app-checkout",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatStepperModule,
  ],
  template: `
    <div class="checkout-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Checkout</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <mat-stepper [linear]="true" #stepper>
            <mat-step [stepControl]="shippingForm" label="Shipping Information">
              <form [formGroup]="shippingForm">
                <div class="form-row">
                  <mat-form-field appearance="outline" class="half-width">
                    <mat-label>First Name</mat-label>
                    <input matInput formControlName="firstName" required>
                  </mat-form-field>
                  <mat-form-field appearance="outline" class="half-width">
                    <mat-label>Last Name</mat-label>
                    <input matInput formControlName="lastName" required>
                  </mat-form-field>
                </div>

                <mat-form-field appearance="outline" class="full-width">
                  <mat-label>Address</mat-label>
                  <input matInput formControlName="address" required>
                </mat-form-field>

                <div class="form-row">
                  <mat-form-field appearance="outline" class="half-width">
                    <mat-label>City</mat-label>
                    <input matInput formControlName="city" required>
                  </mat-form-field>
                  <mat-form-field appearance="outline" class="half-width">
                    <mat-label>Postal Code</mat-label>
                    <input matInput formControlName="postalCode" required>
                  </mat-form-field>
                </div>

                <div class="step-actions">
                  <button mat-raised-button color="primary" matStepperNext>Next</button>
                </div>
              </form>
            </mat-step>

            <mat-step [stepControl]="paymentForm" label="Payment Information">
              <form [formGroup]="paymentForm">
                <mat-form-field appearance="outline" class="full-width">
                  <mat-label>Card Number</mat-label>
                  <input matInput formControlName="cardNumber" placeholder="1234 5678 9012 3456" required>
                </mat-form-field>

                <div class="form-row">
                  <mat-form-field appearance="outline" class="half-width">
                    <mat-label>Expiry Date</mat-label>
                    <input matInput formControlName="expiryDate" placeholder="MM/YY" required>
                  </mat-form-field>
                  <mat-form-field appearance="outline" class="half-width">
                    <mat-label>CVV</mat-label>
                    <input matInput formControlName="cvv" placeholder="123" required>
                  </mat-form-field>
                </div>

                <mat-form-field appearance="outline" class="full-width">
                  <mat-label>Cardholder Name</mat-label>
                  <input matInput formControlName="cardholderName" required>
                </mat-form-field>

                <div class="step-actions">
                  <button mat-button matStepperPrevious>Back</button>
                  <button mat-raised-button color="primary" matStepperNext>Next</button>
                </div>
              </form>
            </mat-step>

            <mat-step label="Review Order">
              <div class="order-summary">
                <h3>Order Summary</h3>
                <div *ngIf="cart && cart.items.length > 0">
                  <div *ngFor="let item of cart.items" class="order-item">
                    <span>{{ item.productName }} x {{ item.quantity }}</span>
                    <span>\${{ (item.unitPrice * item.quantity) | number:'1.2-2' }}</span>
                  </div>
                  <div class="order-total">
                    <strong>Total: \${{ getCartTotal() | number:'1.2-2' }}</strong>
                  </div>
                </div>

                <div class="step-actions">
                  <button mat-button matStepperPrevious>Back</button>
                  <button mat-raised-button color="primary" (click)="placeOrder()">
                    Place Order
                  </button>
                </div>
              </div>
            </mat-step>
          </mat-stepper>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [
    `
    .checkout-container {
      padding: 20px;
      max-width: 800px;
      margin: 0 auto;
    }

    .form-row {
      display: flex;
      gap: 16px;
    }

    .full-width {
      width: 100%;
      margin-bottom: 16px;
    }

    .half-width {
      flex: 1;
      margin-bottom: 16px;
    }

    .step-actions {
      display: flex;
      gap: 16px;
      justify-content: flex-end;
      margin-top: 24px;
    }

    .order-summary {
      padding: 20px 0;
    }

    .order-item {
      display: flex;
      justify-content: space-between;
      padding: 8px 0;
      border-bottom: 1px solid #eee;
    }

    .order-total {
      padding: 16px 0;
      font-size: 1.2rem;
      text-align: right;
    }

    @media (max-width: 768px) {
      .form-row {
        flex-direction: column;
      }
    }
  `,
  ],
})
export class CheckoutComponent implements OnInit {
  private fb = inject(FormBuilder)
  private cartService = inject(CartService)
  private router = inject(Router)

  shippingForm: FormGroup
  paymentForm: FormGroup
  cart: Cart | null = null

  constructor() {
    this.shippingForm = this.fb.group({
      firstName: ["", Validators.required],
      lastName: ["", Validators.required],
      address: ["", Validators.required],
      city: ["", Validators.required],
      postalCode: ["", Validators.required],
    })

    this.paymentForm = this.fb.group({
      cardNumber: ["", Validators.required],
      expiryDate: ["", Validators.required],
      cvv: ["", Validators.required],
      cardholderName: ["", Validators.required],
    })
  }

  ngOnInit(): void {
    this.loadCart()
  }

  loadCart(): void {
    this.cartService.getCart().subscribe({
      next: (cart) => {
        this.cart = cart
      },
      error: (error) => {
        console.error("Error loading cart:", error)
      },
    })
  }

  getCartTotal(): number {
    if (!this.cart || !this.cart.items) return 0
    return this.cart.items.reduce((total, item) => total + item.unitPrice * item.quantity, 0)
  }

  placeOrder(): void {
    // Mock order placement
    console.log("Order placed successfully!")
    this.router.navigate(["/orders"])
  }
}
