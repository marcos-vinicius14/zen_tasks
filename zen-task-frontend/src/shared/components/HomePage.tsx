import React from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '@modules/users/hooks'

const HomePage: React.FC = () => {
  const { isAuthenticated } = useAuth()

  return (
    <div className="text-center max-w-4xl mx-auto">
      <div className="mb-8">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">
          Bem-vindo ao ZenTasks
        </h1>
        <p className="text-xl text-gray-600 mb-8">
          Gerencie suas tarefas com eficiência usando a Matriz de Eisenhower
        </p>
      </div>

      <div className="grid md:grid-cols-2 gap-8 mb-12">
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-lg font-semibold mb-3">📋 Gerenciamento de Tarefas</h3>
          <p className="text-gray-600">
            Organize suas tarefas de forma inteligente e mantenha o foco no que realmente importa.
          </p>
        </div>
        
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h3 className="text-lg font-semibold mb-3">📊 Matriz de Eisenhower</h3>
          <p className="text-gray-600">
            Priorize suas tarefas baseado em urgência e importância para máxima produtividade.
          </p>
        </div>
      </div>

      {!isAuthenticated ? (
        <div className="space-x-4">
          <Link
            to="/register"
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors"
          >
            Começar Agora
          </Link>
          <Link
            to="/login"
            className="border border-blue-600 text-blue-600 px-6 py-3 rounded-lg hover:bg-blue-50 transition-colors"
          >
            Fazer Login
          </Link>
        </div>
      ) : (
        <div className="space-x-4">
          <Link
            to="/tasks"
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors"
          >
            Ver Minhas Tarefas
          </Link>
          <Link
            to="/matrix"
            className="border border-blue-600 text-blue-600 px-6 py-3 rounded-lg hover:bg-blue-50 transition-colors"
          >
            Matriz de Eisenhower
          </Link>
        </div>
      )}
    </div>
  )
}

export default HomePage