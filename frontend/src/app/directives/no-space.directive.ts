import { Directive, HostListener, Optional, Self } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
	selector: '[noSpaceOnlyLetters]'
})

export class NoSpaceOnlyLettersDirective {

	constructor(@Optional() @Self() private control: NgControl) {}

	// ---------- KEYBOARD INPUT ----------
	@HostListener('keydown', ['$event'])
	public onKeyDown(event: KeyboardEvent) {
		const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
		if (allowedKeys.includes(event.key)) {
			return;
		}
		if (event.key === ' ') {
			event.preventDefault();
			return;
		}
		// Only allow letters, numbers, apostrophe and hyphen
		if (!/^[a-zA-Z0-9'-]$/.test(event.key)) {
			event.preventDefault();
		}
	}

	// ---------- PASTE HANDLING ----------
	@HostListener('paste', ['$event'])
	public onPaste(event: ClipboardEvent) {
		const pastedText = event.clipboardData?.getData('text') || '';
		if (!/^[a-zA-Z0-9'-]+$/.test(pastedText)) {
			event.preventDefault();
		}
	}

	// Mark control as touched when it loses focus so validation can run
	@HostListener('blur')
	public onBlur() {
		this.control?.control?.markAsTouched();
	}

}
