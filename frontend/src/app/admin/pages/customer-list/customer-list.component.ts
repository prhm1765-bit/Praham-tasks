import { Component, OnInit } from '@angular/core';
import { AdminUserService } from '../../services/admin-customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
	selector: 'app-customer-list',
	templateUrl: './customer-list.component.html',
	styleUrls: ['./customer-list.component.css']
})

export class CustomerListComponent implements OnInit {

	public customers: any[] = [];
	public loading = false;
	public showPopup = false;
	public showYes = false;
	public popupTitle = '';
	public popupMessage = '';
	public selectedLang: string = 'en';

	constructor(private adminUserService: AdminUserService, private snackBar: MatSnackBar) {}

	public ngOnInit(): void {
		this.loadUsers();
	}

	public loadUsers(): void {
		this.loading = true;
		this.adminUserService.getAllUsers().subscribe({
			next: (data) => {
				this.customers = data;
				this.loading = false;
			},
			error: () => {
				this.loading = false;
			}
		});
	}

	public generateReport(): void {
		this.adminUserService.allUserReports(this.selectedLang).subscribe({
			next: (blob) => {
				const url = window.URL.createObjectURL(blob);
				const a = document.createElement('a');
				a.href = url;
				a.download = 'all-customers.pdf';
				a.click();
				window.URL.revokeObjectURL(url);
			},
			error: () => {
				this.snackBar.open('Failed to download report', '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });
			}
		});
	}

	public generateAddressReport(): void {
		this.adminUserService.allUserAddressReports(this.selectedLang).subscribe({
			next: (blob) => {
				const url = window.URL.createObjectURL(blob);
				const a = document.createElement('a');
				a.href = url;
				a.download = 'all-customers-Address.pdf';
				a.click();
				window.URL.revokeObjectURL(url);
			},
			error: () => {
				this.snackBar.open('Failed to download report', '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });
			}
		});
	}

	public logout() {
		localStorage.removeItem('token'); 
		window.location.href = '/sign-in'; 
	}

}