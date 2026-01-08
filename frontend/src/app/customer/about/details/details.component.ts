import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'app-details',
	templateUrl: './details.component.html',
	styleUrls: ['./details.component.css'],
})

export class DetailsComponent {

	customer: any;

	constructor(
		private route: ActivatedRoute,
		private userService: UserService
	) {}

	ngOnInit(): void {
		const id = Number(this.route.snapshot.paramMap.get('id'));
		this.userService.getUserById(id).subscribe({
			next: (res) => {
				this.customer = res;
				console.log('Customer details:', JSON.stringify(this.customer) + "with id " + id);
			},
			error: (err) => {
				const msg = err?.error?.errors?.message || 'Customer not found';
				alert(msg); 
			}
		});
	}

}
