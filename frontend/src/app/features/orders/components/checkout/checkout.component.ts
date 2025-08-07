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
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CartService } from "@core/services/cart.service"
import { AddressService } from "@core/services/address.service"
import { MatDialog } from "@angular/material/dialog"
import { AddressDialogComponent } from "@app/features/profile/components/address-dialog/address-dialog.component"
import { OrderService } from "@core/services/order.service"
import { PaymentService } from "@core/services/payment.service"
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
 MatProgressSpinnerModule
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

 <mat-form-field appearance="outline" class="full-width">
 <mat-label>Select Address</mat-label>
 <mat-select [(ngModel)]="selectedAddressId" (selectionChange)="onAddressSelect()" [ngModelOptions]="{standalone: true}">
 <mat-option *ngFor="let address of addresses" [value]="address.id">
 {{ address.label }} - {{ address.address }}, {{ address.city }}
 </mat-option>
 </mat-select>
 </mat-form-field>

 <button mat-button (click)="openAddressDialog()">
 Add New Address
 </button>
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
                  <button mat-raised-button color="primary" (click)="placeOrder()" [disabled]="placingOrder">
 <mat-spinner diameter="20" *ngIf="placingOrder"></mat-spinner>
 <span *ngIf="!placingOrder">
 Place Order
 </span>
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
  private orderService = inject(OrderService)
  private paymentService = inject(PaymentService)
 private addressService = inject(AddressService)
 private dialog = inject(MatDialog);
 private snackBar = inject(MatSnackBar);


  shippingForm: FormGroup
  paymentForm: FormGroup
  cart: Cart | null = null
  loadingCart = true;
  loadingAddresses = true;
  placingOrder = false;

  constructor() {
    this.shippingForm = this.fb.group({
      firstName: ["", Validators.required],
      lastName: ["", Validators.required],
      address: ["", Validators.required],
      city: ["", Validators.required],
      postalCode: ["", Validators.required],
 country: [""],
 phone: [""]
    })

    this.paymentForm = this.fb.group({
      cardNumber: ["", Validators.required],
      expiryDate: ["", Validators.required],
      cvv: ["", Validators.required],
      cardholderName: ["", Validators.required],
    })
  }

  ngOnInit(): void {
 this.loadCart();
 this.loadAddresses();
  }

  loadAddresses(): void {
 this.addressService.getAddresses().subscribe({
 next: (addresses) => {
 this.addresses = addresses;
      },
 error: (error) => console.error("Error loading addresses:", error)
    });
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

 onAddressSelect(): void {
    const selectedAddress = this.addresses.find(addr => addr.id === this.selectedAddressId);
    if (selectedAddress) {
      this.shippingForm.patchValue(selectedAddress);
    }
  }

 openAddressDialog(): void {
    const dialogRef = this.dialog.open(AddressDialogComponent, {
      width: \'400px\',
 data: null // No data for adding a new address
    });

 dialogRef.afterClosed().subscribe(result => {
      if (result) {
 this.addressService.addAddress(result).subscribe({
 next: () => this.loadAddresses(), // Refresh list after adding
 error: (error) => console.error("Error adding address:", error)
        });
      }
    });
  }

  placeOrder(): void {
    if (this.shippingForm.valid && this.paymentForm.valid && this.cart && this.cart.items.length > 0) {
      const orderRequest = {
        shippingAddress: this.shippingForm.value,
        orderItems: this.cart.items.map(item => ({
          productId: item.productId,
          quantity: item.quantity
        }))
      };

      this.orderService.createOrder(orderRequest).subscribe({
        next: (order) => {
          const paymentData = {
            orderId: order.id,
            paymentMethod: 'card', // Assuming card for now
            paymentInfo: this.paymentForm.value // Sensitive payment info should be handled securely
          };
          this.paymentService.processPayment(paymentData).subscribe({
            next: () => this.router.navigate(['/orders', order.id]),
            error: (error) => console.error("Payment processing failed:", error) // Handle payment error
          });
        },
        error: (error) => console.error("Order creation failed:", error) // Handle order creation error
      });
    }
  }
}
