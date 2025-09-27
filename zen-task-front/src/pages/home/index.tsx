import React from 'react';
import { Link } from 'react-router-dom';

export const HomePage: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center font-mono p-4 text-center">
      <main className="max-w-2xl mx-auto">
        <h1 className="text-5xl font-bold font-mono sm:text-6xl md:text-7xl">
          Zen Task
        </h1>
        <p className="text-gray-400 mt-4 font-mono text-lg sm:text-xl">
          Organize sua vida, uma tarefa de cada vez.
        </p>
        <p className="mt-6 text-base sm:text-lg max-w-prose">
          As suas tarefas são classificadas automaticamente usando a Matriz de Eisenhower, permitindo que
          você priorize com clareza e conquiste seus objetivos com menos estresse.
        </p>
        <div className="mt-10 flex flex-col sm:flex-row items-center justify-center gap-4">
          <Link
            to="/register"
            className="w-full sm:w-auto bg-blue-600 text-white font-bold font-mono py-3 px-8 text-base border-none rounded-lg cursor-pointer transition-all duration-200 ease-in-out hover:bg-blue-700 hover:scale-[1.02]"
          >
            Comece Agora
          </Link>
          <Link
            to="/login"
            className="font-medium text-blue-500 no-underline hover:underline focus:outline-2 focus:outline-blue-500 focus:outline-offset-2 focus:rounded-sm"
          >
            Já tenho uma conta
          </Link>
        </div>
      </main>
    </div>
  );
};