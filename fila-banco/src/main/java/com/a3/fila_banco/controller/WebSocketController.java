package com.a3.fila_banco.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.a3.fila_banco.model.Ticket;

@Controller
public class WebSocketController {

    @MessageMapping("/atualizar-painel")
    @SendTo("/topic/painel")
    public Ticket atualizarPainel(Ticket ticket) {
        return ticket;
    }
}