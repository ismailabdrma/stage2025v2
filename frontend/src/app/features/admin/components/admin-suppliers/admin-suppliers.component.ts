import { Component, OnInit, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import { MatChipsModule } from "@angular/material/chips";
import { SupplierService } from "@core/services/supplier.service";
import { CreateSupplierDialogComponent } from "./create-supplier-dialog/supplier-dialog.component";

@Component({
    selector: "app-admin-suppliers",
    standalone: true,
    imports: [
        CommonModule,
        MatCardModule,
        MatButtonModule,
        MatIconModule,
        MatTableModule,
        MatDialogModule,
        MatChipsModule,
    ],
    templateUrl: "./admin-suppliers.component.html",
    styleUrl: "./admin-suppliers.component.scss",
})
export class AdminSuppliersComponent implements OnInit {
    private supplierService = inject(SupplierService);
    private dialog = inject(MatDialog);

    suppliers: any[] = [];
    displayedColumns: string[] = ["id", "name", "type", "active", "actions"];

    ngOnInit(): void {
        this.loadSuppliers();
    }

    loadSuppliers(): void {
        this.supplierService.getAllSuppliers().subscribe({
            next: (suppliers) => (this.suppliers = suppliers),
            error: (err) => {
                console.error("Failed to load suppliers", err);
                this.suppliers = [];
            },
        });
    }

    openCreateSupplierDialog(): void {
        const dialogRef = this.dialog.open(CreateSupplierDialogComponent, {
            width: "500px",
            data: null,
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) this.loadSuppliers();
        });
    }

    openEditSupplierDialog(supplier: any): void {
        const dialogRef = this.dialog.open(CreateSupplierDialogComponent, {
            width: "500px",
            data: supplier,
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) this.loadSuppliers();
        });
    }

    toggleActive(supplier: any): void {
        if (supplier.active) {
            this.supplierService.deactivateSupplier(supplier.id).subscribe(() => this.loadSuppliers());
        } else {
            this.supplierService.activateSupplier(supplier.id).subscribe(() => this.loadSuppliers());
        }
    }

    deleteSupplier(id: number): void {
        if (confirm("Are you sure you want to delete this supplier?")) {
            this.supplierService.deleteSupplier(id).subscribe(() => this.loadSuppliers());
        }
    }
}
