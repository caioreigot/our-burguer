import { CartItem } from 'src/app/models/CartItem';
import { CartService } from './../../services/cart.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.less']
})
export class CartComponent {
  
  constructor(public cartService: CartService) {}

  get cartItems(): CartItem[] {
    return this.cartService.cartItems;
  }

  get total(): number {
    return this.cartService.cartItems.reduce((acc, current) => {
      return acc + (current.price * current.amount);
    }, 0);
  }
}
