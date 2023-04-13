import { CartService } from './../../services/cart.service';
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
    { id: 1, urlImage: "/assets/Comuna Burguer.png", price: 10.00, name: "Comuna Burger", kcal: 475, description: "Este hambúrguer é composto por um pão macio e fresquinho, uma suculenta carne bovina, queijo cheddar derretido, cebola caramelizada e um molho especial de maionese com mostarda. Todos os ingredientes são cuidadosamente selecionados para garantir o melhor sabor e qualidade." },
    { id: 2, urlImage: "/assets/Sundae da Coletividade.png", price: 7.00, name: "Sundae da Coletividade", kcal: 325, description: " Este sundae representa a ideia de que juntos somos mais fortes. É feito com uma bola de sorvete de creme, fatias de banana fresca, calda de caramelo quente e chantilly. Todos os ingredientes são cuidadosamente selecionados para oferecer uma sobremesa deliciosa e compartilhável." },
    { id: 3, urlImage: "/assets/Igualdade Burguer.png", price: 12.00, name: "Igualdade Burger", kcal: 530, description: "Este hambúrguer é um verdadeiro exemplo da colaboração entre todos os membros da comunidade. Ele é feito com um pão artesanal crocante, carne bovina suculenta, alface fresca, tomate maduro e um molho especial de maionese com ervas finas. Cada ingrediente é escolhido com cuidado e amor para oferecer um sabor incrível." },
    { id: 4, urlImage: "/assets/Brownie da Solidariedade.png", price: 9.00, name: "Brownie da Solidariedade", kcal: 345, description: "Este brownie é feito com chocolate amargo de alta qualidade e servido quente com uma bola de sorvete de baunilha cremoso. É a combinação perfeita de texturas e sabores e representa a importância da solidariedade em nossa comunidade." },
    { id: 5, urlImage: "/assets/Fraternidade Burger.png", price: 14.00, name: "Fraternidade Burger", kcal: 725, description: "Este hambúrguer tem um pão macio e fresquinho, uma generosa porção de carne bovina suculenta, queijo derretido, alface crocante, tomate maduro e um molho especial de maionese com alho. Cada ingrediente é escolhido com cuidado para oferecer um sabor incrível e criar uma experiência fraterna e acolhedora." },
    { id: 6, urlImage: "/assets/Milkshake da União.png", price: 12.00, name: "Milkshake da União", kcal: 450, description: "Este milkshake é feito com morangos frescos, sorvete de baunilha e leite gelado. É uma bebida cremosa e refrescante que representa a união de todos os membros da comunidade. É servido com chantilly por cima para um toque extra de indulgência." },
  ];

  foodItemsFiltered: FoodItem[] = this.foodItems;

  @ViewChild('search')
  search: ElementRef<HTMLInputElement> | null = null;

  detailsId: number | null = null;
  itemToShowDetails: FoodItem = { id: 0, price: 0, name: "", urlImage: "", kcal: 0, description: "" };

  constructor(private route: ActivatedRoute, private cartService: CartService) {}

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
  }

  ngAfterViewInit() {
    const searchInput = this.search?.nativeElement;
    if (!searchInput) return;

    // Filtrando o array de comidas de acordo com a pesquisa do usuário
    searchInput.onkeyup = (e: any) => this.foodItemsFiltered = this.filterFoods(e.target.value);
  }
}
