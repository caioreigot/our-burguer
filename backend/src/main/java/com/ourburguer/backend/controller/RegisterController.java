package com.ourburguer.backend.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;
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
  public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      con = ConnectionFactory.connect();

      stmt = con.prepareStatement("SELECT id FROM user WHERE email = ?");
      stmt.setString(1, userDTO.email);
      rs = stmt.executeQuery();
      
      if (rs.next()) {
        String json = new JSONObject()
          .put("message", "Este e-mail já está em uso, por favor, forneça outro.")
          .toString();

        return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
      }

      ConnectionFactory.closeConnection(null, stmt, rs);
      stmt = con.prepareStatement(
        "INSERT INTO user(name, email, address, phone, encryptedPassword) VALUES(?, ?, ?, ?, ?)"
      );

      stmt.setString(1, userDTO.name);
      stmt.setString(2, userDTO.email);
      stmt.setString(3, userDTO.address);
      stmt.setString(4, userDTO.phone);
      
      String encryptedPassword = Utils.encryptInSha256(userDTO.password);
      stmt.setString(5, encryptedPassword);
      
      int affectedRows = stmt.executeUpdate();

      if (affectedRows == 0) {
        throw new Exception();
      }

      String json = new JSONObject()
        .put("message", "Usuário cadastrado com sucesso.")
        .toString();

      return new ResponseEntity<>(json, HttpStatus.CREATED);
    } catch(Exception error) {
      String json = new JSONObject()
        .put("message", "Não foi possível realizar o cadastro.")
        .toString();

      return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
    } finally {
      ConnectionFactory.closeConnection(con, stmt, rs);
    }
  }
}
