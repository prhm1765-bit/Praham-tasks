import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class UserService {

	private baseUrl = 'http://localhost:8080/api/customer';

	constructor(private http: HttpClient) {}

	public registerUser(data: any): Observable<any> {
		return this.http.post(`${this.baseUrl}/sign-up`, data);
	}

	public signInUser(data: any): Observable<any> {
		return this.http.post(`${this.baseUrl}/login`, data);
	}

	public getUserById(id: number): Observable<any> {
		return this.http.get(`${this.baseUrl}/${id}`);
	}

	public updateUser(data: any): Observable<any> {
		return this.http.put(`${this.baseUrl}`, data);
	}

	public deleteUser(id: number): Observable<void> {
		return this.http.delete<void>(`${this.baseUrl}/${id}`);
	}

}
