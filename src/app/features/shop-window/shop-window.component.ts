import { AfterViewInit, Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FoodItem } from 'src/app/models/FoodItem';

@Component({
  selector: 'app-shop-window',
  templateUrl: './shop-window.component.html',
  styleUrls: ['./shop-window.component.less']
})
export class ShopWindowComponent implements AfterViewInit, OnInit {
  foodItems: FoodItem[] = [
    { id: 1, urlImage: "https://burgerx.com.br/assets/img/galeria/burgers/x-bacon.jpg", price: 5.98, title: "Hamburguer A", kcal: 612, description: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Quam voluptatum ab velit ullam necessitatibus molestias adipisci excepturi incidunt culpa natus, possimus architecto! Suscipit, eos. Consequuntur obcaecati libero sed minus ad est quos consectetur in neque ab doloremque nemo eveniet sequi necessitatibus dolores non molestias explicabo officia qui ut, iure voluptatum? Id, atque unde architecto eos tempora voluptas saepe molestiae possimus ullam earum aut sapiente dolore est eius nesciunt. Numquam non perspiciatis, vero, enim, autem impedit nihil eaque unde accusantium incidunt voluptatem iste nobis. Fugit hic quaerat ullam maiores unde placeat." },
    { id: 2, urlImage: "https://burgerx.com.br/assets/img/galeria/burgers/x-bacon.jpg", price: 5.98, title: "Combo B", kcal: 612, description: "" },
    { id: 3, urlImage: "https://burgerx.com.br/assets/img/galeria/burgers/x-bacon.jpg", price: 5.98, title: "Hamburguer C", kcal: 612, description: "" },
    { id: 4, urlImage: "https://burgerx.com.br/assets/img/galeria/burgers/x-bacon.jpg", price: 5.98, title: "Sorvete D", kcal: 612, description: "" },
    { id: 5, urlImage: "https://burgerx.com.br/assets/img/galeria/burgers/x-bacon.jpg", price: 5.98, title: "Hamburguer E", kcal: 612, description: "" },
    { id: 6, urlImage: "https://burgerx.com.br/assets/img/galeria/burgers/x-bacon.jpg", price: 5.98, title: "Torta F", kcal: 612, description: "" },
  ];

  foodItemsFiltered: FoodItem[] = this.foodItems;

  @ViewChild('search')
  search: ElementRef<HTMLInputElement> | null = null;

  detailsId: number | null = null;
  itemToShowDetails: FoodItem = { id: 0, price: 0, title: "", urlImage: "", kcal: 0, description: "" };

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.detailsId = Number(this.route.snapshot.paramMap.get('id')) || null;
    if (!this.detailsId) return;

    const item = this.foodItems.find(food => food.id == this.detailsId);
    if (!item) return;

    this.itemToShowDetails = item;
  }

  filterFoods(search: string) {
    return this.foodItems.filter(food =>
      food.title.toLowerCase().includes(search.toLowerCase())
    );
  }

  ngAfterViewInit() {
    const searchInput = this.search?.nativeElement;
    if (!searchInput) return;

    // Filtrando o array de comidas de acordo com a pesquisa do usuário
    searchInput.onkeyup = (e: any) => this.foodItemsFiltered = this.filterFoods(e.target.value);
  }
}
