import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})

export class AdminUserService {

	private baseUrl = 'http://localhost:8080/api/customer';

	constructor(private http: HttpClient) {}

	public getAllUsers(): Observable<any[]> {
		return this.http.get<any[]>(this.baseUrl);
	}

	public allUserReports(): Observable<Blob> {
		return this.http.get(`http://localhost:8080/api/reports/customer-data`, { responseType: 'blob' });
	}

}
