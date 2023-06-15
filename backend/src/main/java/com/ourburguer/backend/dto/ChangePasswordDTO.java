package com.ourburguer.backend.dto;

public class ChangePasswordDTO {
  
  public String newPassword;
  public String roomId;

  ChangePasswordDTO() {}

  ChangePasswordDTO(String newPassword, String roomId) {
    this.newPassword = newPassword;
    this.roomId = roomId;
  }
}
