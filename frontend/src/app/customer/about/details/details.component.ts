import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
	selector: 'app-details',
	templateUrl: './details.component.html',
	styleUrls: ['./details.component.css'],
})

export class DetailsComponent {

	public customer: any;

	constructor(
		private route: ActivatedRoute,
		private userService: UserService,
		private snackBar: MatSnackBar
	) {}

	public ngOnInit(): void {
		const id = Number(this.route.snapshot.paramMap.get('id'));
		this.userService.getUserById(id).subscribe({
			next: (res) => {
				this.customer = res;
				console.log('Customer details:', JSON.stringify(this.customer) + "with id " + id);
			},
			error: (err) => {
				const msg = err?.error?.errors?.message || 'Customer not found';
				this.snackBar.open(msg, '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });
			}
		});
	}

}
