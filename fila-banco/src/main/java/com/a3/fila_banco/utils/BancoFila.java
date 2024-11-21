package com.a3.fila_banco.utils;
import java.util.LinkedList;
import java.util.Queue;
public class BancoFila {
  private Queue<String> fila = new LinkedList<>();

  public void adicionarCliente(String cliente) {
      fila.add(cliente);
  }

  public String atenderCliente() {
      return fila.poll();
  }

  public boolean isFilaVazia() {
      return fila.isEmpty();
  }
}
