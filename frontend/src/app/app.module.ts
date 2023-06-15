import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './features/login/login.component';
import { ShopWindowComponent } from './features/shop-window/shop-window.component';
import { PageNotFoundComponent } from './features/page-not-found/page-not-found.component';
import { FoodItemComponent } from './components/food-item/food-item.component';
import { ForgotMyPasswordComponent } from './features/forgot-my-password/forgot-my-password.component';
import { CartComponent } from './features/cart/cart.component';
import { UseInputValueOnEnterDirective } from './directives/use-input-value-on-enter.directive';
import { RegisterComponent } from './features/register/register.component';
import { BaseUrlHttpInterceptor } from './interceptors/BaseUrlHttpInterceptor';
import { RedefinePasswordComponent } from './features/redefine-password/redefine-password.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ShopWindowComponent,
    PageNotFoundComponent,
    FoodItemComponent,
    ForgotMyPasswordComponent,
    CartComponent,
    UseInputValueOnEnterDirective,
    RegisterComponent,
    RedefinePasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: BaseUrlHttpInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}