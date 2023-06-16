import { CartItem } from 'src/app/models/CartItem';
import { CartService } from './../../services/cart.service';
import { Component } from '@angular/core';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.less']
})
export class CartComponent {
  
  constructor(
    public cartService: CartService,
    private snackbarService: SnackbarService,
    private http: HttpClient
  ) {}

  get cartItems(): CartItem[] {
    return this.cartService.cartItems;
  }

  get total(): number {
    return this.cartService.cartItems.reduce((acc, current) => {
      return acc + (current.price * current.amount);
    }, 0);
  }

  clearCart() {
    this.cartService.clearCart();
    this.snackbarService.showMessage('O carrinho foi limpo!');
  }

  order() {
    this.http.post("order", this.cartItems).subscribe({
      next: (response: any) => {
        this.snackbarService.showMessage(response.message);
        this.cartService.clearCart();
      },
      error: (response: any) => this.snackbarService.showMessage(response.error)
    });
  }
}
