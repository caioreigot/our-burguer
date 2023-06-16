package com.ourburguer.backend.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ourburguer.backend.Secrets;
import com.ourburguer.backend.dto.OrderItemDTO;
import com.ourburguer.backend.infra.ConnectionFactory;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(
  origins = "http://localhost:4200",
  allowedHeaders = "*",
  allowCredentials = "true"
)
@RequestMapping(value = "/order")
@Controller
public class OrderController {

  private final Algorithm JWT_ALG = Algorithm.HMAC256(Secrets.JWT_PASSWORD);

  @PostMapping
  @ResponseBody
  public ResponseEntity<?> order(HttpServletRequest request, @RequestBody OrderItemDTO[] items) {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    float total = 0;

    for (int i = 0; i < items.length; i++) {
      total += items[i].price * items[i].amount;
    }

    try {
      String jwt = request.getHeader("Authorization").replace("Bearer ", "");

      JWTVerifier verifier = JWT.require(this.JWT_ALG)
        .withIssuer("auth0")
        .build();

      DecodedJWT decodedJWT = verifier.verify(jwt);
      String email = decodedJWT.getClaim("email").asString();
      
      con = ConnectionFactory.connect();
      stmt = con.prepareStatement("SELECT id FROM user WHERE email = ?");
      stmt.setString(1, email);
      rs = stmt.executeQuery();
      int idUser = rs.getInt(1);

      ConnectionFactory.closeConnection(null, stmt, rs);
      stmt = con.prepareStatement("INSERT INTO cart(idUser, total, status) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      stmt.setInt(1, idUser);
      stmt.setFloat(2, total);
      stmt.setString(3, "Na fila");
      stmt.executeUpdate();

      ResultSet generatedKeys = stmt.getGeneratedKeys();
      if (!generatedKeys.next()) {
        throw new SQLException("A criação do carrinho falhou. Nenhum registro foi salvo.");
      }

      int idCart = generatedKeys.getInt(1);
      ConnectionFactory.closeConnection(null, stmt, rs);

      int successfullyAddeditems = 0;

      // Criando um cardItem pra cada item
      for (int i = 0; i < items.length; i++) {
        stmt = con.prepareStatement("INSERT INTO cartItem(idCart, itemName, amount, price) VALUES(?, ?, ?, ?)");
        stmt.setInt(1, idCart);
        stmt.setString(2, items[i].name);
        stmt.setInt(3, items[i].amount);
        stmt.setFloat(4, items[i].price);
        successfullyAddeditems += stmt.executeUpdate();
        ConnectionFactory.closeConnection(null, stmt, rs);
      }

      // Se todos os itens foram adicionados ao banco corretamente
      if (successfullyAddeditems == items.length) {
        String json = new JSONObject()
          .put("message", "O pedido foi efetuado com sucesso.")
          .toString();
          
        return new ResponseEntity<>(json, HttpStatus.OK);
      } else {
        throw new SQLException("Houve um erro inesperado ao salvar o pedido.");
      }
    } catch (Exception err) {
      return new ResponseEntity<>(err.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } finally {
      ConnectionFactory.closeConnection(con, stmt, rs);
    }
  }
}
