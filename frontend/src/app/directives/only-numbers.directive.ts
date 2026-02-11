import { Directive, HostListener, Optional, Self } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
	selector: '[onlyNumbers]'
})
export class OnlyNumbersDirective {

	constructor(@Optional() @Self() private control: NgControl) {}

	// Allow only numeric keys and basic navigation/editing keys
	@HostListener('keydown', ['$event'])
	public onKeyDown(event: KeyboardEvent) {
		const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
		if (allowedKeys.includes(event.key)) {
			return;
		}
		if (!/^[0-9]$/.test(event.key)) {
			event.preventDefault();
		}
	}

	// On input (including paste), strip non-numeric characters and
	// synchronize the underlying form control value.
	@HostListener('input', ['$event'])
	public onInput(event: Event) {
		const input = event.target as HTMLInputElement;
		const cleanValue = input.value.replace(/[^0-9]/g, '');
		if (input.value !== cleanValue) {
			input.value = cleanValue;
			// Keep form control in sync without emitting extra value events
			this.control?.control?.setValue(cleanValue, { emitEvent: false });
		}
	}

}
