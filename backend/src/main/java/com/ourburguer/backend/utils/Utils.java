package com.ourburguer.backend.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

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

  public static HashMap<String, String> createHashMap(KeyValue... kv) {
    HashMap<String, String> hashMap = new HashMap<String, String>();

    for (KeyValue current : kv) {
      hashMap.put(current.key, current.value);
    }

    return hashMap;
  }
}
