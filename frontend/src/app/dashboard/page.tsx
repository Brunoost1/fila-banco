'use client';

import { useState } from 'react';
import api from '@/lib/api';

export default function Dashboard() {
  const [tipoAtendimento, setTipoAtendimento] = useState('');
  const [loading, setLoading] = useState(false);
  const [senhaAtual, setSenhaAtual] = useState('');
  const [statusSenha, setStatusSenha] = useState('');

  const handleGerarSenha = async () => {
    try {
      setLoading(true);
      const clienteId = localStorage.getItem('clienteId');
      const response = await api.post('/atendimento/senha', {
        clienteId: parseInt(clienteId || '0'),
        tipoAtendimento
      });
      setSenhaAtual(response.data.senha);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="container mx-auto px-4 py-6">
          <h1 className="text-3xl font-bold">Sistema de Atendimento</h1>
        </div>
      </div>

      <div className="container mx-auto px-4 py-8">
        <div className="grid md:grid-cols-2 gap-8">
          {/* Seção Gerar Senha */}
          <div className="bg-white rounded-xl shadow-lg p-6 space-y-6">
            <div className="flex items-center space-x-4">
              <div className="bg-blue-100 p-3 rounded-full">
                <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
                </svg>
              </div>
              <h2 className="text-2xl font-bold text-gray-800">Gerar Nova Senha</h2>
            </div>

            <div className="space-y-4">
              <select
                value={tipoAtendimento}
                onChange={(e) => setTipoAtendimento(e.target.value)}
                className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              >
                <option value="">Selecione o tipo de atendimento</option>
                <option value="ABERTURA_CONTA">Abertura de Conta</option>
                <option value="EMPRESTIMO">Empréstimo</option>
                <option value="OUTROS">Outros Serviços</option>
              </select>

              <button
                onClick={handleGerarSenha}
                disabled={loading || !tipoAtendimento}
                className={`w-full py-3 px-4 rounded-lg text-white font-medium transition-all
                  ${loading || !tipoAtendimento
                    ? 'bg-gray-400 cursor-not-allowed'
                    : 'bg-blue-600 hover:bg-blue-700 shadow-lg hover:shadow-xl'
                  }`}
              >
                {loading ? 'Gerando...' : 'Gerar Senha'}
              </button>
            </div>

            {senhaAtual && (
              <div className="mt-6 p-4 bg-green-50 border border-green-200 rounded-lg">
                <div className="text-center">
                  <p className="text-sm text-green-600 font-medium">Sua senha é</p>
                  <p className="text-4xl font-bold text-green-700 mt-2">{senhaAtual}</p>
                </div>
              </div>
            )}
          </div>

          {/* Seção Status */}
          <div className="bg-white rounded-xl shadow-lg p-6 space-y-6">
            <div className="flex items-center space-x-4">
              <div className="bg-purple-100 p-3 rounded-full">
                <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                </svg>
              </div>
              <h2 className="text-2xl font-bold text-gray-800">Status do Atendimento</h2>
            </div>

            <div className="space-y-4">
              <input
                type="text"
                placeholder="Digite sua senha"
                className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                onChange={(e) => setStatusSenha(e.target.value)}
              />

              <button
                className="w-full py-3 px-4 rounded-lg text-white font-medium bg-purple-600 hover:bg-purple-700 transition-all shadow-lg hover:shadow-xl"
                onClick={() => {/* Implementar consulta de status */}}
              >
                Consultar Status
              </button>
            </div>

            {statusSenha && (
              <div className="mt-6 p-4 bg-gray-50 border border-gray-200 rounded-lg">
                <div className="text-center">
                  <p className="text-sm text-gray-600 font-medium">Status atual</p>
                  <p className="text-xl font-medium text-gray-800 mt-2">Aguardando atendimento</p>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}