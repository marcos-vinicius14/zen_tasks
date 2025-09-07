export enum TaskStatus {
  CREATED = 'CREATED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
}

export enum TaskQuadrant {
  DO_NOW = 'DO_NOW', // Urgente e Importante
  SCHEDULE = 'SCHEDULE', // Importante mas não Urgente
  DELEGATE = 'DELEGATE', // Urgente mas não Importante
  ELIMINATE = 'ELIMINATE', // Nem Urgente nem Importante
}

export interface Task {
  id: string
  title: string
  description: string
  dueDate: string
  isUrgent: boolean
  isImportant: boolean
  taskStatus: TaskStatus
  quadrant: TaskQuadrant
  createdAt: string
  updatedAt: string
  userId: string
}

export interface CreateTaskRequest {
  title: string
  description: string
  dueDate: string
  isUrgent: boolean
  isImportant: boolean
}

export interface UpdateTaskRequest {
  title?: string
  description?: string
  dueDate?: string
  isUrgent?: boolean
  isImportant?: boolean
  taskStatus?: TaskStatus
}

export interface TaskFilters {
  status?: TaskStatus
  quadrant?: TaskQuadrant
  isUrgent?: boolean
  isImportant?: boolean
  search?: string
}