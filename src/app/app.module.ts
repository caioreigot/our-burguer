import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './features/login/login.component';
import { ShopWindowComponent } from './features/shop-window/shop-window.component';
import { PageNotFoundComponent } from './features/page-not-found/page-not-found.component';
import { FoodItemComponent } from './components/food-item/food-item.component';
import { ForgotMyPasswordComponent } from './features/forgot-my-password/forgot-my-password.component';
import { CartComponent } from './features/cart/cart.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ShopWindowComponent,
    PageNotFoundComponent,
    FoodItemComponent,
    ForgotMyPasswordComponent,
    CartComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}