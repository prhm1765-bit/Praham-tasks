import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})

export class AdminUserService {

	private baseUrl = 'http://localhost:8080/api/customer';

	constructor(private http: HttpClient) {}

	getAllUsers(): Observable<any[]> {
		return this.http.get<any[]>(this.baseUrl);
	}

	deleteCustomer(id: number): Observable<void> {
		return this.http.delete<void>(`${this.baseUrl}/${id}`);
	}

}
