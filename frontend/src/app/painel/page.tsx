'use client';

import PainelSenhas from '@/components/PainelSenhas';

export default function Painel() {
  return (
    <div className="min-h-screen bg-gray-100">
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold text-center mb-8">Painel de Senhas</h1>
        <PainelSenhas />
      </div>
    </div>
  );
}