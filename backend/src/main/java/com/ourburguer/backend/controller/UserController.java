package com.ourburguer.backend.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ourburguer.backend.dto.UserDTO;
import com.ourburguer.backend.infra.ConnectionFactory;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/user")
@Controller
public class UserController {
  
  @PostMapping
  @ResponseBody
  public ResponseEntity<HashMap<String, String>> create(@RequestBody UserDTO userDTO) {
    try {
      Connection con = ConnectionFactory.connect();
      PreparedStatement stmt = con.prepareStatement("INSERT INTO user(name, email, cpf, phone, encryptedPassword) VALUES(?, ?, ?, ?, ?)");
      stmt.setString(1, userDTO.name);
      stmt.setString(2, userDTO.email);
      stmt.setString(3, userDTO.cpf);
      stmt.setString(4, userDTO.phone);
      
      String encryptedPassword = this.encryptInSha256(userDTO.password);
      stmt.setString(5, encryptedPassword);

      int affectedRows = stmt.executeUpdate();

      ConnectionFactory.closeConnection(con, stmt);
      
      if (affectedRows > 0) {
        HashMap<String, String> response = new HashMap<String, String>() {{
          put("message", "Usuário cadastrado com sucesso.");
        }};

        return new ResponseEntity<>(response, HttpStatus.CREATED);
      } else {
        throw new Exception();
      }
    } catch(Exception error) {
      HashMap<String, String> response = new HashMap<String, String>() {{
        put("message", "Ocorreu um erro ao cadastrar usuário. " + error.getMessage());
      }};

      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
  }

  private String encryptInSha256(String text) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
      StringBuilder hexString = new StringBuilder(2 * hash.length);
      
      for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        
        hexString.append(hex);
      }

      return hexString.toString();
    } catch(Exception error) {
      System.err.println("Ocorreu um erro ao criptografar a senha em SHA-256.");
      return null;
    }
  }
}
