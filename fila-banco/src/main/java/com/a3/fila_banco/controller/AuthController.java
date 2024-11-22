package com.a3.fila_banco.controller;

import com.a3.fila_banco.utils.JwtUtil;
import com.a3.fila_banco.model.LoginRequest;
import com.a3.fila_banco.model.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") // Endpoint para autenticação
public class AuthController {

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * Endpoint para autenticar o usuário e gerar o token JWT.
   *
   * @param request Objeto contendo o username e senha.
   * @return Token JWT gerado.
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    // Aqui você deveria validar o usuário com um serviço (ex: banco de dados)
    // Exemplo básico sem validação real:
    if ("admin".equals(request.getUsername()) && "senha123".equals(request.getPassword())) {
      String token = jwtUtil.generateToken(request.getUsername());
      return ResponseEntity.ok(new JwtResponse(token));
    } else {
      return ResponseEntity.status(401).body("Usuário ou senha inválidos");
    }
  }
}
