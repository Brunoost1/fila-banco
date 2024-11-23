package com.a3.fila_banco.service;

import com.a3.fila_banco.model.Cliente;
import com.a3.fila_banco.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente criarCliente(Cliente cliente) {
        // Validações
        if (cliente.getSenha() == null || cliente.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser vazia");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O email não pode ser vazio");
        }
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser vazio");
        }

        // Verifica se já existe um cliente com este email
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        cliente.setDataCriacao(LocalDateTime.now());
        cliente.setAtivo(true);
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        
        return clienteRepository.save(cliente);
    }

    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}