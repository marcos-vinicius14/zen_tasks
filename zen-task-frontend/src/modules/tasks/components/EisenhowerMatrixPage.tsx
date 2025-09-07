import React from 'react'
import { Link } from 'react-router-dom'

const EisenhowerMatrixPage: React.FC = () => {
  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Matriz de Eisenhower</h1>
        <div className="flex space-x-3">
          <button className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
            Nova Tarefa
          </button>
          <Link
            to="/tasks"
            className="border border-blue-600 text-blue-600 px-4 py-2 rounded-lg hover:bg-blue-50 transition-colors"
          >
            Ver Lista
          </Link>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4 h-[600px]">
        {/* Quadrante 1: Urgente e Importante (Fazer Agora) */}
        <div className="bg-red-50 border-2 border-red-200 rounded-lg p-4">
          <h3 className="text-lg font-semibold text-red-800 mb-3 text-center">
            🔥 Fazer Agora
          </h3>
          <p className="text-sm text-red-600 text-center mb-4">
            Urgente e Importante
          </p>
          <div className="space-y-2">
            <div className="text-center text-gray-500 text-sm">
              Nenhuma tarefa neste quadrante
            </div>
          </div>
        </div>

        {/* Quadrante 2: Importante mas não Urgente (Agendar) */}
        <div className="bg-blue-50 border-2 border-blue-200 rounded-lg p-4">
          <h3 className="text-lg font-semibold text-blue-800 mb-3 text-center">
            📅 Agendar
          </h3>
          <p className="text-sm text-blue-600 text-center mb-4">
            Importante mas não Urgente
          </p>
          <div className="space-y-2">
            <div className="text-center text-gray-500 text-sm">
              Nenhuma tarefa neste quadrante
            </div>
          </div>
        </div>

        {/* Quadrante 3: Urgente mas não Importante (Delegar) */}
        <div className="bg-yellow-50 border-2 border-yellow-200 rounded-lg p-4">
          <h3 className="text-lg font-semibold text-yellow-800 mb-3 text-center">
            👥 Delegar
          </h3>
          <p className="text-sm text-yellow-600 text-center mb-4">
            Urgente mas não Importante
          </p>
          <div className="space-y-2">
            <div className="text-center text-gray-500 text-sm">
              Nenhuma tarefa neste quadrante
            </div>
          </div>
        </div>

        {/* Quadrante 4: Nem Urgente nem Importante (Eliminar) */}
        <div className="bg-gray-50 border-2 border-gray-200 rounded-lg p-4">
          <h3 className="text-lg font-semibold text-gray-800 mb-3 text-center">
            🗑️ Eliminar
          </h3>
          <p className="text-sm text-gray-600 text-center mb-4">
            Nem Urgente nem Importante
          </p>
          <div className="space-y-2">
            <div className="text-center text-gray-500 text-sm">
              Nenhuma tarefa neste quadrante
            </div>
          </div>
        </div>
      </div>

      <div className="mt-6 bg-blue-50 border border-blue-200 rounded-lg p-4">
        <h3 className="font-semibold text-blue-800 mb-2">💡 Como usar a Matriz de Eisenhower:</h3>
        <ul className="text-sm text-blue-700 space-y-1">
          <li><strong>Fazer Agora:</strong> Tarefas urgentes e importantes - prioridade máxima</li>
          <li><strong>Agendar:</strong> Tarefas importantes mas não urgentes - planeje quando fazer</li>
          <li><strong>Delegar:</strong> Tarefas urgentes mas não importantes - pode ser delegada</li>
          <li><strong>Eliminar:</strong> Tarefas nem urgentes nem importantes - considere remover</li>
        </ul>
      </div>
    </div>
  )
}

export default EisenhowerMatrixPage