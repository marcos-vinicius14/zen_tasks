// Estilos do login - Corrigido para StyleX
import * as stylex from '@stylexjs/stylex';
import { vars } from '../../styles/themes.stylex';

export const styles = stylex.create({
  pageContainer: {
    minHeight: '100vh',
    backgroundColor: vars.background,
    color: vars.text,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    fontFamily: vars.fontBody,
    padding: '1rem',
    marginBottom: '2rem',
    '@media (min-width: 640px)': {
      padding: '2rem',
      marginBottom: '5rem',
    },
    '@media (min-width: 1024px)': {
      padding: '3rem',
      marginBottom: '10rem',
    },
  },

  loginWrapper: {
    width: '100%',
    maxWidth: '90%',
    margin: '0 auto',
    '@media (min-width: 640px)': {
      maxWidth: '28rem',
    },
    '@media (min-width: 768px)': {
      maxWidth: '32rem',
    },
    '@media (min-width: 1024px)': {
      maxWidth: '36rem',
    },
  },

  header: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    marginBottom: '20rem',
    textAlign: 'center',
    padding: '1rem',
    '@media (min-width: 640px)': {
      marginBottom: '3rem',
      padding: '1.5rem',
    },
    '@media (min-width: 1024px)': {
      marginBottom: '4rem',
    },
  },

  headerInner: {
    display: 'flex',
    alignItems: 'center',
    gap: '0.5rem',
    flexWrap: 'wrap',
    justifyContent: 'center',
    '@media (min-width: 640px)': {
      gap: '0.75rem',
    },
    '@media (min-width: 1024px)': {
      gap: '1rem',
    },
  },

  title: {
    fontSize: '1.75rem',
    fontWeight: 700,
    fontFamily: vars.fontBody,
    lineHeight: 1.2,
    marginBottom: '0.5rem',
    '@media (min-width: 640px)': {
      fontSize: '2rem',
    },
    '@media (min-width: 768px)': {
      fontSize: '2.25rem',
    },
    '@media (min-width: 1024px)': {
      fontSize: '2.5rem',
    },
  },

  subtitle: {
    color: vars.textSecondary,
    marginTop: '0.75rem',
    fontFamily: vars.fontBody,
    fontSize: '0.9rem',
    lineHeight: 1.4,
    '@media (min-width: 640px)': {
      fontSize: '1rem',
    },
    '@media (min-width: 768px)': {
      fontSize: '1.1rem',
    },
  },

  mainCard: {
    position: 'relative',
    backgroundColor: vars.cardBackground,
    borderRadius: '0.5rem',
    boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)',
    padding: '1.5rem',
    width: '100%',
    boxSizing: 'border-box',
    '@media (min-width: 640px)': {
      borderRadius: '0.75rem',
      padding: '2rem',
    },
    '@media (min-width: 768px)': {
      padding: '2.5rem',
    },
    '@media (min-width: 1024px)': {
      borderRadius: '1rem',
      padding: '3rem',
    },
  },

  formTitle: {
    fontSize: '1.5rem',
    fontWeight: 700,
    textAlign: 'center',
    marginBottom: '1.5rem',
    fontFamily: vars.fontTitle,
    lineHeight: 1.3,
    '@media (min-width: 640px)': {
      fontSize: '1.75rem',
      marginBottom: '2rem',
    },
    '@media (min-width: 768px)': {
      fontSize: '1.875rem',
    },
    '@media (min-width: 1024px)': {
      fontSize: '2rem',
      marginBottom: '2.5rem',
    },
  },

  loginForm: {
    display: 'flex',
    flexDirection: 'column',
  },

  errorMessage: {
    color: vars.error,
    fontSize: '0.875rem',
    textAlign: 'center',
    padding: '0.75rem',
    backgroundColor: vars.errorBackground,
    borderRadius: '0.5rem',
    fontFamily: vars.fontBody,
    marginBottom: '1rem',
    lineHeight: 1.4,
    '@media (min-width: 640px)': {
      fontSize: '0.9rem',
      padding: '1rem',
      marginBottom: '1.25rem',
    },
  },

  submitButton: {
    width: '100%',
    backgroundColor: vars.primary,
    color: 'white',
    fontWeight: 700,
    fontFamily: vars.fontBody,
    padding: '0.875rem 1.25rem',
    fontSize: '0.9rem',
    borderStyle: 'none',
    borderRadius: '0.5rem',
    cursor: 'pointer',
    transition: 'all 0.2s ease-in-out',
    minHeight: '3rem',
    marginTop: '1.5rem',
    '@media (min-width: 640px)': {
      padding: '1rem 1.5rem',
      fontSize: '1rem',
      minHeight: '3.5rem',
      marginTop: '2rem',
    },
    '@media (min-width: 1024px)': {
      padding: '1.125rem 1.75rem',
      fontSize: '1.1rem',
    },
    ':hover': {
      backgroundColor: vars.primaryHover,
      transform: 'scale(1.02)',
    },
    ':disabled': {
      opacity: 0.6,
      cursor: 'not-allowed',
      transform: 'none',
    },
  },

  footerText: {
    textAlign: 'center',
    color: vars.textSecondary,
    marginTop: '1.5rem',
    fontFamily: vars.fontBody,
    fontSize: '0.875rem',
    lineHeight: 1.5,
    padding: '0 1rem',
    '@media (min-width: 640px)': {
      fontSize: '0.9rem',
      marginTop: '2rem',
      padding: '0',
    },
    '@media (min-width: 1024px)': {
      fontSize: '1rem',
      marginTop: '2.5rem',
    },
  },

  footerLink: {
    fontWeight: 500,
    color: '#3b82f6',
    textDecoration: 'none',
    ':hover': {
      textDecoration: 'underline',
    },
    ':focus': {
      outline: '2px solid #3b82f6',
      outlineOffset: '2px',
      borderRadius: '2px',
    },
  },
});