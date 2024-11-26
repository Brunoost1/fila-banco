package com.a3.fila_banco.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.a3.fila_banco.model.Ticket;

public class AtendimentoTree {
    private final AtendimentoNode root;

    public AtendimentoTree() {
        // Cria a estrutura inicial da árvore
        this.root = new AtendimentoNode("TODOS");

        // Adiciona os tipos de atendimento como nós
        root.setLeft(new AtendimentoNode("CONTA"));
        root.setRight(new AtendimentoNode("EMPRESTIMO"));
        root.getRight().setRight(new AtendimentoNode("OUTROS"));
    }

    public void adicionarTicket(Ticket ticket) {
        switch (ticket.getTipoAtendimento()) {
            case ABERTURA_CONTA -> addToNode(root.getLeft(), ticket);
            case EMPRESTIMO -> addToNode(root.getRight(), ticket);
            default -> addToNode(root.getRight().getRight(), ticket);
        }
        // Adiciona também na raiz para ter todos os tickets
        addToNode(root, ticket);
    }

    private void addToNode(AtendimentoNode node, Ticket ticket) {
        if (node != null) {
            node.addTicket(ticket);
        }
    }

    public List<Ticket> buscarPorTipo(String tipo) {
        return findNode(root, tipo)
                .map(node -> node.getTickets())
                .orElse(new ArrayList<>());
    }

    private Optional<AtendimentoNode> findNode(AtendimentoNode current, String tipo) {
        if (current == null)
            return Optional.empty();
        if (current.getTipo().equals(tipo))
            return Optional.of(current);

        Optional<AtendimentoNode> left = findNode(current.getLeft(), tipo);
        if (left.isPresent())
            return left;

        return findNode(current.getRight(), tipo);
    }

    public Map<String, List<Ticket>> gerarRelatorio() {
        Map<String, List<Ticket>> relatorio = new HashMap<>();
        collectTickets(root, relatorio);
        return relatorio;
    }

    private void collectTickets(AtendimentoNode node, Map<String, List<Ticket>> relatorio) {
        if (node != null) {
            relatorio.put(node.getTipo(), new ArrayList<>(node.getTickets()));
            collectTickets(node.getLeft(), relatorio);
            collectTickets(node.getRight(), relatorio);
        }
    }
}