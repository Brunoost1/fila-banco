package com.a3.fila_banco.controller;

import java.util.List;
import java.util.Map;

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
import com.a3.fila_banco.model.Ticket.StatusAtendimento;
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
    
    @GetMapping("/chamadas")
    public ResponseEntity<List<Ticket>> listarChamadas() {
        List<Ticket> tickets = atendimentoService.listarSenhasChamadas();
        return ResponseEntity.ok(tickets);
    }
    @PostMapping("/chamar/{guiche}")
    public ResponseEntity<Ticket> chamarProximo(@PathVariable Integer guiche) {
        try {
            System.out.println("Chamando próximo ticket para guichê: " + guiche);
            Ticket ticket = atendimentoService.chamarProximo(guiche);
            if (ticket != null) {
                System.out.println("Ticket chamado: " + ticket.getSenha());
                return ResponseEntity.ok(ticket);
            }
            System.out.println("Nenhum ticket disponível para atendimento");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Erro ao chamar próximo ticket: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
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

      @GetMapping("/aguardando")
    public ResponseEntity<List<Ticket>> listarAguardando() {
        try {
            List<Ticket> tickets = atendimentoService.listarPorStatus(StatusAtendimento.AGUARDANDO);
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            System.err.println("Erro ao listar tickets aguardando: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
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