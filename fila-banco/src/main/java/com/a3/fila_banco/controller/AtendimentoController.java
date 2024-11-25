package com.a3.fila_banco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.a3.fila_banco.model.Ticket;
import com.a3.fila_banco.model.SenhaRequest;
import com.a3.fila_banco.service.AtendimentoService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/atendimento")
public class AtendimentoController {

    @Autowired
    private AtendimentoService atendimentoService;

    @PostMapping("/senha")
    public ResponseEntity<Ticket> gerarSenha(@RequestBody SenhaRequest request) {
        try {
            System.out.println("Recebendo requisição para gerar senha. ClienteId: " + request.getClienteId());
            Ticket ticket = atendimentoService.gerarSenha(
                request.getClienteId(),
                request.getTipoAtendimento()
            );
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
        try {
            Ticket ticket = atendimentoService.consultarTicket(senha);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/iniciar/{senha}")
    public ResponseEntity<Ticket> iniciarAtendimento(@PathVariable String senha) {
        try {
            Ticket ticket = atendimentoService.iniciarAtendimento(senha);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/finalizar/{senha}")
    public ResponseEntity<Ticket> finalizarAtendimento(@PathVariable String senha) {
        try {
            Ticket ticket = atendimentoService.finalizarAtendimento(senha);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/relatorio")
    public ResponseEntity<Map<String, List<Ticket>>> gerarRelatorio() {
        return ResponseEntity.ok(atendimentoService.gerarRelatorioAtendimentos());
    }

    @GetMapping("/busca/{tipo}")
    public ResponseEntity<List<Ticket>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(atendimentoService.buscarAtendimentosPorTipo(tipo));
    }
}