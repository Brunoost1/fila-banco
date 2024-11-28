"use client";

import { useState, useEffect } from "react";
import api from "@/lib/api";
import { useRouter } from "next/navigation";
import { useAuth } from "@/contexts/AuthContext";
type Ticket = {
  id: number;
  senha: string;
  tipoAtendimento: string;
  status: string;
  guiche: number | null;
  posicaoFila: number;
  tempoEstimado: number;
  dataCriacao: string;
};

export default function Atendente() {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [senhaAtual, setSenhaAtual] = useState<Ticket | null>(null);
  const [guiche, setGuiche] = useState("1");
  const [loading, setLoading] = useState(false);
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isAuthenticated) {
      router.push("/login");
    }
  }, [isAuthenticated]);
  const getStatusLabel = (status: string) => {
    switch (status) {
      case "AGUARDANDO":
        return "Aguardando";
      case "CHAMANDO":
        return "Chamado";
      case "EM_ATENDIMENTO":
        return "Em Atendimento";
      case "FINALIZADO":
        return "Finalizado";
      default:
        return status;
    }
  };

  const buscarSenhas = async () => {
    try {
      const response = await api.get("/atendimento/aguardando");
      setTickets(response.data);
    } catch (error) {
      console.error("Erro ao buscar senhas:", error);
    }
  };

  const chamarProximo = async () => {
    try {
      setLoading(true);
      const response = await api.post(`/atendimento/chamar/${guiche}`);
      setSenhaAtual(response.data);
      await buscarSenhas();
    } catch (error) {
      console.error("Erro ao chamar próximo:", error);
    } finally {
      setLoading(false);
    }
  };

  const iniciarAtendimento = async (senha: string) => {
    try {
      setLoading(true);
      console.log("Iniciando atendimento:", senha);

      await api.post(`/atendimento/iniciar/${senha}`);
      const response = await api.get(`/atendimento/status/${senha}`);

      if (response.data.status === "EM_ATENDIMENTO") {
        setSenhaAtual(response.data);
        await buscarSenhas();
      } else {
        throw new Error("Status inválido após iniciar atendimento");
      }
    } catch (error) {
      console.error("Erro:", error);
      alert("Erro ao iniciar atendimento");
    } finally {
      setLoading(false);
    }
  };

  const finalizarAtendimento = async (senha: string) => {
    try {
      await api.post(`/atendimento/finalizar/${senha}`);
      setSenhaAtual(null);
      await buscarSenhas();
    } catch (error) {
      console.error("Erro ao finalizar atendimento:", error);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "AGUARDANDO":
        return "bg-yellow-100 text-yellow-800";
      case "CHAMANDO":
        return "bg-blue-100 text-blue-800";
      case "EM_ATENDIMENTO":
        return "bg-green-100 text-green-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  useEffect(() => {
    buscarSenhas();
    const interval = setInterval(buscarSenhas, 5000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-6xl mx-auto">
        {/* Header */}
        <div className="bg-white rounded-lg shadow-lg p-6 mb-8">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-2xl font-bold text-gray-800">
                Painel do Atendente
              </h1>
              <p className="text-gray-600">Guichê {guiche}</p>
            </div>
            <div className="flex items-center gap-4">
              <select
                value={guiche}
                onChange={(e) => setGuiche(e.target.value)}
                className="p-2 border rounded-lg"
              >
                <option value="1">Guichê 1</option>
                <option value="2">Guichê 2</option>
                <option value="3">Guichê 3</option>
              </select>
              <button
                onClick={chamarProximo}
                disabled={loading || senhaAtual !== null}
                className={`px-6 py-2 rounded-lg text-white font-medium ${
                  loading || senhaAtual !== null
                    ? "bg-gray-400 cursor-not-allowed"
                    : "bg-blue-600 hover:bg-blue-700"
                }`}
              >
                {loading ? "Chamando..." : "Chamar Próximo"}
              </button>
            </div>
          </div>
        </div>

        {/* Senha Atual */}
        {senhaAtual && (
          <div className="bg-green-50 rounded-lg shadow-lg p-6 mb-8">
            <div className="text-center">
              <h2 className="text-xl font-semibold text-green-800">
                {senhaAtual.status === "EM_ATENDIMENTO"
                  ? "Em Atendimento"
                  : "Chamando"}
              </h2>
              <p className="text-4xl font-bold text-green-600 mt-2">
                {senhaAtual.senha}
              </p>
              <p className="text-green-700 mt-2">
                {senhaAtual.tipoAtendimento?.replace("_", " ") ||
                  "Não especificado"}
              </p>
              <div className="flex justify-center gap-4 mt-4">
                {senhaAtual.status === "CHAMANDO" && (
                  <button
                    onClick={() => iniciarAtendimento(senhaAtual.senha)}
                    disabled={loading}
                    className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400"
                  >
                    {loading ? "Iniciando..." : "Iniciar Atendimento"}
                  </button>
                )}
                {senhaAtual.status === "EM_ATENDIMENTO" && (
                  <button
                    onClick={() => finalizarAtendimento(senhaAtual.senha)}
                    className="px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
                  >
                    Finalizar Atendimento
                  </button>
                )}
              </div>
            </div>
          </div>
        )}

        {/* Lista de Senhas Aguardando */}
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h2 className="text-xl font-semibold mb-4">Senhas Aguardando</h2>
          <div className="grid gap-4">
            {tickets.map((ticket) => (
              <div
                key={ticket.id}
                className="border rounded-lg p-4 flex justify-between items-center"
              >
                <div>
                  <p className="font-semibold">{ticket.senha}</p>
                  <p className="text-sm text-gray-600">
                    {ticket.tipoAtendimento?.replace("_", " ") ||
                      "Não especificado"}
                  </p>
                </div>
                <div className="flex items-center gap-4">
                  <span className="text-sm text-gray-500">
                    Espera: {ticket.tempoEstimado}min
                  </span>
                  <span className="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-sm">
                    Posição: {ticket.posicaoFila}
                  </span>
                  <span
                    className={`px-3 py-1 rounded-full text-sm ${getStatusColor(
                      ticket.status
                    )}`}
                  >
                    {ticket.tipoAtendimento?.replace("_", " ") ||
                      "Não especificado"}
                  </span>
                </div>
              </div>
            ))}
            {tickets.length === 0 && (
              <p className="text-gray-500 text-center py-4">
                Não há senhas aguardando atendimento
              </p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}