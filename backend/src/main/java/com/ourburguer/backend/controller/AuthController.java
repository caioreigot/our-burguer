package com.ourburguer.backend.controller;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ourburguer.backend.Secrets;
import com.ourburguer.backend.dto.ChangePasswordDTO;
import com.ourburguer.backend.dto.ForgotPasswordDTO;
import com.ourburguer.backend.dto.UserDTO;
import com.ourburguer.backend.infra.ConnectionFactory;
import com.ourburguer.backend.utils.Utils;

@CrossOrigin(
  origins = "http://localhost:4200",
  allowedHeaders = "*",
  allowCredentials = "true"
)
@RequestMapping(value = "/auth")
@Controller
public class AuthController {

  private final String JWT_ISSUER = "auth0";
  private final Algorithm JWT_ALG = Algorithm.HMAC256(Secrets.JWT_PASSWORD);

  @PostMapping
  @ResponseBody
  public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      con = ConnectionFactory.connect();

      stmt = con.prepareStatement("SELECT * FROM user WHERE email = ?");
      stmt.setString(1, userDTO.email);
      rs = stmt.executeQuery();

      if (!rs.next()) {
        throw new Exception("E-mail e/ou senha errados.");
      }

      String encryptedPassword = rs.getString("encryptedPassword");
      String name = rs.getString("name");
      boolean doHashesMatch = encryptedPassword.equals(Utils.encryptInSha256(userDTO.password));

      if (doHashesMatch) {
        String token = JWT.create()
          .withClaim("name", name)
          .withClaim("email", userDTO.email)
          .withIssuer(this.JWT_ISSUER)
          .sign(this.JWT_ALG);

        String json = new JSONObject()
          .put("token", token)
          .put("name", name)
          .toString();

        return new ResponseEntity<>(json, HttpStatus.OK);
      } else {
        throw new Exception("E-mail e/ou senha errados.");
      }
    } catch(Exception error) {
      return new ResponseEntity<>(error.getMessage(), HttpStatus.FORBIDDEN);
    } finally {
      ConnectionFactory.closeConnection(con, stmt, rs);
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

      String json = new JSONObject()
        .put("name", name)
        .toString();
      
      return new ResponseEntity<>(json, HttpStatus.OK);
    } catch (JWTVerificationException err){
      System.out.println(err.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("forgot-password")
  @ResponseBody 
  public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      con = ConnectionFactory.connect();

      stmt = con.prepareStatement("SELECT * FROM user WHERE email = ?");
      stmt.setString(1, forgotPasswordDTO.email);
      rs = stmt.executeQuery();
      
      // Se o e-mail não estiver cadastrado
      if (!rs.next()) {
        throw new Exception("E-mail não cadastrado.");
      }

      String randomHex = Utils.randomHexString(40);

      ConnectionFactory.closeConnection(null, stmt, rs);
      stmt = con.prepareStatement("INSERT INTO forgotPasswordRoom(email, roomId) VALUES(?, ?)");
      stmt.setString(1, forgotPasswordDTO.email);
      stmt.setString(2, randomHex);

      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        throw new Exception("Não foi possível salvar a sala para redefinição de senha no banco.");
      }

      ArrayList<String> sendTo = new ArrayList<String>(
        Arrays.asList(String.format("Cliente <%s>", forgotPasswordDTO.email))
      );

      String json = new JSONObject()
        .put("api_key", Secrets.SMTP_API_KEY)
        .put("to", sendTo)
        .put("sender", "Our Burguer <caio.rodrigues12@fatec.sp.gov.br>")
        .put("subject", "Redefinição de senha")
        .put("text_body", "Clique no link para alterar sua senha: http://localhost:4200/redefine-password/" + randomHex)
        .toString();

      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

      String url = "https://api.smtp2go.com/v3/email/send";
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

      HttpEntity<String> request = new HttpEntity<>(json, headers);
      String response = restTemplate.postForObject(url, request, String.class);
      System.out.println(response);

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception err) {
      System.err.println(err.getMessage());
      return new ResponseEntity<>(HttpStatus.OK);
    } finally {
      ConnectionFactory.closeConnection(con, stmt, rs);
    }
  }

  @PostMapping("change-password")
  @ResponseBody 
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      con = ConnectionFactory.connect();
      
      stmt = con.prepareStatement("SELECT * FROM forgotPasswordRoom WHERE roomId = ?");
      stmt.setString(1, changePasswordDTO.roomId);
      rs = stmt.executeQuery();

      // Se não houver salas com o id fornecido, retorna
      if (!rs.next()) {
        return new ResponseEntity<>(HttpStatus.OK);
      }

      String userEmail = rs.getString("email");
      
      ConnectionFactory.closeConnection(null, stmt, rs);
      stmt = con.prepareStatement("UPDATE user SET encryptedPassword = ? WHERE email = ?");
      stmt.setString(1, Utils.encryptInSha256(changePasswordDTO.newPassword));
      stmt.setString(2, userEmail);
      stmt.executeUpdate();

      ConnectionFactory.closeConnection(null, stmt, rs);
      stmt = con.prepareStatement("DELETE FROM forgotPasswordRoom WHERE email = ?");
      stmt.setString(1, userEmail);
      stmt.executeUpdate();

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception err) {
      System.err.println(err.getMessage());
      return new ResponseEntity<>(HttpStatus.OK);
    } finally {
      ConnectionFactory.closeConnection(con, stmt, rs);
    }
  }
}
