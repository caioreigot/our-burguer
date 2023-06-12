import { CartService } from './../../services/cart.service';
import { AfterViewInit, Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FoodItem } from 'src/app/models/FoodItem';
import dbFake from '../../dbFake.json';
import { SnackbarService } from 'src/app/services/snackbar.service';

@Component({
  selector: 'app-shop-window',
  templateUrl: './shop-window.component.html',
  styleUrls: ['./shop-window.component.less']
})
export class ShopWindowComponent implements AfterViewInit, OnInit {
  
  foodItems: FoodItem[] = dbFake.fake_data;

  foodItemsFiltered: FoodItem[] = this.foodItems;

  @ViewChild('search')
  search: ElementRef<HTMLInputElement> | null = null;

  detailsId: number | null = null;
  itemToShowDetails: FoodItem = { id: 0, price: 0, name: "", urlImage: "", kcal: 0, description: "" };

  constructor(
    private route: ActivatedRoute,
    private cartService: CartService,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    this.detailsId = Number(this.route.snapshot.paramMap.get('id')) || null;
    if (!this.detailsId) return;

    const item = this.foodItems.find(food => food.id == this.detailsId);
    if (!item) return;

    this.itemToShowDetails = item;
  }

  filterFoods(search: string) {
    return this.foodItems.filter(food =>
      food.name.toLowerCase().includes(search.toLowerCase())
    );
  }

  addCart(item: FoodItem) {
    this.cartService.addToCart({
      id: item.id,
      amount: 1,
      name: item.name,
      price: item.price
    });

    this.snackbarService.showMessage(`${item.name} adicionado ao carrinho.`);
  }

  ngAfterViewInit() {
    const searchInput = this.search?.nativeElement;
    if (!searchInput) return;

    // Filtrando o array de comidas de acordo com a pesquisa do usuário
    searchInput.onkeyup = (e: any) => this.foodItemsFiltered = this.filterFoods(e.target.value);
  }
}
