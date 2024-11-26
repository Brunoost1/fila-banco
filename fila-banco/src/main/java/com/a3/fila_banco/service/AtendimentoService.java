package com.a3.fila_banco.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a3.fila_banco.model.Ticket;
import com.a3.fila_banco.model.Ticket.StatusAtendimento;
import com.a3.fila_banco.model.TipoAtendimento;
import com.a3.fila_banco.repository.TicketRepository;
import com.a3.fila_banco.structure.AtendimentoTree;

@Service
public class AtendimentoService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    private AtomicInteger senhaCounter = new AtomicInteger(1);
    private final AtendimentoTree atendimentoTree = new AtendimentoTree();

    public Ticket gerarSenha(Long clienteId, TipoAtendimento tipoAtendimento) {
        try {
            System.out.println("Gerando senha para cliente: " + clienteId + " - Tipo: " + tipoAtendimento);
            
            Ticket ticket = new Ticket();
            ticket.setClienteId(clienteId);
            ticket.setTipoAtendimento(tipoAtendimento);
            ticket.setSenha(gerarCodigoSenha(tipoAtendimento));
            ticket.setStatus(StatusAtendimento.AGUARDANDO);
            ticket.setDataCriacao(LocalDateTime.now());
            
            int pessoasNaFrente = ticketRepository.countByTipoAtendimentoAndStatus(
                tipoAtendimento, StatusAtendimento.AGUARDANDO
            );
            ticket.setPosicaoFila(pessoasNaFrente + 1);
            ticket.setTempoEstimado((pessoasNaFrente + 1) * 10);
            
            Ticket ticketSalvo = ticketRepository.save(ticket);
            atendimentoTree.adicionarTicket(ticketSalvo);
            
            return ticketSalvo;
        } catch (Exception e) {
            System.err.println("Erro ao gerar senha: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private String gerarCodigoSenha(TipoAtendimento tipo) {
        String prefixo = switch (tipo) {
            case ABERTURA_CONTA -> "C";
            case EMPRESTIMO -> "E";
            case OUTROS -> "O";
        };
        return prefixo + String.format("%03d", senhaCounter.getAndIncrement());
    }

    public Ticket chamarProximo(Integer guiche) {
        Ticket proximo = ticketRepository.findFirstByStatusOrderByDataCriacaoAsc(
            StatusAtendimento.AGUARDANDO
        );
            
        if (proximo != null) {
            proximo.setStatus(StatusAtendimento.CHAMANDO);
            proximo.setGuiche(guiche);
            return ticketRepository.save(proximo);
        }
        return null;
    }

    public Ticket consultarTicket(String senha) {
        return ticketRepository.findFirstBySenhaOrderByDataCriacaoDesc(senha)
            .orElseThrow(() -> new RuntimeException("Senha não encontrada"));
    }

    public Ticket iniciarAtendimento(String senha) {
        Ticket ticket = ticketRepository.findFirstBySenhaOrderByDataCriacaoDesc(senha)
            .orElseThrow(() -> new RuntimeException("Senha não encontrada"));
            
        if (ticket.getStatus() == StatusAtendimento.CHAMANDO) {
            ticket.setStatus(StatusAtendimento.EM_ATENDIMENTO);
            return ticketRepository.save(ticket);
        } else {
            throw new RuntimeException("Ticket não está no status CHAMANDO");
        }
    }
    
    public Ticket finalizarAtendimento(String senha) {
        Ticket ticket = ticketRepository.findFirstBySenhaOrderByDataCriacaoDesc(senha)
            .orElseThrow(() -> new RuntimeException("Senha não encontrada"));
            
        if (ticket.getStatus() == StatusAtendimento.EM_ATENDIMENTO) {
            ticket.setStatus(StatusAtendimento.FINALIZADO);
            return ticketRepository.save(ticket);
        } else {
            throw new RuntimeException("Ticket não está em atendimento");
        }
    }

    public Map<String, List<Ticket>> gerarRelatorioAtendimentos() {
        return atendimentoTree.gerarRelatorio();
    }

    public List<Ticket> buscarAtendimentosPorTipo(String tipo) {
        return atendimentoTree.buscarPorTipo(tipo);
    }

    public List<Ticket> listarPorStatus(StatusAtendimento status) {
        return ticketRepository.findByStatusOrderByDataCriacaoAsc(status);
    }
}