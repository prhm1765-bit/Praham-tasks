import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
	selector: 'app-nav',
	templateUrl: './nav.component.html',
	styleUrls: ["./nav.component.css"]
})

export class NavComponent {
	public isNavOpen = false;

	constructor(private router: Router, private userService: UserService) {}

	public toggleNav() {
		this.isNavOpen = !this.isNavOpen;
	}

	public goToDetails() {
		const token = localStorage.getItem('token');
		if (!token) {
			this.router.navigate(['/sign-in']);
			return;
		}
		const payload = JSON.parse(atob(token.split('.')[1]));
		const id = payload.id;
		this.router.navigate(['/details', id]);
	}

	public logout() {
		localStorage.removeItem('token'); 
		window.location.href = '/sign-in'; 
	}

	public deleteAccount() {
		if (!confirm('Are you sure you want to permanently delete your account?')) {
		return;
		}

		const token = localStorage.getItem('token');
		if (!token) return;

		const payload = JSON.parse(atob(token.split('.')[1]));
		const id = payload.id;

		this.userService.deleteUser(id).subscribe({
			next: () => {
				alert('Your account has been deleted.');
				localStorage.removeItem('token');
				window.location.href = '/sign-in';
			},
			error: (err) => {
				const msg = err?.error?.errors?.message || 'Delete failed';
				alert(msg);
			}
		});
	}

}
