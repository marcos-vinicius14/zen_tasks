import * as stylex from '@stylexjs/stylex';
import { vars } from './themes.stylex';  // Ou ajuste o caminho se movido para src/styles/

export const styles = stylex.create({
  pageContainer: {
    minHeight: '100vh',
    backgroundColor: vars.background,
    color: vars.text,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    fontFamily: 'sans-serif',
    padding: '1rem',
  },
  loginWrapper: {
    width: '100%',
    maxWidth: '28rem',
    margin: '0 auto',
  },
  header: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    marginBottom: '2rem',
  },
  headerInner: {
    display: 'flex',
    alignItems: 'center',
    gap: '0.75rem',
  },
  title: {
    fontSize: '1.875rem',
    fontWeight: 700,
  },
  subtitle: {
    color: vars.textSecondary,
    marginTop: '0.25rem',
  },
  mainCard: {
    position: 'relative',
    backgroundColor: vars.cardBackground,
    borderRadius: '0.75rem',
    boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)',
  },
  triangle: {
    position: 'absolute',
    top: '-0.5rem',
    left: '50%',
    transform: 'translateX(-50%)',
    width: 0,
    height: 0,
    borderLeft: '0.75rem solid transparent',
    borderRight: '0.75rem solid transparent',
    borderBottom: `0.75rem solid ${vars.cardBackground}`,
  },
  cardContent: {
    padding: '2rem',
    '@media (min-width: 768px)': {
      padding: '3rem',
    },
  },
  formTitle: {
    fontSize: '1.875rem',
    fontWeight: 700,
    textAlign: 'center',
    marginBottom: '2rem',
  },
  loginForm: {
    display: 'flex',
    flexDirection: 'column',
    gap: '1.5rem',
  },
  errorMessage: {
    color: vars.error,
    fontSize: '0.875rem',
    textAlign: 'center',
    padding: '0.5rem',
    backgroundColor: vars.errorBackground,
    borderRadius: '0.375rem',
  },
  submitButton: {
    width: '100%',
    backgroundColor: vars.primary,
    color: 'white',
    fontWeight: 700,
    padding: '0.75rem 1rem',
    borderStyle: 'none',
    borderRadius: '0.375rem',
    cursor: 'pointer',
    transition: 'all 0.3s ease-in-out',
    ':hover': {
      backgroundColor: vars.primaryHover,
      transform: 'scale(1.05)',
    },
    ':disabled': {
      opacity: 0.5,
      cursor: 'not-allowed',
    },
  },
  footerText: {
    textAlign: 'center',
    color: vars.textSecondary,
    marginTop: '2rem',
  },
  footerLink: {
    fontWeight: 500,
    color: '#3b82f6',
    textDecoration: 'none',
    ':hover': {
      textDecoration: 'underline',
    },
  },
});