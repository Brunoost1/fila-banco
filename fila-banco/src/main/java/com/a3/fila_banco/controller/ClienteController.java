package com.a3.fila_banco.controller;

import com.a3.fila_banco.model.Cliente;
import com.a3.fila_banco.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listarTodos();
    }

    @PostMapping
    public Cliente criar(@RequestBody Cliente cliente) {
        return clienteService.criarCliente(cliente);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        clienteService.deletarCliente(id);
    }
}
