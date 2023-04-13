import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-food-item',
  templateUrl: './food-item.component.html',
  styleUrls: ['./food-item.component.less']
})
export class FoodItemComponent {
  @Input() id: number = 0;
  @Input() urlImage: string = "";
  @Input() name: string = "";
  @Input() price: number = 0;
}