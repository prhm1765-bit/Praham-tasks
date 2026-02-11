import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
	selector: 'app-sign-up',
	templateUrl: './sign-up.component.html',
	styleUrls: ['./sign-up.component.css']
})

export class SignUpComponent {

	public signUpForm!: FormGroup;
	public isEditMode = false;

	constructor(
		private fb : FormBuilder,
		private userService: UserService,
		private route: ActivatedRoute,
		private router: Router,
		private snackBar: MatSnackBar
	) {}

	ngOnInit(): void {
		this.initForm();
	}

	initForm(): void {
		const id = this.route.snapshot.paramMap.get('id');
		this.isEditMode = !!id;
		// default model (used for register)
		const model: any = {
			id: null,
			firstName: null,
			lastName: null,
			gender: null,
			dob: null,
			mobilenumber: null,
			email: null,
			password: null,
			companyCode: null, 
			address: [{ address: null, addresstype: null }]
		};

		if (id) {
			this.userService.getUserById(+id).subscribe(
				res => {
				// overwrite defaults with backend response
				Object.assign(model, res);
				this.signUpForm = this.fb.group({
					id: [model.id],
					firstName: [model.firstName, [Validators.required]],
					lastName: [model.lastName, [Validators.required]],
					gender: [model.gender, [Validators.required]],
					dob: [model.dob, [Validators.required, this.pastDateValidator()]],
						mobilenumber: [model.mobilenumber, [Validators.required, Validators.pattern('^[6-9]\\d{9}$')]],
					email: [model.email, [Validators.required, Validators.email]],
					password: [''], // never prefill
					address: this.fb.array(
						model.address.map((a: any) =>
							this.fb.group({
								address: [a.address, [Validators.required, Validators.minLength(5)]],
								addresstype: [a.addresstype, [Validators.required]]
							})
						)
					)
				});
			});
		} else {
			// register → blank form
			this.signUpForm = this.fb.group({
				id: [model.id],
				firstName: [model.firstName, [Validators.required]],
				lastName: [model.lastName, [Validators.required]],
				gender: [model.gender, [Validators.required]],
				dob: [model.dob, [Validators.required, this.pastDateValidator()]],
					mobilenumber: [model.mobilenumber, [Validators.required, Validators.pattern('^[6-9]\\d{9}$')]],
				email: [model.email, [Validators.required, Validators.email]],
				companyCode: [model.companyCode, [Validators.required]],
				password: ['', [Validators.required, Validators.minLength(8)]],
				address: this.fb.array(
					model.address.map((a: any) =>
						this.fb.group({
							address: [a.address, [Validators.required, Validators.minLength(5)]],
							addresstype: [a.addresstype, [Validators.required]]
						})
					)
				)
			});
		}
	}

	private pastDateValidator(): ValidatorFn {
		return (control: AbstractControl): ValidationErrors | null => {
			const val = control.value;
			if (!val) return null; // required validator handles empties

			const d = val instanceof Date ? val : new Date(val);
			if (isNaN(d.getTime())) {
				return { invalidDate: true };
			}

			// compare date parts only (ignore time)
			const inputDate = new Date(d.getFullYear(), d.getMonth(), d.getDate()).getTime();
			const today = new Date();
			today.setHours(0, 0, 0, 0);
			if (inputDate >= today.getTime()) {
				return { futureDate: true };
			}
			return null;
		};
	}

	private createAddressGroup(): FormGroup {
		return this.fb.group({
		address: ['', [Validators.required, Validators.minLength(5)]],
		addresstype: ['HOME', Validators.required]
		});
	}

	public get addresses(): FormArray {
		return this.signUpForm.get('address') as FormArray;
	}

	public addAddress(): void {
		this.addresses.push(this.createAddressGroup());
	}

	public removeAddress(index: number): void {
		if (this.addresses.length > 1) {
			this.addresses.removeAt(index);
		}
	}

	public submit(): void {
		if (this.signUpForm.invalid) {
			this.signUpForm.markAllAsTouched();
			return;
		}

		const payload = this.signUpForm.getRawValue(); 

		// Ensure dob is formatted as yyyy-mm-dd for backend
		if (payload.dob) {
			const d = payload.dob instanceof Date ? payload.dob : new Date(payload.dob);
			if (!isNaN(d.getTime())) {
				const yyyy = d.getFullYear();
				const mm = String(d.getMonth() + 1).padStart(2, '0');
				const dd = String(d.getDate()).padStart(2, '0');
				payload.dob = `${yyyy}-${mm}-${dd}`;
			}
		}

		if (payload.id) {
			this.userService.updateUser(payload).subscribe({
				next: (res) => {
					if (res.reLoginRequired) {
					this.snackBar.open('Email changed. Please log in again.', '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });

					localStorage.removeItem('token');

					this.router.navigate(['/sign-in']);
					return;
			}
					console.log('User Updated', res);
						this.snackBar.open('Update successful!', '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });
					this.router.navigate(['/details', payload.id]);
				},
				error: (err) => {
					const msg = err?.error?.errors?.message || 'Update failed';
						this.snackBar.open(msg, '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });
				}
		});
		} else {
			this.userService.registerUser(payload).subscribe({
				next: (res) => {
					console.log('User Registered', res);
					this.snackBar.open('Registration successful!', '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });
					this.router.navigate(['/sign-in']);
				},
			error: (err) => {
  const backendErrors = err?.error?.errors;

  if (backendErrors) {
    Object.keys(backendErrors).forEach(field => {
      const control = this.signUpForm.get(field);
      if (control) {
        control.setErrors({ backend: backendErrors[field] });
      }
    });
    return;
  }

					this.snackBar.open('Registration failed', '', { duration: 3000, panelClass: 'snackbar-success', verticalPosition: 'top', horizontalPosition: 'center' });
}

			});
		}
	}

}
