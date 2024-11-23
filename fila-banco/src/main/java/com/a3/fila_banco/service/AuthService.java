package com.a3.fila_banco.service;

import com.a3.fila_banco.model.Cliente;
import com.a3.fila_banco.repository.ClienteRepository;
import com.a3.fila_banco.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validarCredenciais(String email, String password) {
        Cliente cliente = clienteRepository.findByEmail(email)
            .orElse(null);
        
        if (cliente != null) {
            // Verifica se a senha fornecida corresponde à senha criptografada
            return passwordEncoder.matches(password, cliente.getSenha());
        }
        
        return false;
    }

    public String gerarToken(String email) {
        return jwtUtil.generateToken(email);
    }
}