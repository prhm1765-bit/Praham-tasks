import { Injectable } from '@angular/core';
import {  ActivatedRouteSnapshot, Router } from '@angular/router';

@Injectable({
	providedIn: 'root'
})

export class AuthGuard  {

	constructor(private router: Router) {}

	/**
	 * Guard that checks for a valid JWT token in `localStorage` and
	 * enforces simple role-based routing rules.
	 *
	 * Assumptions:
	 * - Token is a JWT with base64-encoded payload at index 1
	 * - Payload contains `role` and `id` fields used elsewhere
	 */
	public canActivate(route: ActivatedRouteSnapshot): boolean {
		const token = localStorage.getItem('token');

		// Not authenticated -> redirect to sign-in
		if (!token) {
			this.router.navigate(['/sign-in']);
			return false;
		}

		// Decode payload (lightweight, no signature verification here)
		const payload = JSON.parse(atob(token.split('.')[1]));
		const role = payload.role;

		const requiredRole = route.data['role'];

		//  ADMIN-only routes
		if (requiredRole && role !== requiredRole) {
			this.router.navigate(['/']);
			return false;
		}

		// Example behavior: prevent ADMIN users from accessing customer pages
		if (!requiredRole && role === 'ADMIN') {
			this.router.navigate(['/admin/customers']);
			return false;
		}

		// All checks passed
		return true;
	}

}
