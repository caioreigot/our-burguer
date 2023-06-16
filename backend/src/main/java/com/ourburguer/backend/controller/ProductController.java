package com.ourburguer.backend.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ourburguer.backend.dto.ProductDTO;
import com.ourburguer.backend.infra.ConnectionFactory;

@CrossOrigin(
  origins = "http://localhost:4200",
  allowedHeaders = "*",
  allowCredentials = "true"
)
@RequestMapping(value = "/product")
@Controller
public class ProductController {
  
  @GetMapping("list")
  @ResponseBody
  public ResponseEntity<?> list() {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      con = ConnectionFactory.connect();

      stmt = con.prepareStatement("SELECT * FROM product");
      rs = stmt.executeQuery();

      List<ProductDTO> products = new ArrayList<>();
      
      while (rs.next()) {
        int id = rs.getInt(1);
        String imageUrl = rs.getString("imageUrl");
        String title = rs.getString("title");
        int kcal = rs.getInt("kcal");
        float price = rs.getFloat("price");
        String description = rs.getString("description");

        ProductDTO product = new ProductDTO(id, imageUrl, title, price, kcal, description);

        products.add(product);
      }

      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch(Exception error) {
      return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
    } finally {
      ConnectionFactory.closeConnection(con, stmt, rs);
    }
  }

  @GetMapping("{id}")
  @ResponseBody
  public ResponseEntity<?> getById(@PathVariable int id) {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      con = ConnectionFactory.connect();
      stmt = con.prepareStatement("SELECT * FROM product WHERE id = ?");
      stmt.setInt(1, id);
      rs = stmt.executeQuery();

      if (!rs.next()) {
        throw new Exception("Não há produto de id " + id);
      }
      
      int idResult = rs.getInt(1);
      String imageUrl = rs.getString("imageUrl");
      String title = rs.getString("title");
      int kcal = rs.getInt("kcal");
      float price = rs.getFloat("price");
      String description = rs.getString("description");

      ProductDTO product = new ProductDTO(idResult, imageUrl, title, price, kcal, description);
      return new ResponseEntity<>(product, HttpStatus.OK);
    } catch(Exception error) {
      return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
    } finally {
      ConnectionFactory.closeConnection(con, stmt, rs);
    }
  }
}
