import { Component, OnInit } from '@angular/core';
import { AdminUserService } from '../../services/admin-customer.service';

@Component({
	selector: 'app-customer-list',
	templateUrl: './customer-list.component.html',
	styleUrls: ['./customer-list.component.css']
})

export class CustomerListComponent implements OnInit {

	customers: any[] = [];
	loading = false;
	showPopup = false;
	showYes = false;
	popupTitle = '';
	popupMessage = '';
	deleteId!: number;

	constructor(private adminUserService: AdminUserService) {}

	ngOnInit(): void {
		this.loadUsers();
	}

	loadUsers(): void {
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

	logout() {
		localStorage.removeItem('token'); 
		window.location.href = '/sign-in'; 
	}

	deleteCustomer(id: number): void {
		this.deleteId = id;
		this.popupTitle = 'Confirm Delete';
		this.popupMessage = 'Are you sure you want to delete this user?';
		this.showYes = true;
		this.showPopup = true;
	}

	confirmDelete() {
		this.showPopup = false;
		this.adminUserService.deleteCustomer(this.deleteId).subscribe({
			next: () => {
				this.loadUsers();
				this.popupTitle = 'Success';
				this.popupMessage = 'User deleted successfully.';
				this.showYes = false;
				this.showPopup = true;
			},
			error: (err) => {
				this.popupTitle = 'Error';
				this.popupMessage = err?.error?.errors?.message ||
				err?.error?.message || 'Admin cannot be deleted.';
				this.showYes = false;
				this.showPopup = true;
			}
		});
	}

}