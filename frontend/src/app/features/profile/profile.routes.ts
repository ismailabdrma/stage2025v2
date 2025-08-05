import type { Routes } from "@angular/router"

export const profileRoutes: Routes = [
  {
    path: "",
    loadComponent: () => import("./components/profile/profile.component").then((m) => m.ProfileComponent),
  },
  {
    path: "addresses",
    loadComponent: () => import("./components/addresses/addresses.component").then((m) => m.AddressesComponent),
  },
]
