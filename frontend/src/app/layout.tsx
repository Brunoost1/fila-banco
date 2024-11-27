import { AuthProvider } from "@/contexts/AuthContext";
import { Header } from "@/components/Header";
import "./globals.css";
import { Inter } from "next/font/google";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
 title: "Fila Banco",
 description: "Sistema de gerenciamento de filas bancárias",
};

export default function RootLayout({
 children,
}: {
 children: React.ReactNode;
}) {
 return (
   <html lang="pt-BR">
     <body className={inter.className}>
       <AuthProvider>
         <Header />
         <main>{children}</main>
       </AuthProvider>
     </body>
   </html>
 );
}