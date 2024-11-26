'use client';

import { useEffect, useState } from 'react';

type Senha = {
  senha: string;
  guiche: number;
  status: string;
};

export default function PainelSenhas() {
  const [senhaAtual, setSenhaAtual] = useState<Senha | null>(null);
  const [ultimasSenhas, setUltimasSenhas] = useState<Senha[]>([]);

  useEffect(() => {
    const ws = new WebSocket('ws://localhost:8080/ws');
    
    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setSenhaAtual(data);
      setUltimasSenhas(prev => [data, ...prev].slice(0, 5));
    };

    return () => ws.close();
  }, []);

  return (
    <div className="grid grid-cols-2 gap-4 p-6">
      <div className="col-span-2 bg-blue-600 text-white p-8 rounded-lg text-center">
        <h2 className="text-2xl mb-4">Senha Atual</h2>
        <div className="text-6xl font-bold">{senhaAtual?.senha || '-'}</div>
        <div className="text-xl mt-2">Guichê: {senhaAtual?.guiche || '-'}</div>
      </div>

      <div className="col-span-2">
        <h3 className="text-xl mb-4">Últimas Senhas</h3>
        <div className="space-y-2">
          {ultimasSenhas.map((senha, index) => (
            <div key={index} className="bg-gray-100 p-4 rounded">
              Senha {senha.senha} - Guichê {senha.guiche}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}