import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { RouterOutlet, RouterModule } from "@angular/router"
import { MatToolbarModule } from "@angular/material/toolbar"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatSidenavModule } from "@angular/material/sidenav"
import { MatListModule } from "@angular/material/list"
import { MatMenuModule } from "@angular/material/menu"
import { MatBadgeModule } from "@angular/material/badge"
import { Store } from "@ngrx/store"
import type { Observable } from "rxjs"
import { AuthService } from "./core/services/auth.service"
import { CartService } from "./core/services/cart.service"
import { selectUser, selectIsAuthenticated } from "./core/store/auth/auth.selectors"
import { AuthActions } from "./core/store/auth/auth.actions"
import type { User } from "./core/models/user.model"

@Component({
  selector: "app-root",
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatMenuModule,
    MatBadgeModule,
  ],
  template: `
    <div class="app-container">
      <mat-toolbar color="primary" class="app-toolbar">
        <button mat-icon-button (click)="sidenav.toggle()" *ngIf="isAuthenticated$ | async">
          <mat-icon>menu</mat-icon>
        </button>
        
        <span class="app-title">Stage 2025</span>
        
        <span class="spacer"></span>
        
        <ng-container *ngIf="isAuthenticated$ | async; else loginButton">
          <button mat-icon-button routerLink="/cart" [matBadge]="cartItemCount" matBadgeColor="accent">
            <mat-icon>shopping_cart</mat-icon>
          </button>
          
          <button mat-button [matMenuTriggerFor]="userMenu">
            <mat-icon>account_circle</mat-icon>
            {{ (user$ | async)?.username }}
          </button>
          
          <mat-menu #userMenu="matMenu">
            <button mat-menu-item routerLink="/profile">
              <mat-icon>person</mat-icon>
              Profile
            </button>
            <button mat-menu-item routerLink="/orders">
              <mat-icon>receipt</mat-icon>
              My Orders
            </button>
            <mat-divider></mat-divider>
            <button mat-menu-item (click)="logout()">
              <mat-icon>logout</mat-icon>
              Logout
            </button>
          </mat-menu>
        </ng-container>
        
        <ng-template #loginButton>
          <button mat-button routerLink="/auth/login">
            <mat-icon>login</mat-icon>
            Login
          </button>
        </ng-template>
      </mat-toolbar>

      <mat-sidenav-container class="sidenav-container">
        <mat-sidenav #sidenav mode="side" opened="false" class="sidenav">
          <mat-nav-list>
            <a mat-list-item routerLink="/products" routerLinkActive="active">
              <mat-icon matListItemIcon>inventory</mat-icon>
              <span matListItemTitle>Products</span>
            </a>
            <a mat-list-item routerLink="/cart" routerLinkActive="active">
              <mat-icon matListItemIcon>shopping_cart</mat-icon>
              <span matListItemTitle>Cart</span>
            </a>
            <a mat-list-item routerLink="/orders" routerLinkActive="active">
              <mat-icon matListItemIcon>receipt</mat-icon>
              <span matListItemTitle>Orders</span>
            </a>
            <a mat-list-item routerLink="/profile" routerLinkActive="active">
              <mat-icon matListItemIcon>person</mat-icon>
              <span matListItemTitle>Profile</span>
            </a>
            <mat-divider></mat-divider>
            <a mat-list-item routerLink="/admin" routerLinkActive="active" *ngIf="isAdmin">
              <mat-icon matListItemIcon>admin_panel_settings</mat-icon>
              <span matListItemTitle>Admin Panel</span>
            </a>
          </mat-nav-list>
        </mat-sidenav>

        <mat-sidenav-content class="main-content">
          <router-outlet></router-outlet>
        </mat-sidenav-content>
      </mat-sidenav-container>
    </div>
  `,
  styles: [
    `
    .app-container {
      height: 100vh;
      display: flex;
      flex-direction: column;
    }

    .app-toolbar {
      position: sticky;
      top: 0;
      z-index: 1000;
    }

    .app-title {
      font-size: 1.5rem;
      font-weight: 500;
    }

    .spacer {
      flex: 1 1 auto;
    }

    .sidenav-container {
      flex: 1;
    }

    .sidenav {
      width: 250px;
    }

    .main-content {
      padding: 20px;
    }

    .active {
      background-color: rgba(0, 0, 0, 0.04);
    }

    @media (max-width: 768px) {
      .main-content {
        padding: 10px;
      }
    }
  `,
  ],
})
export class AppComponent implements OnInit {
  private store = inject(Store)
  private authService = inject(AuthService)
  private cartService = inject(CartService)

  user$: Observable<User | null> = this.store.select(selectUser)
  isAuthenticated$: Observable<boolean> = this.store.select(selectIsAuthenticated)
  cartItemCount = 0
  isAdmin = false

  ngOnInit(): void {
    const token = localStorage.getItem("token")
    if (token) {
      this.store.dispatch(AuthActions.checkAuthStatus())
    }

    this.user$.subscribe((user) => {
      this.isAdmin = user?.role === "ADMIN"
    })

    this.cartService.getCart().subscribe((cart) => {
      this.cartItemCount = cart?.items?.length || 0
    })
  }

  logout(): void {
    this.store.dispatch(AuthActions.logout())
  }
}
