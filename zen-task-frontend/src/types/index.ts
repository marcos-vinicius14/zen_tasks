export * from './auth';
export * from './task';

export interface ApiErrorResponse {
  message: string;
  status: number;
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
}
