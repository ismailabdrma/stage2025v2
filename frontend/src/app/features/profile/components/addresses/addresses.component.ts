 import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { ReactiveFormsModule } from "@angular/forms"
import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatDialogModule, MatDialog } from "@angular/material/dialog"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { ConfirmDialogComponent } from "@shared/components/confirm-dialog/confirm-dialog.component"

import { AddressService } from "@core/services/address.service";
import { AddressDialogComponent } from "../address-dialog/address-dialog.component";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: "app-addresses",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatProgressSpinnerModule,
  ],
  template: `
    <div class="addresses-container">
      <div class="header">
        <h2>My Addresses</h2>
        <button mat-raised-button color="primary" (click)="openAddressDialog()">
          <mat-icon>add</mat-icon>
          Add New Address
        </button>
      </div>

      <div *ngIf="loading" class="loading-container">
        <mat-spinner></mat-spinner>
        <p>Loading addresses...</p>
      </div>

      <div class="addresses-grid" *ngIf="!loading">
        <mat-card *ngFor="let address of addresses" class="address-card">
          <mat-card-header>
            <mat-card-title>{{ address.label }}</mat-card-title>
            <mat-card-subtitle *ngIf="address.isDefault">Default Address</mat-card-subtitle>
          </mat-card-header>

          <mat-card-content>
            <p>
              {{ address.firstName }} {{ address.lastName }}<br>
              {{ address.address }}<br>
              {{ address.city }}, {{ address.postalCode }}<br>
              {{ address.country }}
            </p>
            <p *ngIf="address.phone">
              <strong>Phone:</strong> {{ address.phone }}
            </p>
          </mat-card-content>

          <mat-card-actions>
            <button mat-button (click)="editAddress(address)">
              <mat-icon>edit</mat-icon>
              Edit
            </button>
            <button mat-button color="warn" (click)="deleteAddress(address.id)">
              <mat-icon>delete</mat-icon>
              Delete
            </button>
            <button mat-button *ngIf="!address.isDefault" (click)="setDefault(address.id)">
              <mat-icon>star</mat-icon>
              Set Default
            </button>
          </mat-card-actions>
        </mat-card>
      </div>

      <div *ngIf="!loading && addresses.length === 0" class="no-items">
        <mat-icon>location_off</mat-icon>
        <h3>No addresses found</h3>
        <p>Add your first address to get started.</p>
        <button mat-raised-button color="primary" (click)="openAddressDialog()">
          Add Address
        </button>
      </div>
    </div>
  `,
  styles: [
    `
    .addresses-container {
      padding: 20px;
      max-width: 1000px;
      margin: 0 auto;
    }

    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;
    }

    .loading-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 40px;
    }

    .addresses-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 20px;
    }

    .address-card {
      height: fit-content;
    }

    .no-addresses {
      text-align: center;
      padding: 40px;
      color: #666;
    }

    .no-addresses mat-icon {
      font-size: 48px;
      height: 48px;
      width: 48px;
      margin-bottom: 16px;
    }

    @media (max-width: 768px) {
      .header {
        flex-direction: column;
        gap: 16px;
        align-items: stretch;
      }

      .addresses-grid {
        grid-template-columns: 1fr;
      }
    }
  `,
  ],
})
export class AddressesComponent implements OnInit {
  private dialog = inject(MatDialog)
  private addressService = inject(AddressService);
  private snackBar = inject(MatSnackBar);

  addresses: any[] = []
  loading = true

  ngOnInit(): void {
 this.loading = true;
 this.addressService.getAddresses().subscribe({
 next: (addresses) => {
 this.addresses = addresses;
 this.loading = false;
      },
 error: (err) => {
 console.error("Error loading addresses:", err);
 this.snackBar.open("Failed to load addresses", "Close", { duration: 3000 });
 this.loading = false;
      },
    });
  }

  openAddressDialog(address?: any): void {
    const dialogRef = this.dialog.open(AddressDialogComponent, {
      width: '500px',
      data: address ? { ...address } : {},
    });

 dialogRef.afterClosed().subscribe(result => {
 if (result) {
 if (result.id) {
 this.addressService.updateAddress(result.id, result).subscribe({
 next: () => this.loadAddresses(),
 error: (err) => this.snackBar.open("Failed to update address", "Close", { duration: 3000 }),
            });
          } else {
 this.addressService.addAddress(result).subscribe({
 next: () => this.loadAddresses(),
 error: (err) => this.snackBar.open("Failed to add address", "Close", { duration: 3000 }),
            });
          }
        }
      });
  }

  editAddress(address: any): void {
 this.openAddressDialog(address);
  }

  deleteAddress(id: number): void {
    if (confirm("Are you sure you want to delete this address?")) {
 this.addressService.deleteAddress(id).subscribe({
 next: () => this.loadAddresses(),
 error: (err) => this.snackBar.open("Failed to delete address", "Close", { duration: 3000 }),
      });
    }
  }

  setDefault(id: number): void {
    // Implement logic to set default address using the API
    // This might require a new endpoint or updating the address with isDefault: true and setting others to false
    console.log("Set default address not implemented yet for ID:", id);
    this.snackBar.open("Setting default address is not yet implemented.", "Close", { duration: 3000 });

    // Temporary mock update for immediate visual feedback
 this.addresses = this.addresses.map((addr) => ({
      ...addr,
      isDefault: addr.id === id,
    }));
  }
}


      this.loading = false
    }, 1000)
  }

  openAddressDialog(address?: any): void {
    // Mock dialog opening
    console.log("Opening address dialog", address)
  }

  editAddress(address: any): void {
    this.openAddressDialog(address)
  }

  deleteAddress(id: number): void {
    this.addresses = this.addresses.filter((addr) => addr.id !== id)
  }

  setDefault(id: number): void {
    this.addresses = this.addresses.map((addr) => ({
      ...addr,
      isDefault: addr.id === id,
    }))
  }
}
