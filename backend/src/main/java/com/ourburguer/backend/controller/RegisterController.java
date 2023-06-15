package com.ourburguer.backend.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import com.ourburguer.backend.utils.KeyValue;
import com.ourburguer.backend.utils.Utils;

@CrossOrigin(
  origins = "http://localhost:4200",
  allowedHeaders = "*",
  allowCredentials = "true"
)
@RequestMapping(value = "/register")
@Controller
public class RegisterController {

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
        HashMap<String, String> response = Utils.createHashMap(
          new KeyValue("message", "Este e-mail já está em uso, por favor, forneça outro.")
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }

      stmt = con.prepareStatement(
        "INSERT INTO user(name, email, cpf, phone, encryptedPassword) VALUES(?, ?, ?, ?, ?)"
      );

      stmt.setString(1, userDTO.name);
      stmt.setString(2, userDTO.email);
      stmt.setString(3, userDTO.cpf);
      stmt.setString(4, userDTO.phone);
      
      String encryptedPassword = Utils.encryptInSha256(userDTO.password);
      stmt.setString(5, encryptedPassword);
      
      int affectedRows = stmt.executeUpdate();

      if (affectedRows > 0) {
        HashMap<String, String> response = Utils.createHashMap(
          new KeyValue("message", "Usuário cadastrado com sucesso.")
        );

        ConnectionFactory.closeConnection(con, stmt);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
      } else {
        ConnectionFactory.closeConnection(con, stmt);
        throw new Exception();
      }
    } catch(Exception error) {
      HashMap<String, String> response = Utils.createHashMap(
        new KeyValue("message", "Não foi possível realizar o cadastro.")
      );

      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
  }
}
