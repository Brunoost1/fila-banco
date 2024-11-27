"use client";

import { useEffect, useState } from "react";
import api from "@/lib/api";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

type Senha = {
  senha: string;
  guiche: number;
  status: string;
  tipoAtendimento: string;
};

export default function PainelSenhas() {
  const [senhaAtual, setSenhaAtual] = useState<Senha | null>(null);
  const [senhasChamadas, setSenhasChamadas] = useState<Senha[]>([]);

  useEffect(() => {
    const atualizarSenhaAtual = async () => {
      try {
        const response = await api.get("/atendimento/chamadas");
        const senhas = response.data;
        const senhaAtiva = senhas.find(s => 
          s.status === "CHAMANDO" || s.status === "EM_ATENDIMENTO"
        );
        setSenhaAtual(senhaAtiva || null);
        setSenhasChamadas(senhas);
      } catch (error) {
        console.error("Erro:", error);
      }
    };
  
    atualizarSenhaAtual();
    const interval = setInterval(atualizarSenhaAtual, 5000);
    return () => clearInterval(interval);
  }, []);
  const buscarSenhasChamadas = async () => {
    try {
      const response = await api.get("/atendimento/chamadas");
      setSenhasChamadas(response.data);
    } catch (error) {
      console.error("Erro ao buscar senhas:", error);
    }
  };

  useEffect(() => {
    buscarSenhasChamadas();
    const interval = setInterval(buscarSenhasChamadas, 5000);
  
    const socket = new SockJS("http://localhost:8080/ws");
    const stompClient = Stomp.over(socket);
  
    stompClient.connect({}, () => {
      console.log("WebSocket Connected");
      stompClient.subscribe("/topic/senhas", (message) => {
        console.log("Received:", message.body);
        const data = JSON.parse(message.body);
        setSenhaAtual(data);
        buscarSenhasChamadas();
      });
    }, (error) => {
      console.error("WebSocket Error:", error);
    });
  
    return () => {
      clearInterval(interval);
      if (stompClient && stompClient.connected) {
        stompClient.disconnect();
      }
    };
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-4xl mx-auto space-y-6">
        <div className="bg-blue-600 text-white p-8 rounded-lg text-center">
          <h2 className="text-2xl mb-4">Senha Atual</h2>
          <div className="text-6xl font-bold">{senhaAtual?.senha || "-"}</div>
          <div className="text-xl mt-2">
            Guichê: {senhaAtual?.guiche || "-"}
          </div>
          <div className="mt-2">
            {senhaAtual?.tipoAtendimento?.replace("_", " ") || "-"}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-xl font-bold mb-4">Senhas Chamadas</h3>
          <div className="space-y-2">
            {senhasChamadas.map((senha, index) => (
              <div
                key={index}
                className="bg-gray-100 p-4 rounded flex justify-between items-center"
              >
                <div>
                  <span className="font-bold">{senha.senha}</span>
                  <span className="ml-2 text-gray-600">
                    {senha.tipoAtendimento?.replace("_", " ")}
                  </span>
                </div>
                <div className="text-gray-600">Guichê {senha.guiche}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
