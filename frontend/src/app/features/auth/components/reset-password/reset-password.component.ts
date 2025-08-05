import { Component, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormBuilder, type FormGroup, Validators, ReactiveFormsModule } from "@angular/forms"
import { RouterModule } from "@angular/router"
import { MatCardModule } from "@angular/material/card"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { Store } from "@ngrx/store"
import type { Observable } from "rxjs"
import { AuthActions } from "@core/store/auth/auth.actions"
import { selectAuthLoading } from "@core/store/auth/auth.selectors"

@Component({
  selector: "app-reset-password",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: "./reset-password.component.html",
  styleUrl: "./reset-password.component.scss",
})
export class ResetPasswordComponent {
  private fb = inject(FormBuilder)
  private store = inject(Store)

  resetPasswordForm: FormGroup
  hidePassword = true
  hideConfirmPassword = true
  loading$: Observable<boolean> = this.store.select(selectAuthLoading)

  constructor() {
    this.resetPasswordForm = this.fb.group(
      {
        email: ["", [Validators.required, Validators.email]],
        otp: ["", [Validators.required, Validators.pattern(/^\d{6}$/)]],
        newPassword: ["", [Validators.required, Validators.minLength(6)]],
        confirmPassword: ["", [Validators.required]],
      },
      { validators: this.passwordMatchValidator },
    )
  }

  passwordMatchValidator(form: FormGroup) {
    const newPassword = form.get("newPassword")
    const confirmPassword = form.get("confirmPassword")

    if (newPassword && confirmPassword && newPassword.value !== confirmPassword.value) {
      confirmPassword.setErrors({ mismatch: true })
    } else {
      confirmPassword?.setErrors(null)
    }

    return null
  }

  onSubmit(): void {
    if (this.resetPasswordForm.valid) {
      const { email, otp, newPassword } = this.resetPasswordForm.value
      this.store.dispatch(AuthActions.resetPassword({ email, otp, newPassword }))
    }
  }
}
