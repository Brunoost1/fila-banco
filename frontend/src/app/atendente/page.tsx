'use client';

import { useState, useEffect } from 'react';
import api from '@/lib/api';

type Ticket = {
  id: number;
  senha: string;
  tipoAtendimento: string;
  status: string;
  guiche: number | null;
};

export default function Atendente() {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [senhaAtual, setSenhaAtual] = useState<Ticket | null>(null);
  const [guiche, setGuiche] = useState('1');
  const [loading, setLoading] = useState(false);

  // Buscar senhas aguardando
  const buscarSenhas = async () => {
    try {
      const response = await api.get('/atendimento/aguardando');
      setTickets(response.data);
    } catch (error) {
      console.error('Erro ao buscar senhas:', error);
    }
  };

  // Chamar próxima senha
  const chamarProximo = async () => {
    try {
      setLoading(true);
      const response = await api.post(`/atendimento/chamar/${guiche}`);
      setSenhaAtual(response.data);
      await buscarSenhas(); // Atualiza a lista
    } catch (error) {
      console.error('Erro ao chamar próximo:', error);
    } finally {
      setLoading(false);
    }
  };

  // Finalizar atendimento
  const finalizarAtendimento = async (senha: string) => {
    try {
      await api.post(`/atendimento/finalizar/${senha}`);
      setSenhaAtual(null);
      await buscarSenhas();
    } catch (error) {
      console.error('Erro ao finalizar atendimento:', error);
    }
  };

  useEffect(() => {
    buscarSenhas();
    const interval = setInterval(buscarSenhas, 5000); // Atualiza a cada 5 segundos
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="max-w-6xl mx-auto">
        {/* Header */}
        <div className="bg-white rounded-lg shadow-lg p-6 mb-8">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-2xl font-bold text-gray-800">Painel do Atendente</h1>
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
                disabled={loading}
                className={`px-6 py-2 rounded-lg text-white font-medium ${
                  loading ? 'bg-gray-400' : 'bg-blue-600 hover:bg-blue-700'
                }`}
              >
                {loading ? 'Chamando...' : 'Chamar Próximo'}
              </button>
            </div>
          </div>
        </div>

        {/* Senha Atual */}
        {senhaAtual && (
          <div className="bg-green-50 rounded-lg shadow-lg p-6 mb-8">
            <div className="text-center">
              <h2 className="text-xl font-semibold text-green-800">Em Atendimento</h2>
              <p className="text-4xl font-bold text-green-600 mt-2">{senhaAtual.senha}</p>
              <p className="text-green-700 mt-2">
                {senhaAtual.tipoAtendimento.replace('_', ' ')}
              </p>
              <button
                onClick={() => finalizarAtendimento(senhaAtual.senha)}
                className="mt-4 px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
              >
                Finalizar Atendimento
              </button>
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
                    {ticket.tipoAtendimento.replace('_', ' ')}
                  </p>
                </div>
                <div className="flex gap-2">
                  <span className="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-sm">
                    Aguardando
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