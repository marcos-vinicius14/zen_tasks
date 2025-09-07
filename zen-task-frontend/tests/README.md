# ZenTasks Frontend - Testes

Estrutura de testes do ZenTasks Frontend.

## Tipos de Testes

### Unit Tests (`/unit`)
Testes unitários para componentes, hooks e utilitários isolados.

### Integration Tests (`/integration`)
Testes de integração entre componentes e serviços.

### End-to-End Tests (`/e2e`)
Testes de fluxo completo da aplicação.

## Executando Testes

```bash
# Todos os testes
npm run test

# Com interface gráfica
npm run test:ui

# Com coverage
npm run test:coverage
```

## Estrutura

```
tests/
├── unit/               # Testes unitários
├── integration/        # Testes de integração  
├── e2e/               # Testes end-to-end
└── __mocks__/         # Mocks compartilhados
```