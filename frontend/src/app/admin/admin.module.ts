import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerListComponent } from './pages/customer-list/customer-list.component';
import { AdminRoutingModule } from './admin-routing.module';
import { PopupComponent } from '../shared/popup/popup.component';
import { MatDialogModule } from "@angular/material/dialog";

@NgModule({
	declarations: [
		CustomerListComponent,
		PopupComponent
	],
	imports: [
		CommonModule,
		AdminRoutingModule,
		MatDialogModule
	]
})

export class AdminModule { }
