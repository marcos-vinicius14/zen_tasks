import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { QUERY_KEYS } from '../constants';
import { tasksApi } from '../services/api';
import type { CreateTaskRequest, Task, UpdateTaskRequest } from '../types';

export const useTasks = () => {
  return useQuery<Task[], Error>({
    queryKey: QUERY_KEYS.TASKS,
    queryFn: tasksApi.getTasks,
  });
};

export const useTask = (id: string) => {
  return useQuery<Task, Error>({
    queryKey: QUERY_KEYS.TASK(id),
    queryFn: () => tasksApi.getTask(id),
    enabled: !!id,
  });
};

export const useCreateTask = () => {
  const queryClient = useQueryClient();

  return useMutation<Task, Error, CreateTaskRequest>({
    mutationFn: tasksApi.createTask,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.TASKS });
    },
  });
};

export const useUpdateTask = () => {
  const queryClient = useQueryClient();

  return useMutation<Task, Error, { id: string; data: UpdateTaskRequest }>({
    mutationFn: ({ id, data }) => tasksApi.updateTask(id, data),
    onSuccess: (_, { id }) => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.TASKS });
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.TASK(id) });
    },
  });
};

export const useDeleteTask = () => {
  const queryClient = useQueryClient();

  return useMutation<void, Error, string>({
    mutationFn: tasksApi.deleteTask,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.TASKS });
    },
  });
};