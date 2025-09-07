export interface Task {
  id: string;
  title: string;
  description: string;
  status: TaskStatus;
  isUrgent?: boolean;
  isImportant?: boolean;
  dueDate?: string;
  createdAt: string;
  updatedAt: string;
}

export enum TaskStatus {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE',
}

export enum Quadrant {
  DO_FIRST = 'DO_FIRST', // Urgent & Important
  SCHEDULE = 'SCHEDULE', // Important & Not Urgent
  DELEGATE = 'DELEGATE', // Urgent & Not Important
  ELIMINATE = 'ELIMINATE', // Not Urgent & Not Important
}

export interface CreateTaskRequest {
  title: string;
  description: string;
  isUrgent: boolean;
  isImportant: boolean;
  dueDate?: string;
}

export interface UpdateTaskRequest {
  title?: string;
  description?: string;
  isUrgent?: boolean;
  isImportant?: boolean;
  dueDate?: string;
  isCompleted?: boolean;
}