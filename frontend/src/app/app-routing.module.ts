import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { ShopWindowComponent } from './features/shop-window/shop-window.component';
import { PageNotFoundComponent } from './features/page-not-found/page-not-found.component';
import { ForgotMyPasswordComponent } from './features/forgot-my-password/forgot-my-password.component';
import { CartComponent } from './features/cart/cart.component';
import { RegisterComponent } from './features/register/register.component';
import { RedefinePasswordComponent } from './features/redefine-password/redefine-password.component';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'shop-window', component: ShopWindowComponent },
  { path: 'cart', component: CartComponent },
  { path: 'forgot-password', component: ForgotMyPasswordComponent },
  { path: 'shop-window/:id', component: ShopWindowComponent },
  { path: 'redefine-password/:id', component: RedefinePasswordComponent },
  { 
    path: '**',
    pathMatch: 'full', 
    component: PageNotFoundComponent 
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
