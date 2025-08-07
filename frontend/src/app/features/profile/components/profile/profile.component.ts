import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormBuilder, type FormGroup, Validators, ReactiveFormsModule } from "@angular/forms"
import { RouterModule } from "@angular/router"
import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { Store } from "@ngrx/store"
import { Observable, tap } from "rxjs"
import { selectUser, selectAuthError, selectAuthMessage } from "@core/store/auth/auth.selectors"
import type { User } from "@core/models/user.model"
import { AuthService } from "@core/services/auth.service"
import { MatSnackBar } from "@angular/material/snack-bar"

@Component({
  selector: "app-profile",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
  ],
  template: `
    <div class="profile-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>My Profile</mat-card-title>
          <mat-card-subtitle>Manage your account information</mat-card-subtitle>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="profileForm" (ngSubmit)="onSubmit()" *ngIf="user$ | async as user">
            <div class="form-row">
              <mat-form-field appearance="outline" class="half-width">
                <mat-label>First Name</mat-label>
                <input matInput formControlName="firstName">
              </mat-form-field>
              <mat-form-field appearance="outline" class="half-width">
                <mat-label>Last Name</mat-label>
                <input matInput formControlName="lastName">
              </mat-form-field>
            </div>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Username</mat-label>
              <input matInput formControlName="username" required>
              <mat-error *ngIf="profileForm.get('username')?.hasError('required')">
                Username is required
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Email</mat-label>
              <input matInput type="email" formControlName="email" required>
              <mat-error *ngIf="profileForm.get('email')?.hasError('required')">
                Email is required
              </mat-error>
              <mat-error *ngIf="profileForm.get('email')?.hasError('email')">
                Please enter a valid email
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Phone</mat-label>
              <input matInput formControlName="phone">
            </mat-form-field>

            <div class="form-actions">
              <button mat-button type="button" (click)="resetForm()">Reset</button>
              <button mat-raised-button color="primary" type="submit" 
                      [disabled]="profileForm.invalid || loading">
                <mat-spinner diameter="20" *ngIf="loading"></mat-spinner>
                <span *ngIf="!loading">Update Profile</span>
              </button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>

      <mat-card class="password-card">
        <mat-card-header>
          <mat-card-title>Change Password</mat-card-title>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="passwordForm" (ngSubmit)="onPasswordSubmit()">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Current Password</mat-label>
              <input matInput type="password" formControlName="currentPassword" required>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>New Password</mat-label>
              <input matInput type="password" formControlName="newPassword" required>
              <mat-error *ngIf="passwordForm.get('newPassword')?.hasError('minlength')">
                Password must be at least 6 characters
              </mat-error>
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Confirm New Password</mat-label>
              <input matInput type="password" formControlName="confirmPassword" required>
              <mat-error *ngIf="passwordForm.get('confirmPassword')?.hasError('mismatch')">
                Passwords do not match
              </mat-error>
            </mat-form-field>

            <div class="form-actions">
              <button mat-raised-button color="primary" type="submit" 
                      [disabled]="passwordForm.invalid || passwordLoading">
                <mat-spinner diameter="20" *ngIf="passwordLoading"></mat-spinner>
                <span *ngIf="!passwordLoading">Change Password</span>
              </button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [
    `
    .profile-container {
      padding: 20px;
      max-width: 600px;
      margin: 0 auto;
      display: flex;
      flex-direction: column;
      gap: 24px;
    }

    .form-row {
      display: flex;
      gap: 16px;
    }

    .full-width {
      width: 100%;
      margin-bottom: 16px;
    }

    .half-width {
      flex: 1;
      margin-bottom: 16px;
    }

    .form-actions {
      display: flex;
      gap: 16px;
      justify-content: flex-end;
      margin-top: 24px;
    }

    .password-card {
      margin-top: 24px;
    }

    mat-spinner {
      margin-right: 8px;
    }

    @media (max-width: 768px) {
      .form-row {
        flex-direction: column;
      }
    }
  `,
  ],
})
export class ProfileComponent implements OnInit {
  private fb = inject(FormBuilder)
  private store = inject(Store)
  private authService = inject(AuthService)
  private snackBar = inject(MatSnackBar)

  user$: Observable<User | null> = this.store.select(selectUser)
  error$: Observable<string | null> = this.store.select(selectAuthError)
  profileForm: FormGroup
  passwordForm: FormGroup
  loading = false
  passwordLoading = false

  constructor() {
    this.profileForm = this.fb.group({
      firstName: [""],
      lastName: [""],
      username: ["", Validators.required],
      email: ["", [Validators.required, Validators.email]],
      phone: [""],
    })

    this.passwordForm = this.fb.group(
      {
        currentPassword: ["", Validators.required],
        newPassword: ["", [Validators.required, Validators.minLength(6)]],
        confirmPassword: ["", Validators.required],
      },
      { validators: this.passwordMatchValidator },
    )
  }

  ngOnInit(): void {
    this.subscribeToUser();

    this.error$.subscribe(error => {
      if (error) {
        this.snackBar.open(error, 'Close', { duration: 5000 });
      }
    });

    this.message$.subscribe(message => {
      if (message) {
        this.snackBar.open(message, 'Close', { duration: 5000 });
      }
    });
  }

  private subscribeToUser(): void {
    this.user$.subscribe((user) => {
      if (user) {
        this.profileForm.patchValue({
          firstName: user.firstName || "",
          lastName: user.lastName || "",
          username: user.username,
          email: user.email,
          phone: user.phone || "",
        })
      }
    })
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
    if (this.profileForm.valid) {
      const updatedUser: Partial<User> = this.profileForm.value;
      this.loading = true;
      this.authService.updateUserProfile(updatedUser).subscribe({
        next: (user) => {
          console.log('Profile updated successfully', user);
          this.loading = false;
          this.snackBar.open('Profile updated successfully', 'Close', { duration: 3000 });
        },
        error: (err) => {
          console.error('Profile update failed', err);
          this.loading = false;
          // Error handled by the error$ subscription and MatSnackBar
        }
      });
    }
  }

  onPasswordSubmit(): void {
    if (this.passwordForm.valid) {
      const passwordChangeRequest = this.passwordForm.value;
      this.passwordLoading = true;
      // Assuming changePassword method in AuthService expects an object with currentPassword and newPassword
      this.authService.changePassword(passwordChangeRequest).subscribe({
        next: () => {
          console.log('Password changed successfully');
          this.passwordLoading = false;
          this.passwordForm.reset();
          this.snackBar.open('Password changed successfully', 'Close', { duration: 3000 });
        },
        error: (err) => {
          console.error('Password change failed', err);
          this.passwordLoading = false;
          // Error handled by the error$ subscription and MatSnackBar
        }
      });
    }
  }

  resetForm(): void {
    this.user$.subscribe((user) => {
      if (user) {
        this.profileForm.patchValue({
          firstName: user.firstName || "",
          lastName: user.lastName || "",
          username: user.username,
          email: user.email,
          phone: user.phone || "",
        })
      }
    })
  }
}
