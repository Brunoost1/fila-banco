package com.a3.fila_banco.repository;

import com.a3.fila_banco.model.Ticket;
import com.a3.fila_banco.model.Ticket.StatusAtendimento;
import com.a3.fila_banco.model.TipoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findFirstBySenhaOrderByDataCriacaoDesc(String senha);
    Ticket findFirstByStatusOrderByDataCriacaoAsc(StatusAtendimento status);
    int countByTipoAtendimentoAndStatus(TipoAtendimento tipo, StatusAtendimento status);
}