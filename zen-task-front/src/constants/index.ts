export const API_BASE_URL = import.meta.env.VITE_API_URL;

export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/v1/login',
    REGISTER: '/v1/register',
  },
  TASKS: {
    BASE: '/v1/tasks',
    BY_ID: (id: string) => `/v1/tasks/${id}`,
  },
} as const;

export const QUERY_KEYS = {
  TASKS: ['tasks'],
  TASK: (id: string) => ['task', id],
  USER: ['user'],
} as const;

export const ROUTES = {
  HOME: '/',
  LOGIN: '/login',
  REGISTER: '/register',
  TASKS: '/tasks',
  TASK: (id: string) => `/tasks/${id}`,
} as const;