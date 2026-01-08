import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './home/home.component';
import { SignInComponent } from './auth/sign-in/sign-in.component';
import { NavComponent } from './nav/nav.component';
import { AboutComponent } from './customer/about/about.component';
import { DetailsComponent } from './customer/about/details/details.component';
import { SignUpComponent } from './auth/sign-up/sign-up.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { NoSpaceOnlyLettersDirective } from './directives/no-space.directive';
import { OnlyNumbersDirective } from './directives/only-numbers.directive';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './auth.interceptor';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
	declarations: [
		AppComponent,
		HomeComponent,
		SignInComponent,
		NavComponent,
		AboutComponent,
		DetailsComponent,
		SignUpComponent,
		NoSpaceOnlyLettersDirective,
		OnlyNumbersDirective,
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		FormsModule,
		ReactiveFormsModule,
		BrowserAnimationsModule,
		MatIconModule,
		MatButtonModule,
		MatInputModule,
		MatSelectModule,
		MatDatepickerModule,
		MatNativeDateModule,
		MatFormFieldModule,
		HttpClientModule,
		MatDialogModule
	],
	providers: [
	{
		provide: HTTP_INTERCEPTORS,
		useClass: AuthInterceptor,
		multi: true
	}
	],
	bootstrap: [AppComponent]
})

export class AppModule { }
