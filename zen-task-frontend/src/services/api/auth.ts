import { apiClient } from './client';
import { API_ENDPOINTS } from '../../constants';
import {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
} from '../../types';

export const authApi = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    return apiClient.post<AuthResponse>(API_ENDPOINTS.AUTH.LOGIN, credentials);
  },

  register: async (userData: RegisterRequest): Promise<AuthResponse> => {
    return apiClient.post<AuthResponse>(API_ENDPOINTS.AUTH.REGISTER, userData);
  },
};