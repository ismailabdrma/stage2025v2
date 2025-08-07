import { Component, Inject, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';

@Component({
  selector: 'app-address-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule
  ],
  template: `
    <h2 mat-dialog-title>{{ data ? 'Edit Address' : 'Add New Address' }}</h2>
    <div mat-dialog-content>
      <form [formGroup]="addressForm">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Label (e.g., Home, Work)</mat-label>
          <input matInput formControlName="label">
        </mat-form-field>

        <mat-form-field appearance="outline" class="half-width">
          <mat-label>First Name</mat-label>
          <input matInput formControlName="firstName" required>
          <mat-error *ngIf="addressForm.get('firstName')?.hasError('required')">
            First Name is required
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="half-width">
          <mat-label>Last Name</mat-label>
          <input matInput formControlName="lastName" required>
          <mat-error *ngIf="addressForm.get('lastName')?.hasError('required')">
            Last Name is required
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Address</mat-label>
          <input matInput formControlName="address" required>
          <mat-error *ngIf="addressForm.get('address')?.hasError('required')">
            Address is required
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="half-width">
          <mat-label>City</mat-label>
          <input matInput formControlName="city" required>
          <mat-error *ngIf="addressForm.get('city')?.hasError('required')">
            City is required
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="half-width">
          <mat-label>Postal Code</mat-label>
          <input matInput formControlName="postalCode" required>
          <mat-error *ngIf="addressForm.get('postalCode')?.hasError('required')">
            Postal Code is required
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Country</mat-label>
          <input matInput formControlName="country" required>
          <mat-error *ngIf="addressForm.get('country')?.hasError('required')">
            Country is required
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Phone</mat-label>
          <input matInput formControlName="phone">
        </mat-form-field>

        <mat-checkbox formControlName="isDefault">Set as Default Address</mat-checkbox>
      </form>
    </div>
    <div mat-dialog-actions align="end">
      <button mat-button (click)="onNoClick()">Cancel</button>
      <button mat-raised-button color="primary" [disabled]="addressForm.invalid" (click)="onSaveClick()">Save</button>
    </div>
  `,
  styles: [
    `
      .full-width {
        width: 100%;
      }
      .half-width {
        width: 48%; // Adjust as needed
        margin-right: 4%; // Adjust as needed
        &:last-child {
          margin-right: 0;
        }
      }
    `
  ]
})
export class AddressDialogComponent implements OnInit {
  private fb = inject(FormBuilder);
  addressForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<AddressDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.addressForm = this.fb.group({
      label: [''],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required],
      postalCode: ['', Validators.required],
      country: ['', Validators.required],
      phone: [''],
      isDefault: [false]
    });
  }

  ngOnInit(): void {
    if (this.data) {
      this.addressForm.patchValue(this.data);
    }
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSaveClick(): void {
    if (this.addressForm.valid) {
      this.dialogRef.close(this.addressForm.value);
    }
  }
}