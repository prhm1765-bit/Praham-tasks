import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerListComponent } from './pages/customer-list/customer-list.component';
import { AuthGuard } from '../auth.guard';

const routes: Routes = [
{
	path: 'customers', component: CustomerListComponent, canActivate: [AuthGuard]
}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})

export class AdminRoutingModule {}

