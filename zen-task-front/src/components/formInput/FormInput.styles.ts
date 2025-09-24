// Estilos do form input - Corrigido para StyleX
import * as stylex from '@stylexjs/stylex';
import { vars } from '../../styles/themes.stylex';

export const styles = stylex.create({
  inputWrapper: {
    display: 'flex',
    flexDirection: 'column',
    width: '100%',
    gap: '0.5rem',
    marginBottom: '1.5rem',
    '@media (min-width: 640px)': {
      gap: '0.75rem',
      marginBottom: '2rem',
    },
    '@media (min-width: 1024px)': {
      marginBottom: '2.5rem',
    },
  },

  styledLabel: {
    fontSize: '0.9rem',
    fontWeight: 500,
    color: vars.textSecondary,
    fontFamily: vars.fontBody,
    lineHeight: 1.4,
    marginBottom: '0.25rem',
    '@media (min-width: 640px)': {
      fontSize: '0.95rem',
      marginBottom: '0.5rem',
    },
    '@media (min-width: 1024px)': {
      fontSize: '1rem',
    },
  },

  styledInput: {
    display: 'block',
    width: '100%',
    padding: '0.875rem 1rem',
    fontSize: '16px', // Fixo para evitar zoom no mobile
    minHeight: '3rem',
    backgroundColor: vars.inputBackground,
    border: `1px solid ${vars.inputBorder}`,
    borderRadius: '0.375rem',
    color: vars.text,
    fontFamily: vars.fontBody,
    transition: 'border-color 0.2s ease-in-out, box-shadow 0.2s ease-in-out, transform 0.1s ease-in-out',
    boxSizing: 'border-box',
    lineHeight: 1.4,
    '@media (min-width: 640px)': {
      padding: '1rem 1.25rem',
      fontSize: '1rem',
      minHeight: '3.25rem',
      borderRadius: '0.5rem',
    },
    '@media (min-width: 1024px)': {
      padding: '1.125rem 1.5rem',
      fontSize: '1.05rem',
      minHeight: '3.5rem',
    },
    '::placeholder': {
      color: vars.inputPlaceholder,
      fontSize: '0.9rem',
      '@media (min-width: 640px)': {
        fontSize: '0.95rem',
      },
    },
    ':focus': {
      outline: 'none',
      borderColor: vars.inputFocusBorder,
      boxShadow: `0 0 0 3px ${vars.inputFocusShadow}`,
      transform: 'translateY(-1px)',
    },
    ':hover': {
      borderColor: vars.inputFocusBorder,
    },
  },

  inputError: {
    borderColor: vars.error,
    boxShadow: `0 0 0 2px ${vars.error}25`,
  },

  inputDisabled: {
    opacity: 0.6,
    cursor: 'not-allowed',
    backgroundColor: vars.inputBackground + '80',
  },
});