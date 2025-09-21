import React from 'react';
import { useTasks } from '../../hooks/useTasks';

export const TasksPage: React.FC = () => {
  const { data: tasks, isLoading, error } = useTasks();

  if (isLoading) {
    return <div style={{ padding: '20px' }}>Loading tasks...</div>;
  }

  if (error) {
    return (
      <div style={{ padding: '20px', color: 'red' }}>
        Error loading tasks: {error.message}
      </div>
    );
  }

  return (
    <div style={{ padding: '20px' }}>
      <h1>Tasks</h1>
      {tasks && tasks.length > 0 ? (
        <ul>
          {tasks.map((task) => (
            <li key={task.id} style={{ marginBottom: '10px' }}>
              <h3>{task.title}</h3>
              <p>{task.description}</p>
              <p>Status: {task.status}</p>
              {task.isUrgent && <span style={{ color: 'red' }}>Urgent </span>}
              {task.isImportant && <span style={{ color: 'blue' }}>Important </span>}
            </li>
          ))}
        </ul>
      ) : (
        <p>No tasks found</p>
      )}
    </div>
  );
};