import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CartItem } from '../models/CartItem';

@Injectable({ providedIn: 'root' })
export class CartService {

  private _cartItems: BehaviorSubject<CartItem[]> = new BehaviorSubject([] as CartItem[]);

  get cartItems(): CartItem[] {
    return this._cartItems.value;
  }

  set cartItems(cartItems: CartItem[]) {
    this._cartItems.next(cartItems);
  }

  clearCart() {
    this.cartItems = [];
  }

  addToCart(cartItemToAdd: CartItem) {
    const existingItemIndex = this.cartItems.findIndex(cartItem => cartItem.id == cartItemToAdd.id);

    // Se o mesmo produto já estiver no carrinho, apenas aumenta sua quantidade
    if (existingItemIndex >= 0) {
      this.cartItems[existingItemIndex].amount += cartItemToAdd.amount;
      return;
    }

    this.cartItems.push(cartItemToAdd);
  }
}
