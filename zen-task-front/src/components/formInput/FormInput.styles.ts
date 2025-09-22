// style.ts (ou FormInput.styles.ts)
import * as stylex from '@stylexjs/stylex';
import { vars } from '../../pages/login/themes.stylex'

export const styles = stylex.create({
  inputWrapper: {
    display: 'flex',
    flexDirection: 'column',
    width: '100%',
  },
  styledLabel: {
    fontSize: '0.875rem',
    fontWeight: 500,
    color: vars.textSecondary,
    marginBottom: '0.25rem',
  },
  styledInput: {
    display: 'block',
    width: '100%',
    padding: '0.75rem 1rem',
    backgroundColor: vars.inputBackground,
    border: `1px solid ${vars.inputBorder}`,
    borderRadius: '0.375rem',
    color: vars.text,
    transition: 'border-color 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
    '::placeholder': {
      color: vars.inputPlaceholder,
    },
    ':focus': {
      outline: 'none',
      borderColor: vars.inputFocusBorder,
      boxShadow: `0 0 0 2px ${vars.inputFocusShadow}`,
    },
  },
});