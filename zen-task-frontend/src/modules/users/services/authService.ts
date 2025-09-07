import axios from 'axios'
import { API_CONFIG } from '@config/index'
import {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
} from '../types'

const api = axios.create({
  baseURL: API_CONFIG.BASE_URL,
  timeout: API_CONFIG.TIMEOUT,
})

export const authService = {
  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await api.post('/login', data)
    return response.data
  },

  register: async (data: RegisterRequest): Promise<RegisterResponse> => {
    const response = await api.post('/register', data)
    return response.data
  },

  logout: () => {
    // Clear any stored tokens/user data
    localStorage.removeItem('auth-token')
    localStorage.removeItem('user-data')
  },
}