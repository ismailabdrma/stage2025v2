import type { Routes } from "@angular/router"

export const productsRoutes: Routes = [
  {
    path: "",
    loadComponent: () => import("./components/product-list/product-list.component").then((m) => m.ProductListComponent),
  },
  {
    path: ":id",
    loadComponent: () =>
      import("./components/product-detail/product-detail.component").then((m) => m.ProductDetailComponent),
  },
]
