import type { User } from '../../types';

const TOKEN_KEY = 'authToken';
const USER_KEY = 'authUser';

export const authService = {
  getToken: (): string | null => {
    return localStorage.getItem(TOKEN_KEY);
  },

  setToken: (token: string): void => {
    localStorage.setItem(TOKEN_KEY, token);
  },

  removeToken: (): void => {
    localStorage.removeItem(TOKEN_KEY);
  },

  getUser: (): User | null => {
    const userStr = localStorage.getItem(USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  },

  setUser: (user: User): void => {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  },

  removeUser: (): void => {
    localStorage.removeItem(USER_KEY);
  },

  isAuthenticated: (): boolean => {
    return !!authService.getToken();
  },

  logout: (): void => {
    authService.removeToken();
    authService.removeUser();
  },
};