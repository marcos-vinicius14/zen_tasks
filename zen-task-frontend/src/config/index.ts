export const API_CONFIG = {
  BASE_URL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/v1',
  TIMEOUT: 10000,
} as const

export const APP_CONFIG = {
  NAME: 'ZenTasks',
  VERSION: '1.0.0',
  DESCRIPTION: 'Gerenciamento de Tarefas com Matriz de Eisenhower',
} as const

export const AUTH_CONFIG = {
  TOKEN_KEY: 'zen-tasks-token',
  USER_KEY: 'zen-tasks-user',
} as const