import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { ActivatedRoute, Router } from "@angular/router";
import { UserService } from "src/app/services/user.service";
import { PopupComponent } from "src/app/shared/popup/popup.component";

@Component({
	selector: "app-sign-in",
	templateUrl: "./sign-in.component.html",
	styleUrls: ["./sign-in.component.css"]
})

	export class SignInComponent implements OnInit {

	public signInForm!: FormGroup;
	constructor(
		private fb : FormBuilder,
		private userService: UserService,
		private router: Router,
		private dialog: MatDialog
	) {}

	public ngOnInit(): void {
		this.signInForm = this.fb.group({
			email: ['', [Validators.required, Validators.email]],  
			password: ['', [Validators.required, Validators.minLength(6)]]
		});
	}

	public submit() {
		if (this.signInForm.invalid) return;

		this.userService.signInUser(this.signInForm.value).subscribe({
			next: (res: any) => {
				localStorage.setItem('token', res.token);

				const payload = JSON.parse(atob(res.token.split('.')[1]));
				const role = payload.role;
				const id = payload.id;

				this.dialog.open(PopupComponent, {
					width: '350px',
					data: {
						title: 'Success',
						message: 'Sign in successful!'
					}
				}).afterClosed().subscribe(() => {
					if (role === 'ADMIN') {
						this.router.navigate(['/admin/customers']);
					} else {
						this.router.navigate(['/details', id]);
					}
				});
			},
			error: (err) => {
				const msg = err?.error?.errors?.message || 'Login failed';

				this.dialog.open(PopupComponent, {
					width: '350px',
					data: {
						title: 'Error',
						message: msg
					}
				});
			}
		});
	}

}