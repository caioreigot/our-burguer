package com.ourburguer.backend.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(value = "/product")
@Controller
public class ProductController {
  
  @GetMapping("list")
  @ResponseBody
  public ResponseEntity<?> list() {
    try {
      Connection con = ConnectionFactory.connect();
      PreparedStatement stmt;

      stmt = con.prepareStatement("SELECT * FROM product");
      ResultSet rs = stmt.executeQuery();

      List<HashMap<String, String>> products = new ArrayList<>();
      
      while (rs.next()) {
        String imageUrl = rs.getString("imageUrl");
        String title = rs.getString("title");
        String kcal = rs.getString("kcal");
        String price = rs.getString("price");
        String description = rs.getString("description");

        HashMap<String, String> product = Utils.createHashMap(
          new KeyValue("imageUrl", imageUrl),
          new KeyValue("title", title),
          new KeyValue("kcal", kcal),
          new KeyValue("price", price),
          new KeyValue("description", description)
        );

        products.add(product);
      }

      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch(Exception error) {
      return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
