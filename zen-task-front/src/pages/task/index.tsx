import React from 'react';
import { useTasks } from '../../hooks/useTasks';

export const TasksPage: React.FC = () => {
  const { data: tasks, isLoading, error } = useTasks();

  if (isLoading) {
    return <div className="p-5">Loading tasks...</div>;
  }

  if (error) {
    return (
      <div className="p-5 text-red-500">
        Error loading tasks: {error.message}
      </div>
    );
  }

  return (
    <div className="p-5">
      <h1>Tasks</h1>
      {tasks && tasks.length > 0 ? (
        <ul>
          {tasks.map((task) => (
            <li key={task.id} className="mb-2.5">
              <h3>{task.title}</h3>
              <p>{task.description}</p>
              <p>Status: {task.status}</p>
              {task.isUrgent && <span className="text-red-500">Urgent </span>}
              {task.isImportant && <span className="text-blue-500">Important </span>}
            </li>
          ))}
        </ul>
      ) : (
        <p>No tasks found</p>
      )}
    </div>
  );
};
