import type { Routes } from "@angular/router"
import { authGuard } from "./core/guards/auth.guard"
import { adminGuard } from "./core/guards/admin.guard"
import { HomeComponent } from "./features/home/home.component"

export const routes: Routes = [
  {
    path: "",
    component: HomeComponent,
  },
  {
    path: "auth",
    loadChildren: () => import("./features/auth/auth.routes").then((m) => m.authRoutes),
  },
  {
    path: "products",
    loadChildren: () => import("./features/products/products.routes").then((m) => m.productsRoutes),
  },
  {
    path: "cart",
    loadChildren: () => import("./features/cart/cart.routes").then((m) => m.cartRoutes),
    canActivate: [authGuard],
  },
  {
    path: "orders",
    loadChildren: () => import("./features/orders/orders.routes").then((m) => m.ordersRoutes),
    canActivate: [authGuard],
  },
  {
    path: "profile",
    loadChildren: () => import("./features/profile/profile.routes").then((m) => m.profileRoutes),
    canActivate: [authGuard],
  },
  {
    path: "admin",
    loadChildren: () => import("./features/admin/admin.routes").then((m) => m.adminRoutes),
    canActivate: [authGuard, adminGuard],
  },
  {
    path: "**",
    redirectTo: "/products",
  },
]
