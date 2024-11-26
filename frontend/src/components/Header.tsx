'use client';

import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';

export function Header() {
  const { isAuthenticated, logout } = useAuth();

  return (
    <header className="bg-white shadow">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          <Link href="/" className="text-xl font-bold">
            Fila Banco
          </Link>
          
          <nav className="space-x-4">
            {isAuthenticated ? (
              <>
                <Link href="/dashboard" className="hover:text-blue-600">
                  Dashboard
                </Link>
                <Link href="/painel" className="hover:text-blue-600">
                  Painel
                </Link>
                <button
                  onClick={logout}
                  className="text-red-600 hover:text-red-700"
                >
                  Sair
                </button>
              </>
            ) : (
              <Link href="/login" className="hover:text-blue-600">
                Login
              </Link>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
}