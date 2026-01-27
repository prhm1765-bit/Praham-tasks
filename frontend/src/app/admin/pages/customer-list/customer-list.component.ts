import { Component, OnInit } from '@angular/core';
import { AdminUserService } from '../../services/admin-customer.service';

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

	constructor(private adminUserService: AdminUserService) {}

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
		this.adminUserService.allUserReports().subscribe({
			next: (blob) => {
				const url = window.URL.createObjectURL(blob);
				const a = document.createElement('a');
				a.href = url;
				a.download = 'all-customers.pdf';
				a.click();
				window.URL.revokeObjectURL(url);
			},
			error: () => {
				alert('Failed to download report');
			}
		});
	}

	public logout() {
		localStorage.removeItem('token'); 
		window.location.href = '/sign-in'; 
	}

}