import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
	selector: 'app-nav',
	templateUrl: './nav.component.html',
	styleUrls: ["./nav.component.css"]
})

export class NavComponent {
	// Tracks whether the mobile/side nav is open
	public isNavOpen = false;

	constructor(private router: Router, private userService: UserService, private snackBar: MatSnackBar) {}

	// Toggle nav open/closed state used by template
	public toggleNav() {
		this.isNavOpen = !this.isNavOpen;
	}

	// Navigate to the authenticated user's details page
	public goToDetails() {
		const token = localStorage.getItem('token');
		if (!token) {
			this.router.navigate(['/sign-in']);
			return;
		}
		const payload = JSON.parse(atob(token.split('.')[1]));
		const id = payload.customerId;
		this.router.navigate(['/details', id]);
	}

	// Simple logout: remove token and redirect to sign-in
	public logout() {
		localStorage.removeItem('token'); 
		window.location.href = '/sign-in'; 
	}

	// Delete the current user's account via UserService and handle response
	public deleteAccount() {
		const token = localStorage.getItem('token');
		if (!token) return;

		// Show a persistent snackbar with a Confirm action instead of a native confirm dialog
		const confirmSnack = this.snackBar.open('Are you sure you want to permanently delete your account?', 'Confirm', { panelClass: 'snackbar-warn', verticalPosition: 'top', horizontalPosition: 'center' });

		// When the user clicks the Confirm action, proceed with deletion
		const actionSub = confirmSnack.onAction().subscribe(() => {
			const payload = JSON.parse(atob(token.split('.')[1]));
			const id = payload.customerId;

			this.userService.deleteUser(id).subscribe({
				next: () => {
					this.snackBar.open('Your account has been deleted.', '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });
					localStorage.removeItem('token');
					window.location.href = '/sign-in';
				},
				error: (err) => {
					const msg = err?.error?.errors?.message || 'Delete failed';
					this.snackBar.open(msg, '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });
				}
			});
		});

		// Clean up subscription when the snackbar is dismissed without action
		confirmSnack.afterDismissed().subscribe(() => actionSub.unsubscribe());
	}

}
