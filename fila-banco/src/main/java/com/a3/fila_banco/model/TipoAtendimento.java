package com.a3.fila_banco.model;

public enum TipoAtendimento {
    ABERTURA_CONTA("Abertura de Conta"),
    EMPRESTIMO("Empréstimo"),
    OUTROS("Outros Serviços");

    private final String descricao;

    TipoAtendimento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}