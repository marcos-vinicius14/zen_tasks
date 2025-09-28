import { authApi } from '../api';


export const authService = {
  useLogout: async(): Promise<void> => {
    await authApi.logout();
  },

  checkStatus: async(): Promise<void> => {
    return authApi.status();
  }
};