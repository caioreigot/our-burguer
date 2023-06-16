package com.ourburguer.backend.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class Utils {

  public static String encryptInSha256(String text) {
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

  public static String randomHexString(int size) {
    SecureRandom random = new SecureRandom();
    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    StringBuilder sb = new StringBuilder();
    
    for (int i = 0; i < size; i++) {
      sb.append(hexDigits[random.nextInt(hexDigits.length)]);
    }

    return sb.toString();
  }
}
