export enum UserRole {
  USER = 'USER',
  ADMIN = 'ADMIN',
}

export interface User {
  id: string
  username: string
  email: string
  role: UserRole
  createdAt: string
  updatedAt: string
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
  role: UserRole
}

export interface RegisterResponse {
  username: string
  email: string
  role: UserRole
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: User
}

export interface ChangePasswordRequest {
  currentPassword: string
  newPassword: string
}