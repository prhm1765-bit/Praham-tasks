import { Directive, HostListener, Optional, Self } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
	selector: '[onlyNumbers]'
})
export class OnlyNumbersDirective {

	constructor(@Optional() @Self() private control: NgControl) {}

	@HostListener('keydown', ['$event'])
	onKeyDown(event: KeyboardEvent) {
		const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
		if (allowedKeys.includes(event.key)) {
			return;
		}
		if (!/^[0-9]$/.test(event.key)) {
			event.preventDefault();
		}
	}

	@HostListener('input', ['$event'])
	onInput(event: Event) {
		const input = event.target as HTMLInputElement;
		const cleanValue = input.value.replace(/[^0-9]/g, '');
		if (input.value !== cleanValue) {
			input.value = cleanValue;
			// IMPORTANT: update form control value AND error
			this.control?.control?.setValue(cleanValue, { emitEvent: false });
		}
	}

}
