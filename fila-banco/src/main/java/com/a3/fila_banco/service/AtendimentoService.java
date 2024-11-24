package com.a3.fila_banco.service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a3.fila_banco.model.Ticket;
import com.a3.fila_banco.model.Ticket.StatusAtendimento;
import com.a3.fila_banco.repository.TicketRepository;

@Service
public class AtendimentoService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    private AtomicInteger senhaCounter = new AtomicInteger(1);
    
    public Ticket gerarSenha(Long clienteId) {
        try {
            System.out.println("Gerando senha para cliente: " + clienteId);
            
            Ticket ticket = new Ticket();
            ticket.setClienteId(clienteId);
            ticket.setSenha(String.format("A%03d", senhaCounter.getAndIncrement()));
            ticket.setStatus(StatusAtendimento.AGUARDANDO);
            ticket.setDataCriacao(LocalDateTime.now());
            
            return ticketRepository.save(ticket);
        } catch (Exception e) {
            System.err.println("Erro ao gerar senha: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public Ticket chamarProximo(Integer guiche) {
        Ticket proximo = ticketRepository.findFirstByStatusOrderByDataCriacaoAsc(
            StatusAtendimento.AGUARDANDO);
            
        if (proximo != null) {
            proximo.setStatus(StatusAtendimento.CHAMANDO);
            proximo.setGuiche(guiche);
            return ticketRepository.save(proximo);
        }
        return null;
    }
    
    public Ticket consultarTicket(String senha) {
        return ticketRepository.findBySenha(senha).orElse(null);
    }
    
    public Ticket finalizarAtendimento(String senha) {
        Ticket ticket = ticketRepository.findBySenha(senha).orElse(null);
        if (ticket != null && ticket.getStatus() == StatusAtendimento.CHAMANDO) {
            ticket.setStatus(StatusAtendimento.FINALIZADO);
            return ticketRepository.save(ticket);
        }
        return null;
    }
    
    public Ticket iniciarAtendimento(String senha) {
        Ticket ticket = ticketRepository.findBySenha(senha).orElse(null);
        if (ticket != null && ticket.getStatus() == StatusAtendimento.CHAMANDO) {
            ticket.setStatus(StatusAtendimento.EM_ATENDIMENTO);
            return ticketRepository.save(ticket);
        }
        return null;
    }
}