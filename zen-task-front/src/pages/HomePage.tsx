import React from 'react';
import { Link } from 'react-router-dom';

export const HomePage: React.FC = () => {
  return (
    <div style={{ padding: '20px', textAlign: 'center' }}>
      <h1>Welcome to ZenTasks</h1>
      <p>Manage your tasks with the Eisenhower Matrix</p>
      <div style={{ marginTop: '20px' }}>
        <Link to="/login" style={{ marginRight: '10px' }}>
          Login
        </Link>
        <Link to="/tasks">
          Tasks
        </Link>
      </div>
    </div>
  );
};