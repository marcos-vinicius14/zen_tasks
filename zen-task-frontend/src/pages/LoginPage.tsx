import React, { useState } from 'react';
import { useLogin } from '../hooks';
import { LoginRequest } from '../types';

export const LoginPage: React.FC = () => {
  const [credentials, setCredentials] = useState<LoginRequest>({
    username: '',
    password: '',
  });

  const loginMutation = useLogin();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    loginMutation.mutate(credentials);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCredentials({
      ...credentials,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div style={{ padding: '20px', maxWidth: '400px', margin: '0 auto' }}>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '15px' }}>
          <label>
            Username:
            <input
              type="text"
              name="username"
              value={credentials.username}
              onChange={handleChange}
              required
              style={{ width: '100%', padding: '8px', marginTop: '5px' }}
            />
          </label>
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label>
            Password:
            <input
              type="password"
              name="password"
              value={credentials.password}
              onChange={handleChange}
              required
              style={{ width: '100%', padding: '8px', marginTop: '5px' }}
            />
          </label>
        </div>
        <button
          type="submit"
          disabled={loginMutation.isPending}
          style={{ width: '100%', padding: '10px' }}
        >
          {loginMutation.isPending ? 'Logging in...' : 'Login'}
        </button>
        {loginMutation.error && (
          <p style={{ color: 'red', marginTop: '10px' }}>
            {loginMutation.error.message}
          </p>
        )}
      </form>
    </div>
  );
};