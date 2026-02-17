
export interface User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  token: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  token: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}

//contact Types
export interface Email {
  id?: string;
  value: string;
  type: string;
}

export interface Phone {
  id?: string;
  value: string;
  type: string;
}

export interface Contact {
  id: string;
  firstName: string;
  lastName: string;
  emailAddresses: Array<{ id?: string; email: string; type: string }>;
  phoneNumbers: Array<{ id?: string; number: string; type: string }>;
  title?: string;
  photoUrl?: string;
  created_at?: string;
  updated_at?: string;
}

export interface CreateContactRequest {
  firstName: string;
  lastName: string;
  emails?: Array<{ email: string; type: string }>;
  phones?: Array<{ number: string; type: string }>;
  title?: string;
  photoUrl?: string;
}

export interface UpdateContactRequest extends CreateContactRequest {}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface PaginationParams {
  page: number;
  pageSize: number;
}
