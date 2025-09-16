import React, { useState } from 'react';
import { useLogin } from '../hooks';
import { LoginRequest } from '../types';
import styles from './LoginPage.module.css';

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
    <div className={styles.loginContainer}>
      <div className={styles.loginCard}>
        <div className={styles.logoSection}>
          <div className={styles.logo}>Z</div>
          <h1 className={styles.title}>Welcome back</h1>
          <p className={styles.subtitle}>Sign in to your ZenTasks account</p>
        </div>

        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.inputGroup}>
            <label htmlFor="username" className={styles.label}>
              Username
            </label>
            <input
              id="username"
              type="text"
              name="username"
              value={credentials.username}
              onChange={handleChange}
              required
              placeholder="Enter your username"
              className={styles.input}
              autoComplete="username"
            />
          </div>

          <div className={styles.inputGroup}>
            <label htmlFor="password" className={styles.label}>
              Password
            </label>
            <input
              id="password"
              type="password"
              name="password"
              value={credentials.password}
              onChange={handleChange}
              required
              placeholder="Enter your password"
              className={styles.input}
              autoComplete="current-password"
            />
          </div>

          <button
            type="submit"
            disabled={loginMutation.isPending}
            className={`${styles.submitButton} ${loginMutation.isPending ? styles.loading : ''}`}
          >
            {loginMutation.isPending ? 'Signing in...' : 'Sign in'}
          </button>

          {loginMutation.error && (
            <div className={styles.errorMessage}>
              {loginMutation.error.message}
            </div>
          )}
        </form>

        <div className={styles.footer}>
          Don&apos;t have an account?{' '}
          <a href="/register" className={styles.footerLink}>
            Sign up
          </a>
        </div>
      </div>
    </div>
  );
};
