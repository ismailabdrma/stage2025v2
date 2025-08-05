import type { Routes } from "@angular/router"

export const ordersRoutes: Routes = [
  {
    path: "",
    loadComponent: () => import("./components/order-list/order-list.component").then((m) => m.OrderListComponent),
  },
  {
    path: "checkout",
    loadComponent: () => import("./components/checkout/checkout.component").then((m) => m.CheckoutComponent),
  },
  {
    path: ":id",
    loadComponent: () => import("./components/order-detail/order-detail.component").then((m) => m.OrderDetailComponent),
  },
]
