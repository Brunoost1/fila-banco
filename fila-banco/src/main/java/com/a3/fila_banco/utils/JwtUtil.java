package com.a3.fila_banco.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

  private final String secretKey = "seuSegredoJWT";
  private final int tokenExpiration = 3600000;

  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
