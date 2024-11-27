'use client';

import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';

export function Header() {
  const { isAuthenticated } = useAuth();

  return (
    <header className="bg-white shadow-lg">
      <nav className="container mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <Link href="/" className="text-xl font-bold text-blue-600">
            Fila Banco
          </Link>
          
          <div className="flex gap-4">
            {isAuthenticated ? (
              <>
                <Link 
                  href="/dashboard" 
                  className="text-gray-600 hover:text-blue-600"
                >
                  Dashboard
                </Link>
                <Link 
                  href="/atendente" 
                  className="text-gray-600 hover:text-blue-600"
                >
                  Atendente
                </Link>
                <Link 
                  href="/painel" 
                  className="text-gray-600 hover:text-blue-600"
                >
                  Painel
                </Link>
              </>
            ) : (
              <>
                <Link 
                  href="/login" 
                  className="text-gray-600 hover:text-blue-600"
                >
                  Login
                </Link>
                <Link 
                  href="/cadastro" 
                  className="text-gray-600 hover:text-blue-600"
                >
                  Cadastro
                </Link>
              </>
            )}
          </div>
        </div>
      </nav>
    </header>
  );
}