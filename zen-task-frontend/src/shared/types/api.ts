// Common types used across the application

export interface ApiResponse<T> {
  data: T
  message?: string
  status: number
}

export interface PaginatedResponse<T> {
  data: T[]
  total: number
  page: number
  totalPages: number
}

export interface ApiError {
  message: string
  code?: string
  status: number
  details?: Record<string, unknown>
}