package com.a3.fila_banco.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

  private final String secretKey = "seuSegredoJWT"; // Use uma chave segura e complexa aqui
  private final int tokenExpiration = 3600000; // 1 hora em milissegundos

  // Gerar Token
  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  // Validar Token
  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.ExpiredJwtException | io.jsonwebtoken.UnsupportedJwtException | io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.SignatureException | IllegalArgumentException e) {
      return false;
    }
  }

  // Extrair o nome de usuário do token
  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }
}
