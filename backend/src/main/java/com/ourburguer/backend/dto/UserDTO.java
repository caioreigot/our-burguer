package com.ourburguer.backend.dto;

public class UserDTO {

  public String name;
  public String email;
  public String cpf;
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

  public UserDTO(String name, String email, String password, String cpf) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.cpf = cpf;
  }

  public UserDTO(String name, String email, String password, String cpf, String phone) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.cpf = cpf;
    this.phone = phone;
  }
}
