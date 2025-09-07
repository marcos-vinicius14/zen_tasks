import { format, parseISO, isValid } from 'date-fns'
import { ptBR } from 'date-fns/locale'

export const formatDate = (date: string | Date, formatStr = 'dd/MM/yyyy'): string => {
  try {
    const dateObj = typeof date === 'string' ? parseISO(date) : date
    if (!isValid(dateObj)) {
      return 'Data inválida'
    }
    return format(dateObj, formatStr, { locale: ptBR })
  } catch {
    return 'Data inválida'
  }
}

export const formatDateTime = (date: string | Date): string => {
  return formatDate(date, 'dd/MM/yyyy HH:mm')
}

export const isDateOverdue = (date: string | Date): boolean => {
  try {
    const dateObj = typeof date === 'string' ? parseISO(date) : date
    if (!isValid(dateObj)) {
      return false
    }
    return dateObj < new Date()
  } catch {
    return false
  }
}