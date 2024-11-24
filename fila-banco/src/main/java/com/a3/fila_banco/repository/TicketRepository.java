package com.a3.fila_banco.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a3.fila_banco.model.Ticket;
import com.a3.fila_banco.model.Ticket.StatusAtendimento;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findBySenha(String senha);
    Ticket findFirstByStatusOrderByDataCriacaoAsc(StatusAtendimento status);
}