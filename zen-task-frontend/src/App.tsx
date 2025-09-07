import { Routes, Route } from 'react-router-dom'
import { AuthProvider } from '@modules/users/hooks/useAuth'
import Layout from '@shared/components/Layout'
import HomePage from '@shared/components/HomePage'
import LoginPage from '@modules/users/components/LoginPage'
import RegisterPage from '@modules/users/components/RegisterPage'
import TasksPage from '@modules/tasks/components/TasksPage'
import EisenhowerMatrixPage from '@modules/tasks/components/EisenhowerMatrixPage'
import ProtectedRoute from '@shared/components/ProtectedRoute'

function App() {
  return (
    <AuthProvider>
      <Layout>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/tasks"
            element={
              <ProtectedRoute>
                <TasksPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/matrix"
            element={
              <ProtectedRoute>
                <EisenhowerMatrixPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </Layout>
    </AuthProvider>
  )
}

export default App