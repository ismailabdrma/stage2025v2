// src/app/core/interceptors/auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const token = authService.getToken();

    // Endpoints that should NOT receive the token
    const noAuthEndpoints = [
        '/api/auth/login',
        '/api/auth/signup',
        '/api/auth/verify-email',
        '/api/auth/login/verify-otp',
        '/api/auth/resend-otp',
        '/api/auth/forgot-password',
        '/api/auth/reset-password',
    ];

    // Check if the request URL ends with one of the above endpoints
    const shouldSkipAuth = noAuthEndpoints.some((endpoint) => req.url.endsWith(endpoint));

    if (token && !shouldSkipAuth) {
        return next(
            req.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`,
                },
                withCredentials: true,
            })
        );
    }

    return next(req);
};
