# ZenTasks Frontend

Frontend moderno para o sistema ZenTasks - uma aplicação de gerenciamento de tarefas com foco na Matriz de Eisenhower para priorização eficiente.

## 📋 Visão Geral

O ZenTasks Frontend é uma aplicação React + TypeScript que oferece uma interface intuitiva para gerenciar tarefas usando os princípios da Matriz de Eisenhower. A aplicação segue uma arquitetura modular limpa, espelhando os padrões arquiteturais do backend.

## 🏗️ Arquitetura

### Estrutura Modular

A aplicação segue uma arquitetura modular similar ao backend, com separação clara de responsabilidades:

```
src/
├── modules/                 # Módulos de negócio
│   ├── users/              # Módulo de usuários
│   │   ├── components/     # Componentes específicos de usuários
│   │   ├── services/       # Serviços para comunicação com API
│   │   ├── types/          # Tipos TypeScript
│   │   └── hooks/          # Hooks customizados
│   └── tasks/              # Módulo de tarefas
│       ├── components/     # Componentes específicos de tarefas
│       ├── services/       # Serviços para comunicação com API
│       ├── types/          # Tipos TypeScript
│       └── hooks/          # Hooks customizados
├── shared/                 # Recursos compartilhados
│   ├── components/         # Componentes reutilizáveis
│   ├── services/           # Serviços compartilhados
│   ├── utils/              # Utilitários
│   └── types/              # Tipos compartilhados
├── assets/                 # Recursos estáticos
│   ├── images/             # Imagens
│   └── styles/             # Estilos CSS
└── config/                 # Configurações da aplicação
```

### Princípios Arquiteturais

- **Modularidade**: Cada módulo de negócio é independente e auto-contido
- **Separação de Responsabilidades**: Components, services, hooks e types bem definidos
- **Clean Architecture**: Dependências fluem para dentro, domínio independente
- **TypeScript First**: Tipagem forte em toda a aplicação

## 🚀 Tecnologias

### Core
- **React 18** - Biblioteca para interfaces de usuário
- **TypeScript** - Tipagem estática para JavaScript
- **Vite** - Build tool moderna e rápida

### Roteamento e Estado
- **React Router Dom** - Roteamento SPA
- **Zustand** - Gerenciamento de estado leve
- **React Query** - Cache e sincronização de dados

### Formulários e Validação
- **React Hook Form** - Formulários performáticos
- **Zod** - Validação de esquemas TypeScript-first

### HTTP e Utilitários
- **Axios** - Cliente HTTP
- **date-fns** - Manipulação de datas
- **clsx** - Utilitário para classes CSS condicionais

### Desenvolvimento
- **ESLint** - Linting de código
- **Prettier** - Formatação de código
- **Vitest** - Framework de testes
- **Testing Library** - Utilitários para testes

## 📦 Funcionalidades

### Gerenciamento de Usuários
- ✅ Registro de novos usuários
- ✅ Autenticação (login/logout)
- ✅ Proteção de rotas
- 🚧 Perfil do usuário
- 🚧 Alteração de senha

### Gerenciamento de Tarefas
- 🚧 Criação de tarefas
- 🚧 Listagem e filtros
- 🚧 Edição de tarefas
- 🚧 Exclusão de tarefas
- 🚧 Alteração de status

### Matriz de Eisenhower
- ✅ Visualização em quadrantes
- 🚧 Classificação automática por urgência/importância
- 🚧 Drag & drop entre quadrantes
- 🚧 Métricas e insights

**Legenda**: ✅ Estrutura criada | 🚧 A implementar

## 🛠️ Setup do Projeto

### Pré-requisitos

- Node.js >= 18.0.0
- npm >= 9.0.0

### Instalação

1. **Clone o repositório**:
   ```bash
   git clone <url-do-repositorio>
   cd zen-task-frontend
   ```

2. **Instale as dependências**:
   ```bash
   npm install
   ```

3. **Configure as variáveis de ambiente**:
   ```bash
   cp .env.example .env
   ```
   
   Edite o arquivo `.env` com as configurações:
   ```env
   VITE_API_BASE_URL=http://localhost:8080/v1
   ```

### Execução

```bash
# Desenvolvimento
npm run dev

# Build para produção
npm run build

# Preview da build
npm run preview
```

