import { useQuery, useMutation, useQueryClient } from 'react-query'
import { taskService } from '../services'
import { Task, CreateTaskRequest, UpdateTaskRequest, TaskFilters } from '../types'

export const useTasks = (filters?: TaskFilters) => {
  return useQuery(['tasks', filters], () => taskService.getTasks(filters), {
    staleTime: 5 * 60 * 1000, // 5 minutes
  })
}

export const useTask = (id: string) => {
  return useQuery(['task', id], () => taskService.getTask(id), {
    enabled: !!id,
  })
}

export const useCreateTask = () => {
  const queryClient = useQueryClient()

  return useMutation(taskService.createTask, {
    onSuccess: () => {
      queryClient.invalidateQueries(['tasks'])
    },
  })
}

export const useUpdateTask = () => {
  const queryClient = useQueryClient()

  return useMutation(
    ({ id, data }: { id: string; data: UpdateTaskRequest }) =>
      taskService.updateTask(id, data),
    {
      onSuccess: (_, { id }) => {
        queryClient.invalidateQueries(['tasks'])
        queryClient.invalidateQueries(['task', id])
      },
    }
  )
}

export const useDeleteTask = () => {
  const queryClient = useQueryClient()

  return useMutation(taskService.deleteTask, {
    onSuccess: () => {
      queryClient.invalidateQueries(['tasks'])
    },
  })
}