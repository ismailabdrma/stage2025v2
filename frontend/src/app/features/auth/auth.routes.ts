import type { Routes } from "@angular/router";

export const authRoutes: Routes = [
  {
    path: "login",
    loadComponent: () => import("./components/login/login.component").then(m => m.LoginComponent),
  },
  {
    path: "verify-otp",
    loadComponent: () => import("./components/verify-otp/verify-otp.component").then(m => m.VerifyOtpComponent),
  },
  {
    path: "signup",
    loadComponent: () => import("./components/signup/signup.component").then(m => m.SignupComponent),
  },
  {
    path: "verify-email",
    loadComponent: () => import("./components/verify-email/verify-email.component").then(m => m.VerifyEmailComponent),
  },
  {
    path: "forgot-password",
    loadComponent: () => import("./components/forgot-password/forgot-password.component").then(m => m.ForgotPasswordComponent),
  },
  {
    path: "reset-password",
    loadComponent: () => import("./components/reset-password/reset-password.component").then(m => m.ResetPasswordComponent),
  },
  {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
  }
];
