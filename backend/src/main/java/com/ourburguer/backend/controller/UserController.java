package com.ourburguer.backend.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.springframework.http.HttpHeaders;
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

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/user")
@Controller
public class UserController {
  
  @PostMapping
  @ResponseBody
  public ResponseEntity<HashMap<String, String>> create(@RequestBody UserDTO userDTO) {
    try {
      Connection con = ConnectionFactory.connect();
      PreparedStatement stmt;

      stmt = con.prepareStatement("SELECT id FROM user WHERE email = ?");
      stmt.setString(1, userDTO.email);
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next()) {
        HashMap<String, String> response = new HashMap<String, String>() {{
          put("message", "Este e-mail já está em uso, por favor, forneça outro.");
        }};

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }

      stmt = con.prepareStatement(
        "INSERT INTO user(name, email, cpf, phone, encryptedPassword) VALUES(?, ?, ?, ?, ?)"
      );

      stmt.setString(1, userDTO.name);
      stmt.setString(2, userDTO.email);
      stmt.setString(3, userDTO.cpf);
      stmt.setString(4, userDTO.phone);
      
      String encryptedPassword = this.encryptInSha256(userDTO.password);
      stmt.setString(5, encryptedPassword);

      int affectedRows = stmt.executeUpdate();
      
      if (affectedRows > 0) {
        HashMap<String, String> response = new HashMap<String, String>() {{
          put("message", "Usuário cadastrado com sucesso.");
        }};

        ConnectionFactory.closeConnection(con, stmt);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
      } else {
        ConnectionFactory.closeConnection(con, stmt);
        throw new Exception();
      }
    } catch(Exception error) {
      HashMap<String, String> response = new HashMap<String, String>() {{
        put("message", "Não foi possível realizar o cadastro.");
      }};

      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("auth")
  @ResponseBody
  public ResponseEntity<?> login(@RequestBody UserDTO userDTO, HttpSession session) {
    try {
      Connection con = ConnectionFactory.connect();
      
      PreparedStatement stmt = con.prepareStatement(
        "SELECT * FROM user WHERE email = ?"
      );

      stmt.setString(1, userDTO.email);
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next()) {
        String encryptedPassword = rs.getString("encryptedPassword");

        if (encryptedPassword.equals(this.encryptInSha256(userDTO.password))) {
          HttpHeaders headers = new HttpHeaders();
          headers.add("Location", "/shop-window");
          
          // TODO
          // session.setAttribute("name_logged_user", rs.getString("name"));

          return new ResponseEntity<String>(headers, HttpStatus.OK);
        } else {
          throw new Exception("A senha está errada.");
        }
      } else {
        throw new Exception("Não há cadastro de usuário com este e-mail.");
      }
    } catch(Exception error) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Location", "/");    
      return new ResponseEntity<String>(error.getMessage(), headers, HttpStatus.FORBIDDEN);
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
