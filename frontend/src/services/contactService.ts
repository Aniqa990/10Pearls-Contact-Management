import apiClient from './apiClient';
import type { Contact, CreateContactRequest, UpdateContactRequest, PaginatedResponse } from '@/types';

export const contactService = {
  getAllContacts: async (
    userId: string,
    page: number = 0,
    size: number = 10
  ): Promise<PaginatedResponse<Contact>> => {
    const response = await apiClient.get<PaginatedResponse<Contact>>('/contacts', {
      params: { userId, page, size },
    });
    return response.data;
  },

  searchContacts: async (
    userId: string,
    keyword: string,
    page: number = 0,
    size: number = 10
  ): Promise<PaginatedResponse<Contact>> => {
    const response = await apiClient.get<PaginatedResponse<Contact>>('/contacts/search', {
      params: { userId, keyword, page, size },
    });
    return response.data;
  },

  getContact: async (userId: string, contactId: string): Promise<Contact> => {
    const response = await apiClient.get<Contact>(`/contacts/${userId}/${contactId}`);
    return response.data;
  },

  createContact: async (userId: string, data: CreateContactRequest): Promise<Contact> => {
    const response = await apiClient.post<Contact>('/contacts', data, {
      params: { userId },
    });
    return response.data;
  },

  updateContact: async (
    userId: string,
    contactId: string,
    data: UpdateContactRequest
  ): Promise<Contact> => {
    const response = await apiClient.put<Contact>(`/contacts/${contactId}`, data, {
      params: { userId },
    });
    return response.data;
  },

  deleteContact: async (userId: string, contactId: string): Promise<void> => {
    await apiClient.delete(`/contacts/${contactId}`, {
      params: { userId },
    });
  },

  uploadPhoto: async (userId: string, contactId: string, file: File): Promise<string> => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await apiClient.post<string>(
      `/contacts/${contactId}/photo`,
      formData,
      {
        params: { userId },
        headers: { 'Content-Type': 'multipart/form-data' },
      }
    );
    return response.data;
  },
};
