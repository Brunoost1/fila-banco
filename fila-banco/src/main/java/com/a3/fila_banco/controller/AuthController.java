package com.a3.fila_banco.controller;

import com.a3.fila_banco.model.LoginRequest;
import com.a3.fila_banco.model.JwtResponse;
import com.a3.fila_banco.service.AuthService;
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
        if (authService.validarCredenciais(request.getEmail(), request.getSenha())) {
            String token = authService.gerarToken(request.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }
    }
}