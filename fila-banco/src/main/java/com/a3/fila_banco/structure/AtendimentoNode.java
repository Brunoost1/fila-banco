package com.a3.fila_banco.structure;

import java.util.ArrayList;
import java.util.List;

import com.a3.fila_banco.model.Ticket;

public class AtendimentoNode {
    private final String tipo;
    private AtendimentoNode left;
    private AtendimentoNode right;
    private final List<Ticket> tickets;

    public AtendimentoNode(String tipo) {
        this.tipo = tipo;
        this.tickets = new ArrayList<>();
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public String getTipo() {
        return tipo;
    }

    public AtendimentoNode getLeft() {
        return left;
    }

    public void setLeft(AtendimentoNode left) {
        this.left = left;
    }

    public AtendimentoNode getRight() {
        return right;
    }

    public void setRight(AtendimentoNode right) {
        this.right = right;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}