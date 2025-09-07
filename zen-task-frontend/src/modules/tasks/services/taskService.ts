import axios from 'axios'
import { API_CONFIG } from '@config/index'
import { Task, CreateTaskRequest, UpdateTaskRequest, TaskFilters } from '../types'

const api = axios.create({
  baseURL: API_CONFIG.BASE_URL,
  timeout: API_CONFIG.TIMEOUT,
})

// Add auth token to requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('auth-token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const taskService = {
  getTasks: async (filters?: TaskFilters): Promise<Task[]> => {
    const response = await api.get('/tasks', { params: filters })
    return response.data
  },

  getTask: async (id: string): Promise<Task> => {
    const response = await api.get(`/tasks/${id}`)
    return response.data
  },

  createTask: async (data: CreateTaskRequest): Promise<Task> => {
    const response = await api.post('/tasks', data)
    return response.data
  },

  updateTask: async (id: string, data: UpdateTaskRequest): Promise<Task> => {
    const response = await api.put(`/tasks/${id}`, data)
    return response.data
  },

  deleteTask: async (id: string): Promise<void> => {
    await api.delete(`/tasks/${id}`)
  },
}