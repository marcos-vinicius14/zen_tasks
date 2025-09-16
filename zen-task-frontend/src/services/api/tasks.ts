import { apiClient } from './client';
import { API_ENDPOINTS } from '../../constants';
import { Task, CreateTaskRequest, UpdateTaskRequest } from '../../types';

export const tasksApi = {
  getTasks: async (): Promise<Task[]> => {
    return apiClient.get<Task[]>(API_ENDPOINTS.TASKS.BASE);
  },

  getTask: async (id: string): Promise<Task> => {
    return apiClient.get<Task>(API_ENDPOINTS.TASKS.BY_ID(id));
  },

  createTask: async (taskData: CreateTaskRequest): Promise<Task> => {
    return apiClient.post<Task>(API_ENDPOINTS.TASKS.BASE, taskData);
  },

  updateTask: async (
    id: string,
    taskData: UpdateTaskRequest
  ): Promise<Task> => {
    return apiClient.put<Task>(API_ENDPOINTS.TASKS.BY_ID(id), taskData);
  },

  deleteTask: async (id: string): Promise<void> => {
    return apiClient.delete<void>(API_ENDPOINTS.TASKS.BY_ID(id));
  },
};
