import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

	/**
	 * Intercepts outgoing HTTP requests and attaches the `Authorization`
	 * header with a Bearer token when a token exists in localStorage.
	 * This keeps auth handling centralized for all HttpClient calls.
	 */
	public intercept(
		req: HttpRequest<any>,
		next: HttpHandler
	): Observable<HttpEvent<any>> {

		const token = localStorage.getItem('token');

		// If token exists, attach it as an Authorization header
		if (token) {
			const authReq = req.clone({
				setHeaders: {
					Authorization: `Bearer ${token}`
				}
			});
			return next.handle(authReq);
		}

		// No token: forward request unchanged
		return next.handle(req);
	}

}
