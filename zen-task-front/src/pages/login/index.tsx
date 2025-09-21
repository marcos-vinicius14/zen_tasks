import React, { useState } from 'react';
import { FormInput } from '../../components/formInput';
import { useLogin } from '../../hooks';
import {
  CardContent,
  ErrorMessage,
  FooterLink,
  FooterText,
  FormTitle,
  Header,
  LoginForm,
  LoginWrapper,
  MainCard,
  PageContainer,
  SubmitButton,
  Subtitle,
  Title,
  Triangle,
} from './styles';

export function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('123456');

  const { mutate, isPending, isError, error } = useLogin();

  const handleLogin = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    mutate({ username, password });
  };

  return (
    <PageContainer>
      <LoginWrapper>
        <Header>
          <div className="flex items-center space-x-3">
            <Title>Zen Task</Title>
          </div>
          <Subtitle>Organize as suas tarefas</Subtitle>
        </Header>

        <MainCard>
          <Triangle />
          <CardContent>
            <FormTitle>Login</FormTitle>
            <LoginForm onSubmit={handleLogin}>
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
                <ErrorMessage>
                  {/* @ts-ignore */}
                  {error.message}
                </ErrorMessage>
              )}

              <SubmitButton type="submit" disabled={isPending}>
                {isPending ? 'Entrando...' : 'Entrar'}
              </SubmitButton>
            </LoginForm>

            <FooterText>
              Não tem uma conta? <FooterLink href="#">Crie agora</FooterLink>
            </FooterText>
          </CardContent>
        </MainCard>
      </LoginWrapper>
    </PageContainer>
  );
}
