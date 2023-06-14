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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import com.ourburguer.backend.dto.UserDTO;
import com.ourburguer.backend.infra.ConnectionFactory;
import com.ourburguer.backend.utils.KeyValue;
import com.ourburguer.backend.utils.Utils;

@CrossOrigin(
  origins = "http://localhost:4200",
  allowedHeaders = "*",
  allowCredentials = "true"
)
@RequestMapping(value = "/auth")
@Controller
public class AuthController {

  private final String JWT_PASSWORD = "hF3G#175(rrAyFz_H[0Ehu4=^Oq7I)iiImpL]JXSr8qX)I1Ebz";
  private final String JWT_ISSUER = "auth0";
  private final Algorithm JWT_ALG = Algorithm.HMAC256(this.JWT_PASSWORD);

  @PostMapping
  @ResponseBody
  public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
    try {
      Connection con = ConnectionFactory.connect();

      PreparedStatement stmt = con.prepareStatement(
        "SELECT * FROM user WHERE email = ?"
      );

      stmt.setString(1, userDTO.email);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String encryptedPassword = rs.getString("encryptedPassword");
        String name = rs.getString("name");

        if (encryptedPassword.equals(Utils.encryptInSha256(userDTO.password))) {
          String token = JWT.create()
            .withClaim("name", name)
            .withIssuer(this.JWT_ISSUER)
            .sign(this.JWT_ALG);

          HashMap<String, String> response = Utils.createHashMap(
            new KeyValue("token", token),
            new KeyValue("name", name)
          );

          return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
          throw new Exception("A senha está errada.");
        }
      } else {
        throw new Exception("Não há cadastro de usuário com este e-mail.");
      }
    } catch(Exception error) {
      return new ResponseEntity<String>(error.getMessage(), HttpStatus.FORBIDDEN);
    }
  }

  @PostMapping("jwt")
  @ResponseBody
  public ResponseEntity<?> loginWithJwt(@RequestBody String jwt) {
    try {
      JWTVerifier verifier = JWT.require(this.JWT_ALG)
        .withIssuer("auth0")
        .build();
      
      DecodedJWT decodedJWT = verifier.verify(jwt);
      String name = decodedJWT.getClaim("name").asString();

      HashMap<String, String> response = Utils.createHashMap(
        new KeyValue("name", name)
      );
      
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (JWTVerificationException exception){
      System.out.println(exception.getMessage());
    }

    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
