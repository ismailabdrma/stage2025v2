import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '@core/services/product.service'; // Assuming ProductService path
import { MatSnackBar } from '@angular/material/snack-bar';

import { CartService } from '../../core/services/cart.service';
@Component({
 // Add MatProgressSpinnerModule if not already imported in a shared module
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private productService = inject(ProductService);
  private cartService = inject(CartService);
  private snackBar = inject(MatSnackBar);

  product: any; // Replace 'any' with your Product model type
  suggestedProducts: any[] = [];
  loadingProduct = true;
  imageUrls: string[] = [];

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const productId = Number(params.get('id'));
      if (productId) {
        this.loadProduct(productId);
      }
    });
  }

  loadProduct(id: number): void {
    this.loadingProduct = true;
    this.productService.getProductById(id).subscribe(
      (data) => {
        this.product = data;
        this.imageUrls = data.imageUrls || []; // Assuming imageUrls property exists in product data
        this.loadingProduct = false;
        this.loadSuggestedProducts(id); // Load suggested products after main product
      },
      (error) => {
        console.error('Error fetching product details:', error);
        this.snackBar.open('Failed to load product details. Please try again.', 'Close', {
          duration: 3000,
        });
        this.loadingProduct = false;
      }
    );
  }

  loadSuggestedProducts(productId: number): void {
    // Add a loading indicator for suggested products if needed
    this.productService.getSuggestedProducts(productId).subscribe(
      (data) => {
        this.suggestedProducts = data;
      },
      (error) => {
        console.error('Error fetching suggested products:', error);
        this.snackBar.open('Failed to load suggested products.', 'Close', { duration: 3000 });
      }
    )
  }

  get isProductAvailable(): boolean {
    return this.product && this.product.stock > 0;
  }

  addToCart(): void {
    if (this.product) {
      const cartItem = { productId: this.product.id, quantity: 1 };
      this.cartService.addItemToCart(cartItem).subscribe(
        (response) => {
 console.log('Product added to cart', response);
          this.snackBar.open('Product added to cart!', 'Close', { duration: 3000 });
 },
        (error) => {
 console.error('Error adding product to cart:', error);
          this.snackBar.open('Failed to add product to cart.', 'Close', { duration: 3000 });
 }
      );
    }
  }

  addToWishlist(): void {
    this.snackBar.open('Adding to wishlist is not yet implemented.', 'Close', { duration: 3000 });
  }
}