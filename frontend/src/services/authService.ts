import apiClient from './apiClient';
import type { LoginRequest, RegisterRequest, AuthResponse, ChangePasswordRequest } from '@/types';

export const authService = {
  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/register', data);
    return response.data;
  },

  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/login', data);
    return response.data;
  },

  logout: async (): Promise<void> => {
    await apiClient.post('/users/logout');
  },

  changePassword: async (userId: string, data: ChangePasswordRequest): Promise<void> => {
    await apiClient.post(`/users/${userId}/change-password`, null, {
      params: {
        oldPassword: data.oldPassword,
        newPassword: data.newPassword,
      },
    });
  },
};