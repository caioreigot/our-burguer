package com.ourburguer.backend.dto;

public class ProductDTO {

  public int id;
  public String imageUrl;
  public String title;
  public float price;
  public int kcal;
  public String description;

  public ProductDTO() {}
  
  public ProductDTO(int id, String imageUrl, String title, float price, int kcal, String description) {
    this.id = id;
    this.imageUrl = imageUrl;
    this.title = title;
    this.price = price;
    this.kcal = kcal;
    this.description = description;
  }

  public ProductDTO(String imageUrl, String title, float price, int kcal, String description) {
    this.imageUrl = imageUrl;
    this.title = title;
    this.price = price;
    this.kcal = kcal;
    this.description = description;
  }
}
