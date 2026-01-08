import { Directive, HostListener, Optional, Self } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
	selector: '[noSpaceOnlyLetters]'
})

export class NoSpaceOnlyLettersDirective {

	constructor(@Optional() @Self() private control: NgControl) {}

	// ---------- KEYBOARD INPUT ----------
	@HostListener('keydown', ['$event'])
	onKeyDown(event: KeyboardEvent) {
		const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
		if (allowedKeys.includes(event.key)) {
			return;
		}
		if (event.key === ' ') {
		event.preventDefault();
			return;
		}
		if (!/^[a-zA-Z0-9'-]$/.test(event.key)) {
			event.preventDefault();
		}
	}

	// ---------- PASTE HANDLING ----------
	@HostListener('paste', ['$event'])
	onPaste(event: ClipboardEvent) {
		const pastedText = event.clipboardData?.getData('text') || '';
		if (!/^[a-zA-Z0-9'-]+$/.test(pastedText)) {
			event.preventDefault();
		}
	}

	// ---------- MARK TOUCHED ----------
	@HostListener('blur')
	onBlur() {
		this.control?.control?.markAsTouched();
	}

}
