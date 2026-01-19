import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class UserService {

	// Base API URL for customer-related endpoints. Update for production
	private baseUrl = 'http://localhost:8080/api/customer';

	constructor(private http: HttpClient) {}

	// Register a new user (sign-up)
	public registerUser(data: any): Observable<any> {
		return this.http.post(`${this.baseUrl}/sign-up`, data);
	}

	// Authenticate user and receive token
	public signInUser(data: any): Observable<any> {
		return this.http.post(`${this.baseUrl}/login`, data);
	}

	// Fetch a user's details by id
	public getUserById(id: number): Observable<any> {
		return this.http.get(`${this.baseUrl}/${id}`);
	}

	// Update user profile (expects full user payload)
	public updateUser(data: any): Observable<any> {
		return this.http.put(`${this.baseUrl}`, data);
	}

	// Permanently delete a user account
	public deleteUser(id: number): Observable<void> {
		return this.http.delete<void>(`${this.baseUrl}/${id}`);
	}

}
