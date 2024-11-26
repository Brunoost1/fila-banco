import Link from 'next/link';

export default function Home() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50">
      <div className="text-center space-y-6 p-8">
        <h1 className="text-4xl font-bold text-gray-900">Sistema de Fila Bancária</h1>
        <p className="text-xl text-gray-600">
          Bem-vindo ao sistema de gerenciamento de filas
        </p>
        
        <div className="flex gap-4 justify-center pt-4">
          <Link 
            href="/login" 
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors"
          >
            Login
          </Link>
          <Link 
            href="/cadastro" 
            className="bg-green-600 text-white px-6 py-3 rounded-lg hover:bg-green-700 transition-colors"
          >
            Criar Conta
          </Link>
          <Link 
            href="/painel" 
            className="bg-gray-200 text-gray-800 px-6 py-3 rounded-lg hover:bg-gray-300 transition-colors"
          >
            Ver Painel
          </Link>
        </div>
      </div>
    </div>
  );
}