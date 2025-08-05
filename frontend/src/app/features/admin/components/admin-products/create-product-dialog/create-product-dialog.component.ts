import {Component, Inject, inject} from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormBuilder, type FormGroup, Validators, ReactiveFormsModule } from "@angular/forms"
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog"
import { MatDialogModule } from "@angular/material/dialog"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatSelectModule } from "@angular/material/select"
import { MatButtonModule } from "@angular/material/button"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { ProductService } from "@core/services/product.service"
import type { Product, Category } from "@core/models/product.model"

@Component({
    selector: "app-create-product-dialog",
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatButtonModule,
        MatProgressSpinnerModule,
    ],
    template: `
        <h2 mat-dialog-title>{{ data ? 'Edit Product' : 'Create Product' }}</h2>

        <mat-dialog-content>
            <form [formGroup]="productForm">
                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Product Name</mat-label>
                    <input matInput formControlName="name" required>
                    <mat-error *ngIf="productForm.get('name')?.hasError('required')">
                        Product name is required
                    </mat-error>
                </mat-form-field>

                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Description</mat-label>
                    <textarea matInput formControlName="description" rows="3" required></textarea>
                    <mat-error *ngIf="productForm.get('description')?.hasError('required')">
                        Description is required
                    </mat-error>
                </mat-form-field>

                <div class="form-row">
                    <mat-form-field appearance="outline" class="half-width">
                        <mat-label>Price</mat-label>
                        <input matInput type="number" formControlName="displayedPrice" min="0" step="0.01" required>
                        <mat-error *ngIf="productForm.get('displayedPrice')?.hasError('required')">
                            Price is required
                        </mat-error>
                    </mat-form-field>

                    <mat-form-field appearance="outline" class="half-width">
                        <mat-label>Stock</mat-label>
                        <input matInput type="number" formControlName="syncedStock" min="0" required>
                        <mat-error *ngIf="productForm.get('syncedStock')?.hasError('required')">
                            Stock is required
                        </mat-error>
                    </mat-form-field>
                </div>

                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Category</mat-label>
                    <mat-select formControlName="categoryName" required>
                        <mat-option *ngFor="let category of categories" [value]="category.name">
                            {{ category.name }}
                        </mat-option>
                    </mat-select>
                    <mat-error *ngIf="productForm.get('categoryName')?.hasError('required')">
                        Category is required
                    </mat-error>
                </mat-form-field>

                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Supplier</mat-label>
                    <input matInput formControlName="supplierName" required>
                    <mat-error *ngIf="productForm.get('supplierName')?.hasError('required')">
                        Supplier is required
                    </mat-error>
                </mat-form-field>

                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Image URLs (comma separated)</mat-label>
                    <textarea matInput formControlName="imageUrls" rows="2" placeholder="https://example.com/image1.jpg, https://example.com/image2.jpg"></textarea>
                </mat-form-field>
            </form>
        </mat-dialog-content>

        <mat-dialog-actions align="end">
            <button mat-button (click)="onCancel()">Cancel</button>
            <button mat-raised-button color="primary"
                    (click)="onSave()"
                    [disabled]="productForm.invalid || loading">
                <mat-spinner diameter="20" *ngIf="loading"></mat-spinner>
                <span *ngIf="!loading">{{ data ? 'Update' : 'Create' }}</span>
            </button>
        </mat-dialog-actions>
    `,
    styles: [
        `
          .full-width {
            width: 100%;
            margin-bottom: 16px;
          }

          .form-row {
            display: flex;
            gap: 16px;
          }

          .half-width {
            flex: 1;
            margin-bottom: 16px;
          }

          mat-dialog-content {
            min-width: 400px;
            max-height: 70vh;
            overflow-y: auto;
          }

          mat-spinner {
            margin-right: 8px;
          }

          @media (max-width: 768px) {
            .form-row {
              flex-direction: column;
            }

            mat-dialog-content {
              min-width: 300px;
            }
          }
        `,
    ],
})
export class CreateProductDialogComponent {
    private fb = inject(FormBuilder)
    private productService = inject(ProductService)
    private dialogRef = inject(MatDialogRef<CreateProductDialogComponent>)

    productForm: FormGroup
    categories: Category[] = []
    loading = false

    // Fixed constructor with proper injection
    constructor(@Inject(MAT_DIALOG_DATA) public data: Product | null) {
        this.productForm = this.fb.group({
            name: [this.data?.name || "", Validators.required],
            description: [this.data?.description || "", Validators.required],
            displayedPrice: [this.data?.displayedPrice || 0, [Validators.required, Validators.min(0)]],
            syncedStock: [this.data?.syncedStock || 0, [Validators.required, Validators.min(0)]],
            categoryName: [this.data?.categoryName || "", Validators.required],
            supplierName: [this.data?.supplierName || "", Validators.required],
            imageUrls: [this.data?.imageUrls?.join(", ") || ""],
        })

        this.loadCategories()
    }

    loadCategories(): void {
        this.productService.getAllCategories().subscribe({
            next: (categories) => {
                this.categories = categories
            },
            error: (error) => {
                console.error("Error loading categories:", error)
            },
        })
    }

    onSave(): void {
        if (this.productForm.valid) {
            this.loading = true
            const formValue = this.productForm.value

            // Convert comma-separated image URLs to array
            const imageUrls = formValue.imageUrls
                ? formValue.imageUrls
                    .split(",")
                    .map((url: string) => url.trim())
                    .filter((url: string) => url)
                : []

            const productData = {
                ...formValue,
                imageUrls,
            }

            const request = this.data
                ? this.productService.updateProduct(this.data.id, productData)
                : this.productService.createProduct(productData)

            request.subscribe({
                next: (product) => {
                    this.loading = false
                    this.dialogRef.close(product)
                },
                error: (error) => {
                    console.error("Error saving product:", error)
                    this.loading = false
                },
            })
        }
    }

    onCancel(): void {
        this.dialogRef.close()
    }
}
