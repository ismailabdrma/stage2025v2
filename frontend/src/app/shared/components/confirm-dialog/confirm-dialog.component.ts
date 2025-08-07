import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button'; // Import MatButtonModule
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog'; // Import MatDialogModule, MatDialogRef, and MAT_DIALOG_DATA

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule, // Add MatButtonModule to imports
    MatDialogModule, // Add MatDialogModule to imports
  ],
  template: `
    <h1 mat-dialog-title>{{ data.title }}</h1>
    <div mat-dialog-content>
      <p>{{ data.message }}</p>
    </div>
    <div mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button color="primary" (click)="onConfirm()" cdkFocusInitial>Confirm</button>
    </div>
  `,
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Inject MAT_DIALOG_DATA and make it public
  ) {}

  onConfirm(): void {
    this.dialogRef.close(true); // Close dialog with true
  }

  onCancel(): void {
    this.dialogRef.close(false); // Close dialog with false
  }
}