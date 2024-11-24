package com.a3.fila_banco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a3.fila_banco.model.SenhaRequest;
import com.a3.fila_banco.model.Ticket;
import com.a3.fila_banco.service.AtendimentoService;

@RestController
@RequestMapping("/atendimento")
public class AtendimentoController {

    @Autowired
    private AtendimentoService atendimentoService;

    @PostMapping("/senha")
    public ResponseEntity<Ticket> gerarSenha(@RequestBody SenhaRequest request) {
        try {
            System.out.println("Recebendo requisição para gerar senha. ClienteId: " + request.getClienteId());
            Ticket ticket = atendimentoService.gerarSenha(request.getClienteId());
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            System.err.println("Erro ao gerar senha: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/chamar/{guiche}")
    public ResponseEntity<Ticket> chamarProximo(@PathVariable Integer guiche) {
        Ticket ticket = atendimentoService.chamarProximo(guiche);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{senha}")
    public ResponseEntity<Ticket> consultarStatus(@PathVariable String senha) {
        Ticket ticket = atendimentoService.consultarTicket(senha);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/iniciar/{senha}")
    public ResponseEntity<Ticket> iniciarAtendimento(@PathVariable String senha) {
        Ticket ticket = atendimentoService.iniciarAtendimento(senha);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/finalizar/{senha}")
    public ResponseEntity<Ticket> finalizarAtendimento(@PathVariable String senha) {
        Ticket ticket = atendimentoService.finalizarAtendimento(senha);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        }
        return ResponseEntity.notFound().build();
    }
}