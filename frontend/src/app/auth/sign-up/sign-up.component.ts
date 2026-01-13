import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
	selector: 'app-sign-up',
	templateUrl: './sign-up.component.html',
	styleUrls: ['./sign-up.component.css']
})

export class SignUpComponent {

	signUpForm!: FormGroup;
	isEditMode = false;
	customerId!: number;

	constructor(
		private fb : FormBuilder,
		private userService: UserService,
		private route: ActivatedRoute,
		private router: Router
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
					dob: [model.dob, [Validators.required]],
					mobilenumber: [model.mobilenumber, [Validators.required]],
					email: [model.email, [Validators.required, Validators.email]],
					password: [''], // never prefill
					address: this.fb.array(
						model.address.map((a: any) =>
							this.fb.group({
							address: [a.address, [Validators.required]],
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
				dob: [model.dob, [Validators.required]],
				mobilenumber: [model.mobilenumber, [Validators.required]],
				email: [model.email, [Validators.required, Validators.email]],
				password: [''],
				address: this.fb.array(
					model.address.map((a: any) =>
						this.fb.group({
							address: [a.address, [Validators.required]],
							addresstype: [a.addresstype, [Validators.required]]
						})
					)
				)
			});
		}
	}

	createAddressGroup(): FormGroup {
		return this.fb.group({
		address: ['', [Validators.required, Validators.minLength(5)]],
		addresstype: ['home', Validators.required]
		});
	}

	get addresses(): FormArray {
		return this.signUpForm.get('address') as FormArray;
	}

	addAddress(): void {
		this.addresses.push(this.createAddressGroup());
	}

	removeAddress(index: number): void {
		if (this.addresses.length > 1) {
		this.addresses.removeAt(index);
		}
	}

	submit(): void {
		if (this.signUpForm.invalid) {
			this.signUpForm.markAllAsTouched();
			return;
		}

		const payload = this.signUpForm.getRawValue(); 

		if (payload.id) {
			this.userService.updateUser(payload).subscribe({
				next: (res) => {
					console.log('User Updated', res);
					alert('Update successful!');
					this.router.navigate(['/details', payload.id]);
				},
				error: (err) => {
					const msg = err?.error?.errors?.message || 'Update failed';
					alert(msg); 
				}
		});
		} else {
			this.userService.registerUser(payload).subscribe({
				next: (res) => {
					console.log('User Registered', res);
					alert('Registration successful!');
					this.router.navigate(['/sign-in']);
				},
				error: (err) => {
					const backendErrors = err?.error?.errors;

  // EMAIL ALREADY EXISTS
  if (backendErrors?.email) {
    this.signUpForm
      .get('email')
      ?.setErrors({ backend: backendErrors.email });
    return;
  }

  // fallback
  alert('Registration failed');
				}
			});
		}
	}

}
