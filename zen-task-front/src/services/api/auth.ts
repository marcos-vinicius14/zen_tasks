import { API_ENDPOINTS } from '../../constants';
import type {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
} from '../../types';
import { apiClient } from './client';

export const authApi = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    return apiClient.post<AuthResponse>(API_ENDPOINTS.AUTH.LOGIN, credentials);
  },

  register: async (userData: RegisterRequest): Promise<AuthResponse> => {
    return apiClient.post<AuthResponse>(API_ENDPOINTS.AUTH.REGISTER, userData);
  },
};