import { LocalStorageService } from 'src/app/services/local-storage.service';
import { CartService } from './../../services/cart.service';
import { AfterViewInit, Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FoodItem } from 'src/app/models/FoodItem';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-shop-window',
  templateUrl: './shop-window.component.html',
  styleUrls: ['./shop-window.component.less']
})
export class ShopWindowComponent implements AfterViewInit, OnInit {
  
  foodItems: FoodItem[] = [];
  foodItemsFiltered: FoodItem[] = this.foodItems;

  @ViewChild('search')
  search: ElementRef<HTMLInputElement> | null = null;

  detailsId: number | null = null;
  itemToShowDetails: FoodItem = { id: 0, price: 0, title: "", imageUrl: "", kcal: 0, description: "" };

  constructor(
    private route: ActivatedRoute,
    private cartService: CartService,
    private snackbarService: SnackbarService,
    public localStorageService: LocalStorageService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.http.get("product/list").subscribe({
      next: (response: any) => {
        this.foodItems = response;
        this.foodItemsFiltered = response;
        this.showDetailsIfUrlHasId();
      }
    });
  }

  ngAfterViewInit() {
    const searchInput = this.search?.nativeElement;
    if (!searchInput) return;

    // Filtrando o array de comidas de acordo com a pesquisa do usuário
    searchInput.onkeyup = (e: any) => {
      this.foodItemsFiltered = this.filterFoods(e.target.value);
    }
  }

  showDetailsIfUrlHasId() {
    this.detailsId = Number(this.route.snapshot.paramMap.get('id')) || null;
    if (!this.detailsId) return;

    // const item = this.foodItems.find(food => food.id == this.detailsId);
    // if (!item) return;

    this.http.get(`product/${this.detailsId}`).subscribe((response: any) => {
      this.itemToShowDetails = response;
    });
  }

  filterFoods(search: string): FoodItem[] {
    return this.foodItems.filter(food =>
      food.title.toLowerCase().includes(search.toLowerCase())
    );
  }

  addCart(item: FoodItem) {
    this.cartService.addToCart({
      id: item.id,
      amount: 1,
      name: item.title,
      price: item.price
    });

    this.snackbarService.showMessage(`${item.title} adicionado ao carrinho.`);
  }

  logout() {
    this.localStorageService.clearToken();
    this.cartService.clearCart();
  }
}
