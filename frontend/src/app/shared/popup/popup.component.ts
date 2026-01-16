import { Component, EventEmitter, Inject, Input, Optional, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

interface PopupData {
	title: string;
	message: string;
	showYes?: boolean;
}

@Component({
	selector: 'app-popup',
	templateUrl: './popup.component.html',
	styleUrls: ['./popup.component.css']
})
export class PopupComponent {

	// Inputs for template-driven usage; when opened via MatDialog the
	// same values may be provided via `MAT_DIALOG_DATA` instead.
	@Input() public title = '';
	@Input() public message = '';
	@Input() public showYes = false;

	// Outputs used when the component is embedded rather than dialog-opened
	@Output() public yes = new EventEmitter<void>();
	@Output() public close = new EventEmitter<void>();

	constructor(
		@Optional() private dialogRef: MatDialogRef<PopupComponent>,
		@Optional() @Inject(MAT_DIALOG_DATA) public data?: PopupData
	) { if (this.data) {
		// When opened as a MatDialog, populate inputs from injected data
		this.title = this.data.title;
		this.message = this.data.message;
		this.showYes = !!this.data.showYes;
	}}

	// Confirm/Yes handler: close dialog with true or emit event for embedded use
	public onYes() {
		if (this.dialogRef) {
			this.dialogRef.close(true); 
		} else {
			this.yes.emit(); 
		}
	}

	// Close handler: close dialog with false or emit close event
	public onClose() {
		if (this.dialogRef) {
			this.dialogRef.close(false); 
		} else {
			this.close.emit(); 
		}
	}

}