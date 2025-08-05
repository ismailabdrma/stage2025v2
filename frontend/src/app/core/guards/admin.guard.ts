import { inject } from "@angular/core"
import { Router, type CanActivateFn } from "@angular/router"
import { Store } from "@ngrx/store"
import { map, take } from "rxjs/operators"
import { selectUser } from "../store/auth/auth.selectors"

export const adminGuard: CanActivateFn = () => {
  const store = inject(Store)
  const router = inject(Router)

  return store.select(selectUser).pipe(
    take(1),
    map((user) => {
      if (user?.role === "ADMIN") {
        return true
      }
      router.navigate(["/products"])
      return false
    }),
  )
}
