import React from 'react';
import { Link } from 'react-router-dom';

export const HomePage: React.FC = () => {
  return (
    <div className="p-5 text-center">
      <h1>Welcome to ZenTasks</h1>
      <p>Manage your tasks with the Eisenhower Matrix</p>
      <div className="mt-5">
        <Link to="/login" className="mr-2.5">
          Login
        </Link>
        <Link to="/tasks">
          Tasks
        </Link>
      </div>
    </div>
  );
};
