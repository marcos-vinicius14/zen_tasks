import React, {useState} from 'react';
import { FormInput } from '../../components/formInput';
import { useLogin } from '../../hooks';
import * as stylex from '@stylexjs/stylex';
import { styles } from './LoginPage.styles';

export function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('123456');

  const { mutate, isPending, isError, error } = useLogin();

  const handleLogin = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    mutate({ username, password });
  };

  return (
    <div {...stylex.props(styles.pageContainer)}>
      <div {...stylex.props(styles.loginWrapper)}>
        <header {...stylex.props(styles.header)}>
          <div {...stylex.props(styles.headerInner)}>
            <h1 {...stylex.props(styles.title)}>Zen Task</h1>
          </div>
          <p {...stylex.props(styles.subtitle)}>Organize as suas tarefas</p>
        </header>

        <main {...stylex.props(styles.mainCard)}>
          <h2 {...stylex.props(styles.formTitle)}>Login</h2>
          <form {...stylex.props(styles.loginForm)} onSubmit={handleLogin}>
            <FormInput
              id="username"
              type="text"
              label="Nome de usuário"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Digite seu nome de usuário"
            />
            <FormInput
              id="password"
              type="password"
              label="Senha"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Digite sua senha"
            />

            {isError && (
              <div {...stylex.props(styles.errorMessage)}>
                {/* @ts-ignore */}
                {error.message}
              </div>
            )}

            <button type="submit" disabled={isPending} {...stylex.props(styles.submitButton)}>
              {isPending ? 'Entrando...' : 'Entrar'}
            </button>
          </form>

          <p {...stylex.props(styles.footerText)}>
            Não tem uma conta?{' '}
            <a href="#" {...stylex.props(styles.footerLink)}>
              Crie agora
            </a>
          </p>
        </main>
      </div>
    </div>
  );
}

