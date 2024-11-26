package com.a3.fila_banco.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String senha;
    private Long clienteId;
    private LocalDateTime dataCriacao;
    private Integer guiche;
    private Integer posicaoFila;
    private Integer tempoEstimado;
    
    @Enumerated(EnumType.STRING)
    private StatusAtendimento status;
    
    @Enumerated(EnumType.STRING)
    private TipoAtendimento tipoAtendimento;

    public enum StatusAtendimento {
        AGUARDANDO,
        CHAMANDO,
        EM_ATENDIMENTO,
        FINALIZADO
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }
    private LocalDateTime dataInicio;



    public LocalDateTime getDataInicio() {

        return dataInicio;

    }



    public void setDataInicio(LocalDateTime dataInicio) {

        this.dataInicio = dataInicio;
    }

    private LocalDateTime dataFim;



    public LocalDateTime getDataFim() {

        return dataFim;

    }



    public void setDataFim(LocalDateTime dataFim) {

        this.dataFim = dataFim;

    }


    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Integer getGuiche() {
        return guiche;
    }

    public void setGuiche(Integer guiche) {
        this.guiche = guiche;
    }

    public StatusAtendimento getStatus() {
        return status;
    }

    public void setStatus(StatusAtendimento status) {
        this.status = status;
    }

    public TipoAtendimento getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(TipoAtendimento tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public Integer getPosicaoFila() {
        return posicaoFila;
    }

    public void setPosicaoFila(Integer posicaoFila) {
        this.posicaoFila = posicaoFila;
    }

    public Integer getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(Integer tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }
}