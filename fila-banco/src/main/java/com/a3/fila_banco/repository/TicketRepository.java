package com.a3.fila_banco.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a3.fila_banco.model.Ticket;
import com.a3.fila_banco.model.Ticket.StatusAtendimento;
import com.a3.fila_banco.model.TipoAtendimento;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findFirstBySenhaOrderByDataCriacaoDesc(String senha);
    Ticket findFirstByStatusOrderByDataCriacaoAsc(StatusAtendimento status);
    List<Ticket> findByStatusOrderByDataCriacaoAsc(StatusAtendimento status);
    int countByTipoAtendimentoAndStatus(TipoAtendimento tipo, StatusAtendimento status);
}