A aplicação estará disponível em `http://localhost:3000`.

## 🧪 Testes

```bash
# Executar testes
npm run test

# Testes com interface
npm run test:ui

# Cobertura de testes
npm run test:coverage
```

## 🔧 Scripts Disponíveis

- `npm run dev` - Inicia servidor de desenvolvimento
- `npm run build` - Build para produção
- `npm run preview` - Preview da build de produção
- `npm run lint` - Executa ESLint
- `npm run lint:fix` - Corrige problemas do ESLint automaticamente
- `npm run type-check` - Verifica tipos TypeScript
- `npm run format` - Formata código com Prettier
- `npm run format:check` - Verifica formatação
- `npm run test` - Executa testes com Vitest
- `npm run test:ui` - Interface gráfica para testes
- `npm run test:coverage` - Relatório de cobertura

## 🌐 Integração com Backend

O frontend está configurado para se conectar com a ZenTasks API:

- **Base URL**: Configurável via `VITE_API_BASE_URL`
- **Proxy**: Vite proxy configurado para `/api` → `http://localhost:8080/v1`
- **Autenticação**: Bearer token via localStorage
- **Interceptors**: Axios configurado para adicionar token automaticamente

### Endpoints Utilizados

```typescript
// Autenticação
POST /v1/login
POST /v1/register

// Tarefas
GET /v1/tasks
POST /v1/tasks
PUT /v1/tasks/:id
DELETE /v1/tasks/:id
```

## 📁 Estrutura Detalhada

```
zen-task-frontend/
├── public/                     # Arquivos públicos
├── src/
│   ├── modules/
│   │   ├── users/
│   │   │   ├── components/
│   │   │   │   ├── LoginPage.tsx
│   │   │   │   ├── RegisterPage.tsx
│   │   │   │   └── index.ts
│   │   │   ├── services/
│   │   │   │   ├── authService.ts
│   │   │   │   └── index.ts
│   │   │   ├── types/
│   │   │   │   └── index.ts
│   │   │   └── hooks/
│   │   │       ├── useAuth.tsx
│   │   │       └── index.ts
│   │   └── tasks/
│   │       ├── components/
│   │       │   ├── TasksPage.tsx
│   │       │   ├── EisenhowerMatrixPage.tsx
│   │       │   └── index.ts
│   │       ├── services/
│   │       │   ├── taskService.ts
│   │       │   └── index.ts
│   │       ├── types/
│   │       │   └── index.ts
│   │       └── hooks/
│   │           ├── useTasks.ts
│   │           └── index.ts
│   ├── shared/
│   │   ├── components/
│   │   │   ├── Layout.tsx
│   │   │   ├── ProtectedRoute.tsx
│   │   │   ├── HomePage.tsx
│   │   │   └── index.ts
│   │   ├── utils/
│   │   │   ├── dateUtils.ts
│   │   │   ├── stringUtils.ts
│   │   │   └── index.ts
│   │   └── types/
│   │       ├── api.ts
│   │       └── index.ts
│   ├── assets/
│   │   └── styles/
│   │       └── globals.css
│   ├── config/
│   │   └── index.ts
│   ├── App.tsx
│   ├── main.tsx
│   └── setupTests.ts
├── tests/                      # Diretório de testes
├── docs/                       # Documentação
├── package.json
├── vite.config.ts
├── tsconfig.json
├── eslint.config.js
├── .prettierrc
├── .gitignore
└── README.md
```

## 🎯 Próximos Passos

1. **Implementar funcionalidades core**:
   - Formulários de login/registro funcionais
   - CRUD completo de tarefas
   - Drag & drop na matriz

2. **Melhorias de UX/UI**:
   - Design system consistente
   - Loading states
   - Error boundaries
   - Feedback visual

3. **Funcionalidades avançadas**:
   - Filtros avançados
   - Busca inteligente
   - Notificações
   - Métricas e relatórios

4. **Otimizações**:
   - Code splitting
   - Lazy loading
   - Service Worker (PWA)
   - Otimização de bundle

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob licença MIT. Veja o arquivo [LICENSE](../LICENSE) para mais detalhes.

---

**ZenTasks Frontend** - Desenvolvido com ❤️ para produtividade máxima