package com.a3.fila_banco.service;

import com.a3.fila_banco.model.Cliente;
import com.a3.fila_banco.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Lista todos os clientes.
     */
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    /**
     * Cria um novo cliente.
     */
    public Cliente criarCliente(Cliente cliente) {
        // Exemplo de validação antes de salvar
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O e-mail é obrigatório!");
        }
        cliente.setAtivo(true); // Define como ativo por padrão
        cliente.setDataCriacao(java.time.LocalDateTime.now()); // Define a data de criação
        return clienteRepository.save(cliente);
    }

    /**
     * Busca um cliente pelo ID.
     */
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Deleta um cliente pelo ID.
     */
    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
