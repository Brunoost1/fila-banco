'use client';

import { useState } from 'react';
import api from '@/lib/api';

export default function GerarSenha() {
  const [tipoAtendimento, setTipoAtendimento] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleGerarSenha = async () => {
    if (!tipoAtendimento) {
      setError('Selecione um tipo de atendimento');
      return;
    }

    try {
      setLoading(true);
      setError('');
      
      const clienteId = localStorage.getItem('clienteId');
      if (!clienteId) {
        throw new Error('Cliente não identificado');
      }

      console.log('Enviando requisição:', {
        clienteId,
        tipoAtendimento
      });

      const response = await api.post('/atendimento/senha', {
        clienteId: parseInt(clienteId),
        tipoAtendimento
      });

      console.log('Resposta:', response.data);
      
      alert(`Sua senha é: ${response.data.senha}`);
    } catch (error: any) {
      console.error('Erro:', error.response?.data || error.message);
      setError(error.response?.data?.message || 'Erro ao gerar senha');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow p-6 text-black">
      <h2 className="text-2xl font-bold mb-4">Gerar Nova Senha</h2>
      
      {error && (
        <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      )}

      <div className="mb-4 text-black">
        <select
          value={tipoAtendimento}
          onChange={(e) => setTipoAtendimento(e.target.value)}
          className="w-full p-2 border rounded focus:ring-2 focus:ring-blue-500"
        >
          <option value="">Selecione o tipo de atendimento</option>
          <option value="ABERTURA_CONTA">Abertura de Conta</option>
          <option value="EMPRESTIMO">Empréstimo</option>
          <option value="OUTROS">Outros Serviços</option>
        </select>
      </div>

      <button
        onClick={handleGerarSenha}
        disabled={loading}
        className={`w-full py-2 px-4 rounded-lg text-white font-medium ${
          loading 
            ? 'bg-blue-400 cursor-not-allowed' 
            : 'bg-blue-600 hover:bg-blue-700'
        }`}
      >
        {loading ? 'Gerando...' : 'Gerar Senha'}
      </button>
    </div>
  );
}