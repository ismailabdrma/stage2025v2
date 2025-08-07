import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/api/payments`;

  processPayment(paymentData: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, paymentData);
  }
}