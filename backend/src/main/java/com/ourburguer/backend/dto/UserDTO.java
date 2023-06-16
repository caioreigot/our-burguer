package com.ourburguer.backend.dto;

public class UserDTO {

  public String name;
  public String email;
  public String address;
  public String phone;
  public String password;

  public UserDTO() {}

  public UserDTO(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public UserDTO(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public UserDTO(String name, String email, String password, String address) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.address = address;
  }

  public UserDTO(String name, String email, String password, String address, String phone) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.address = address;
    this.phone = phone;
  }
}
