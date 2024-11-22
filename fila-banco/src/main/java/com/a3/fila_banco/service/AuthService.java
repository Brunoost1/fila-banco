package com.a3.fila_banco.service;

import com.a3.fila_banco.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * Valida as credenciais de login.
   * Substitua essa lógica com validações reais, como consultas ao banco de dados.
   */
  public boolean validarCredenciais(String username, String password) {
    // Apenas um exemplo básico
    return "admin".equals(username) && "senha123".equals(password);
  }

  /**
   * Gera um token JWT para o usuário autenticado.
   */
  public String gerarToken(String username) {
    return jwtUtil.generateToken(username);
  }
}
