import { API_ENDPOINTS } from '../../constants';
import type {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
} from '../../types';
import { apiClient } from './client';

export const authApi = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    return apiClient.post<AuthResponse>(API_ENDPOINTS.AUTH.LOGIN, credentials, { authRequired: false });
  },

  register: async (userData: RegisterRequest): Promise<AuthResponse> => {
    return apiClient.post<AuthResponse>(API_ENDPOINTS.AUTH.REGISTER, userData, { authRequired: false });
  },

  logout: async():  Promise<void> => {
    return apiClient.post(API_ENDPOINTS.AUTH.LOGOUT);
  },

  status: async(): Promise<void> => {
    return apiClient.get(API_ENDPOINTS.AUTH.STATUS);
  }
};