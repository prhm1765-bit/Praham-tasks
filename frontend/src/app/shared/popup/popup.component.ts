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

	@Input() public title = '';
	@Input() public message = '';
	@Input() public showYes = false;

	@Output() public yes = new EventEmitter<void>();
	@Output() public close = new EventEmitter<void>();

	constructor(
		@Optional() private dialogRef: MatDialogRef<PopupComponent>,
		@Optional() @Inject(MAT_DIALOG_DATA) public data?: PopupData
	) { if (this.data) {
		this.title = this.data.title;
		this.message = this.data.message;
		this.showYes = !!this.data.showYes;
	}}

	public onYes() {
		if (this.dialogRef) {
			this.dialogRef.close(true); 
		} else {
			this.yes.emit(); 
		}
	}

	public onClose() {
		if (this.dialogRef) {
			this.dialogRef.close(false); 
		} else {
			this.close.emit(); 
		}
	}

}