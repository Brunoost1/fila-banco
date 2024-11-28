package com.a3.fila_banco.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
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
        try {
            Ticket proximo = ticketRepository.findFirstByStatusOrderByDataCriacaoAsc(
                StatusAtendimento.AGUARDANDO
            );
                
            if (proximo != null) {
                proximo.setStatus(StatusAtendimento.CHAMANDO);
                proximo.setGuiche(guiche);
                Ticket ticketSalvo = ticketRepository.save(proximo);
                atualizarFilaAposAtendimento(proximo.getTipoAtendimento());
                
                // Enviar atualizações via WebSocket
                try {
                    messagingTemplate.convertAndSend("/topic/senhas", ticketSalvo);
                } catch (Exception e) {
                    System.err.println("Erro ao enviar mensagem WebSocket: " + e.getMessage());
                    // Não deixa o erro do WebSocket impedir a operação principal
                }
                
                return ticketSalvo;
            }
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao chamar próximo: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao chamar próximo ticket", e);
        }
    }

    private void atualizarFilaAposAtendimento(TipoAtendimento tipo) {
        List<Ticket> ticketsAguardando = ticketRepository.findByTipoAtendimentoAndStatusOrderByDataCriacaoAsc(
            tipo, StatusAtendimento.AGUARDANDO
        );
        
        for (int i = 0; i < ticketsAguardando.size(); i++) {
            Ticket ticket = ticketsAguardando.get(i);
            ticket.setPosicaoFila(i + 1);
            ticket.setTempoEstimado((i + 1) * 10);
            ticketRepository.save(ticket);
        }
    }
    public List<Ticket> listarSenhasChamadas() {
        return ticketRepository.findByStatusInOrderByDataCriacaoDesc(
            Arrays.asList(StatusAtendimento.CHAMANDO, StatusAtendimento.EM_ATENDIMENTO)
        );
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
            ticket.setDataInicio(LocalDateTime.now());
            Ticket ticketSalvo = ticketRepository.save(ticket);
            messagingTemplate.convertAndSend("/topic/senhas", ticketSalvo);
            return ticketSalvo;
        } else {
            throw new RuntimeException("Ticket não está no status CHAMANDO");
        }
    }
    public Ticket finalizarAtendimento(String senha) {
        Ticket ticket = ticketRepository.findFirstBySenhaOrderByDataCriacaoDesc(senha)
            .orElseThrow(() -> new RuntimeException("Senha não encontrada"));
            
        if (ticket.getStatus() == StatusAtendimento.EM_ATENDIMENTO) {
            ticket.setStatus(StatusAtendimento.FINALIZADO);
            ticket.setDataFim(LocalDateTime.now());
            return ticketRepository.save(ticket);
        } else {
            throw new RuntimeException("Ticket não está em atendimento");
        }
    }

    public List<Ticket> listarPorStatus(StatusAtendimento status) {
        return ticketRepository.findByStatusOrderByDataCriacaoAsc(status);
    }

    public Map<String, List<Ticket>> gerarRelatorioAtendimentos() {
        return atendimentoTree.gerarRelatorio();
    }

    public List<Ticket> buscarAtendimentosPorTipo(String tipo) {
        return atendimentoTree.buscarPorTipo(tipo);
    }

    public List<Ticket> listarAguardandoPorTipo(TipoAtendimento tipo) {
        return ticketRepository.findByTipoAtendimentoAndStatusOrderByDataCriacaoAsc(
            tipo, StatusAtendimento.AGUARDANDO
        );
    }

    public int estimarTempoEspera(TipoAtendimento tipo) {
        int pessoasNaFrente = ticketRepository.countByTipoAtendimentoAndStatus(
            tipo, StatusAtendimento.AGUARDANDO
        );
        return pessoasNaFrente * 10; // 10 minutos por pessoa
    }
}