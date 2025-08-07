import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { CartService } from '../../core/services/cart.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  cart: any;
  private cartService = inject(CartService);
  private router = inject(Router);

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.cartService.getCart().subscribe(
      (data) => {
        this.cart = data;
      },
      (error) => {
        console.error('Error loading cart:', error);
        // TODO: Implement user-friendly error handling
      }
    );
  }

  updateQuantity(item: any): void {
    // Ensure quantity is at least 1
    if (item.quantity < 1) {
      item.quantity = 1;
    }
    this.cartService.updateItemQuantity(item.id, item.quantity).subscribe(
      (data) => {
        this.cart = data; // Assuming API returns updated cart
      },
      (error) => {
        console.error('Error updating item quantity:', error);
        // TODO: Implement user-friendly error handling (e.g., check for insufficient stock)
        this.loadCart(); // Reload cart to revert local changes on error
      }
    );
  }

  removeItem(itemId: number): void {
    this.cartService.removeItemFromCart(itemId).subscribe(
      () => {
        this.loadCart(); // Reload cart after successful removal
      },
      (error) => {
        console.error('Error removing item from cart:', error);
        // TODO: Implement user-friendly error handling
      }
    );
  }

  clearCart(): void {
    this.cartService.clearCart().subscribe(
      () => {
        this.loadCart(); // Reload cart after successful clear
      },
      (error) => {
        console.error('Error clearing cart:', error);
        // TODO: Implement user-friendly error handling
      }
    );
  }

  get subtotal(): number {
    if (!this.cart || !this.cart.items) {
      return 0;
    }
    return this.cart.items.reduce(
      (sum: number, item: any) => sum + item.product.price * item.quantity,
      0
    );
  }

  get total(): number {
    // TODO: Include shipping costs if available
    return this.subtotal;
  }

  get isCartEmpty(): boolean {
    return !this.cart || !this.cart.items || this.cart.items.length === 0;
  }

  goToCheckout(): void {
    this.router.navigate(['/checkout']);
  }
}