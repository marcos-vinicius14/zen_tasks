import React from 'react'
import { Link } from 'react-router-dom'

const TasksPage: React.FC = () => {
  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Minhas Tarefas</h1>
        <div className="flex space-x-3">
          <button className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
            Nova Tarefa
          </button>
          <Link
            to="/matrix"
            className="border border-blue-600 text-blue-600 px-4 py-2 rounded-lg hover:bg-blue-50 transition-colors"
          >
            Ver Matriz
          </Link>
        </div>
      </div>

      <div className="grid gap-6">
        {/* Filtros */}
        <div className="bg-white p-4 rounded-lg shadow-sm border">
          <h3 className="text-lg font-semibold mb-3">Filtros</h3>
          <div className="flex flex-wrap gap-4">
            <select className="px-3 py-2 border border-gray-300 rounded-md">
              <option value="">Todos os Status</option>
              <option value="CREATED">Criada</option>
              <option value="IN_PROGRESS">Em Progresso</option>
              <option value="COMPLETED">Concluída</option>
            </select>
            
            <select className="px-3 py-2 border border-gray-300 rounded-md">
              <option value="">Todos os Quadrantes</option>
              <option value="DO_NOW">Fazer Agora</option>
              <option value="SCHEDULE">Agendar</option>
              <option value="DELEGATE">Delegar</option>
              <option value="ELIMINATE">Eliminar</option>
            </select>
            
            <input
              type="text"
              placeholder="Buscar tarefas..."
              className="px-3 py-2 border border-gray-300 rounded-md flex-1 min-w-0"
            />
          </div>
        </div>

        {/* Lista de Tarefas */}
        <div className="bg-white rounded-lg shadow-sm border">
          <div className="p-6 text-center text-gray-500">
            <p>Nenhuma tarefa encontrada.</p>
            <p className="text-sm mt-2">Crie sua primeira tarefa para começar!</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default TasksPage