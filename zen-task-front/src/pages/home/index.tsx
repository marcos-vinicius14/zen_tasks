import React from 'react';
import { Link } from 'react-router-dom';
import * as stylex from '@stylexjs/stylex';
import { styles } from './style';  

export const HomePage: React.FC = () => {
  return (
    <div {...stylex.props(styles.container)}>
      <h1>Welcome to ZenTasks</h1>
      <p>Manage your tasks with the Eisenhower Matrix</p>
      <div {...stylex.props(styles.marginTop)}>
        <Link to="/login" {...stylex.props(styles.link)}>
          Login
        </Link>
        <Link to="/tasks">
          Tasks
        </Link>
      </div>
    </div>
  );
};