import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignInComponent } from './auth/sign-in/sign-in.component';
import { AboutComponent } from './customer/about/about.component';
import { DetailsComponent } from './customer/about/details/details.component';
import { SignUpComponent } from './auth/sign-up/sign-up.component';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './auth.guard';

const routes: Routes = [
	{path: '', component: HomeComponent},
	{path: 'sign-in', component: SignInComponent},
	{path: 'about', component: AboutComponent,  canActivate: [AuthGuard]},
	{path: 'details/:id', component: DetailsComponent,  canActivate: [AuthGuard]},
	{ path: 'edit/:id', component: SignUpComponent,  canActivate: [AuthGuard] },
	{path: 'sign-up', component: SignUpComponent},
	{path: 'admin',
		loadChildren: () =>
		import('./admin/admin.module').then(m => m.AdminModule),  canActivate: [AuthGuard],  data: { role: 'ADMIN' }
	}
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
	})
	
export class AppRoutingModule { }
