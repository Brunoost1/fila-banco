package com.a3.fila_banco.controller;

import com.a3.fila_banco.service.AuthService;
import com.a3.fila_banco.model.LoginRequest;
import com.a3.fila_banco.model.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    if (authService.validarCredenciais(request.getUsername(), request.getPassword())) {
      String token = authService.gerarToken(request.getUsername());
      return ResponseEntity.ok(new JwtResponse(token));
    } else {
      return ResponseEntity.status(401).body("Usuário ou senha inválidos");
    }
  }
}
