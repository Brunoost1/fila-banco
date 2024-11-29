Fila Banco - Sistema de Gerenciamento de Filas
Este projeto é um sistema de gerenciamento de filas para bancos, permitindo o controle de senhas e atendimentos em tempo real.
🚀 Tecnologias Utilizadas

Java Spring Boot
MySQL (Azure Database)
RabbitMQ
Docker
Next.js (Frontend)

📋 Pré-requisitos
Antes de começar, você precisa ter instalado em sua máquina:

Git
Docker Desktop
Java 17 ou superior (para desenvolvimento)
Node.js (para desenvolvimento frontend)

🔧 Instalação e Configuração
1. Clone o repositório
bashCopygit clone (https://github.com/Brunoost1/fila-banco/)
cd fila-banco
2. Configuração das Variáveis de Ambiente
Crie um arquivo .env na raiz do projeto com as seguintes variáveis:
envCopyMYSQL_URL=// SOLICITAR AO ADM
MYSQL_USER= (SOLICITAR AO ADM)
MYSQL_PASSWORD=(SOLICITAR AO ADM)
3. Iniciando a Aplicação com Docker
bashCopy# Parar containers anteriores (se existirem)
docker-compose down -v

# Iniciar os containers
docker-compose up -d
4. Verificando a Instalação
Após a inicialização, você deve ter acesso a:

Aplicação: http://localhost:8080
RabbitMQ Management: http://localhost:15672

Usuário: guest
Senha: guest



📦 Estrutura do Projeto
Copyfila-banco/
├── backend/            # Código Java Spring Boot
├── frontend/           # Código Next.js
├── docker-compose.yml  # Configuração Docker
└── README.md          # Esta documentação
🔄 Endpoints Principais
Gerenciamento de Senhas

POST /api/senhas/gerar - Gera nova senha
GET /api/senhas/proxima - Chama próxima senha
GET /api/senhas/status - Status das senhas

👥 Acessos e Permissões
Banco de Dados (Azure)

Host: SOLICITAR AO ADM
Porta: 3306
Banco: a3_java
Usuário: [Solicitar ao administrador]
Senha: [Solicitar ao administrador]

RabbitMQ

Host: localhost
Porta: 5672 (AMQP)
Porta Admin: 15672
Usuário: guest
Senha: guest

🛠️ Desenvolvimento
Para desenvolver localmente:

Frontend:

bashCopycd frontend
npm install
npm run dev

Backend (usando IDE):


Abra o projeto em sua IDE preferida
Configure as variáveis de ambiente
Execute a classe principal FilaBancoApplication

🔍 Monitoramento

Logs da aplicação: docker logs app-fila-banco
Logs do RabbitMQ: docker logs rabbitmq-fila-banco

⚠️ Troubleshooting
Problemas Comuns

Erro de conexão com banco:

Verifique as credenciais no .env
Confirme se o IP está liberado no Azure


Erro no RabbitMQ:

Verifique se o container está rodando: docker ps
Reinicie o container se necessário



📱 Contato e Suporte
Para suporte ou dúvidas:

Email: bruno.santos421@gmail.com
Issues: Criar uma issue no GitHub

🔐 Segurança

Não compartilhe credenciais em repositórios públicos
Mantenha o Docker Desktop atualizado
Use sempre HTTPS em produção


Desenvolvido por :

Bruno Santos Teixeira - RA: 12522178550 
João Vitor Veras - RA: 1252219434 
Leonardo Affonso de Freitas - RA: 12522175405
Rafael Marcelino -  RA: 12522196076
