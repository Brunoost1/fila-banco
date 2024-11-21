package com.a3.fila_banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a3.fila_banco.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
