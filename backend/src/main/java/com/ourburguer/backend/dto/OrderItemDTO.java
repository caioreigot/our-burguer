package com.ourburguer.backend.dto;

public class OrderItemDTO {

  public int id;
  public String name;
  public int amount;
  public float price;

  OrderItemDTO() {}

  OrderItemDTO(int id, String name, int amount, float price) {
    this.id = id;
    this.name = name;
    this.amount = amount;
    this.price = price;
  }
}