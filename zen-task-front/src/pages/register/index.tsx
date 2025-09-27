import React, { useState } from 'react';
import { FormInput } from '../../components/formInput'; 
import { useRegister } from '../../hooks';


export function RegisterPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');

  const { mutate, isPending, isError, error } = useRegister();

  const handleRegister = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    mutate({ username, password, email });
  };

  return (
    <div className="min-h-screen overflow-y-hidden bg-gray-900 text-white flex flex-col items-center justify-center font-mono p-4 overflow-x-hidden">
      <div className="w-full max-w-md mx-auto">
        <header className="flex flex-col items-center mb-6 text-center sm:mb-8">
          <div className="flex items-center gap-3 justify-center">
            <h1 className="text-3xl font-bold font-mono sm:text-4xl md:text-5xl">Zen Task</h1>
          </div>
          <p className="text-gray-400 mt-2 font-mono text-base sm:text-lg">Organize as suas tarefas</p>
        </header>

        <main className="relative bg-gray-800 rounded-xl shadow-lg p-6 w-full sm:p-8">
          <h2 className="text-2xl font-bold text-center mb-6 font-sans sm:text-3xl">Crie sua conta</h2>
          <form className="flex flex-col gap-5 sm:gap-6" onSubmit={handleRegister}>
            <FormInput
              id="username"
              type="text"
              label="Nome de usuário"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Digite seu nome de usuário"
            />
            <FormInput
              id="email"
              type="email"
              label="Seu email"
              value={username}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Digite seu email"
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
              <div className="text-red-500 text-sm text-center p-3 bg-red-900/20 rounded-lg font-mono">
                {/* @ts-ignore */}
                {error.message}
              </div>
            )}

            <button type="submit" disabled={isPending} className="w-full bg-blue-600 text-white font-bold font-mono py-3 px-5 text-base border-none rounded-lg cursor-pointer transition-all duration-200 ease-in-out min-h-14 hover:bg-blue-700 hover:scale-[1.02] disabled:opacity-60 disabled:cursor-not-allowed disabled:transform-none">
              {isPending ? 'Entrando...' : 'Entrar'}
            </button>
          </form>

          <p className="text-center text-gray-400 mt-6 font-mono text-sm sm:text-base">
            Já tem uma conta?{' '}
            <a href="/login" className="font-medium text-blue-500 no-underline hover:underline focus:outline-2 focus:outline-blue-500 focus:outline-offset-2 focus:rounded-sm">
              Login
            </a>
          </p>
        </main>
      </div>
    </div>
  );
}
