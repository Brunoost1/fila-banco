'use client';

import { useEffect, useState } from 'react';
import api from '@/lib/api';

type Ticket = {
  senha: string;
  status: string;
  guiche: number;
  tempoEstimado: number;
};

export default function StatusAtendimento() {
  const [ticket, setTicket] = useState<Ticket | null>(null);

  const consultarStatus = async (senha: string) => {
    try {
      const response = await api.get(`/atendimento/status/${senha}`);
      setTicket(response.data);
    } catch (error) {
      alert('Erro ao consultar status');
    }
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow">
      <div className="mb-4">
        <input
          type="text"
          placeholder="Digite sua senha"
          className="w-full p-2 border rounded"
          onBlur={(e) => e.target.value && consultarStatus(e.target.value)}
        />
      </div>

      {ticket && (
        <div className="space-y-2">
          <div className="p-4 bg-gray-50 rounded">
            <p className="font-medium">Senha: {ticket.senha}</p>
            <p>Status: {ticket.status}</p>
            {ticket.guiche && <p>Guichê: {ticket.guiche}</p>}
            {ticket.tempoEstimado && (
              <p>Tempo estimado: {ticket.tempoEstimado} minutos</p>
            )}
          </div>
        </div>
      )}
    </div>
  );
}