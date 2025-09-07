import React, { createContext, useContext, useState, useEffect } from 'react'
import { User, LoginRequest, RegisterRequest } from '../types'
import { authService } from '../services'

interface AuthContextType {
  user: User | null
  isLoading: boolean
  isAuthenticated: boolean
  login: (data: LoginRequest) => Promise<void>
  register: (data: RegisterRequest) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // Check for stored user data on app load
    const storedUser = localStorage.getItem('user-data')
    const storedToken = localStorage.getItem('auth-token')

    if (storedUser && storedToken) {
      try {
        setUser(JSON.parse(storedUser))
      } catch {
        localStorage.removeItem('user-data')
        localStorage.removeItem('auth-token')
      }
    }
    setIsLoading(false)
  }, [])

  const login = async (data: LoginRequest) => {
    setIsLoading(true)
    try {
      const response = await authService.login(data)
      localStorage.setItem('auth-token', response.token)
      localStorage.setItem('user-data', JSON.stringify(response.user))
      setUser(response.user)
    } finally {
      setIsLoading(false)
    }
  }

  const register = async (data: RegisterRequest) => {
    setIsLoading(true)
    try {
      await authService.register(data)
      // After registration, automatically log in
      await login({ username: data.username, password: data.password })
    } finally {
      setIsLoading(false)
    }
  }

  const logout = () => {
    authService.logout()
    setUser(null)
  }

  const value = {
    user,
    isLoading,
    isAuthenticated: !!user,
    login,
    register,
    logout,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}