import { useMutation, useQueryClient } from '@tanstack/react-query';
import { authApi } from '../services/api';
import { authService } from '../services/auth';
import { LoginRequest, RegisterRequest, AuthResponse } from '../types';
import { QUERY_KEYS } from '../constants';

export const useLogin = () => {
  const queryClient = useQueryClient();

  return useMutation<AuthResponse, Error, LoginRequest>({
    mutationFn: authApi.login,
    onSuccess: (data) => {
      authService.setToken(data.token);
      authService.setUser(data.user);
      queryClient.setQueryData(QUERY_KEYS.USER, data.user);
    },
  });
};

export const useRegister = () => {
  const queryClient = useQueryClient();

  return useMutation<AuthResponse, Error, RegisterRequest>({
    mutationFn: authApi.register,
    onSuccess: (data) => {
      authService.setToken(data.token);
      authService.setUser(data.user);
      queryClient.setQueryData(QUERY_KEYS.USER, data.user);
    },
  });
};

export const useLogout = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async () => {
      authService.logout();
    },
    onSuccess: () => {
      queryClient.clear();
    },
  });
};