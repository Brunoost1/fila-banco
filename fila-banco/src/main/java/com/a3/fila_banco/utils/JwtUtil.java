package com.a3.fila_banco.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey = "seuSegredoJWT"; // Use uma chave segura e complexa aqui
    private final int tokenExpiration = 3600000; // 1 hora em milissegundos

    /**
     * Gera um token JWT para um determinado username.
     *
     * @param username O nome de usuário para o qual o token será gerado.
     * @return O token JWT.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Define o username como assunto
                .setIssuedAt(new Date()) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)) // Data de expiração
                .signWith(SignatureAlgorithm.HS256, secretKey) // Algoritmo de assinatura
                .compact();
    }

    /**
     * Valida se um token JWT é válido.
     *
     * @param token O token JWT a ser validado.
     * @return True se o token for válido, False caso contrário.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Token expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Token não suportado: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Token malformado: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Assinatura inválida: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Token inválido: " + e.getMessage());
        }
        return false;
    }

    /**
     * Extrai o nome de usuário (username) de um token JWT.
     *
     * @param token O token JWT.
     * @return O nome de usuário extraído do token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
