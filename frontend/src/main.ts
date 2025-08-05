import { bootstrapApplication } from "@angular/platform-browser"
import { AppComponent } from "./app/app.component"
import { provideRouter } from "@angular/router"
import { provideAnimationsAsync } from "@angular/platform-browser/animations/async"
import { provideHttpClient, withInterceptors } from "@angular/common/http"
import { provideStore } from "@ngrx/store"
import { provideEffects } from "@ngrx/effects"
import { provideStoreDevtools } from "@ngrx/store-devtools"
import { routes } from "./app/app.routes"
import { authInterceptor } from "./app/core/interceptors/auth.interceptor"
import { errorInterceptor } from "./app/core/interceptors/error.interceptor"
import { authReducer } from "./app/core/store/auth/auth.reducer"
import { AuthEffects } from "./app/core/store/auth/auth.effects"
import { environment } from "./environments/environment"

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(withInterceptors([authInterceptor, errorInterceptor])),
    provideStore({
      auth: authReducer,
    }),
    provideEffects([AuthEffects]),
    provideStoreDevtools({
      maxAge: 25,
      logOnly: environment.production,
    }),
  ],
}).catch((err) => console.error(err))
