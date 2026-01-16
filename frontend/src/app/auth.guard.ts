import { Injectable } from '@angular/core';
import {  ActivatedRouteSnapshot, Router } from '@angular/router';

@Injectable({
	providedIn: 'root'
})

export class AuthGuard  {

	constructor(private router: Router) {}

	public canActivate(route: ActivatedRouteSnapshot): boolean {
		const token = localStorage.getItem('token');

		if (!token) {
			this.router.navigate(['/sign-in']);
			return false;
		}

		const payload = JSON.parse(atob(token.split('.')[1]));
		const role = payload.role;

		const requiredRole = route.data['role'];

		//  ADMIN-only routes
		if (requiredRole && role !== requiredRole) {
			this.router.navigate(['/']);
			return false;
		}

		//  CHANGE: block ADMIN from customer pages
		if (!requiredRole && role === 'ADMIN') {
			this.router.navigate(['/admin/customers']);
			return false;
		}

		return true;
	}

}
