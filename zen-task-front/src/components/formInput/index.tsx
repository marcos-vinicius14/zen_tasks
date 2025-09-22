// componente (FormInput.tsx)
import React from 'react';
import * as stylex from '@stylexjs/stylex';
import { styles } from './FormInput.styles';  // Ajuste o caminho se necessário (remova .ts se for TS)

interface FormInputProps {
  id: string;
  type: 'text' | 'password' | 'email' | 'number';
  label: string;
  value: string | number;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
}

export const FormInput: React.FC<FormInputProps> = ({ id, type, label, value, onChange, placeholder }) => (
  <div {...stylex.props(styles.inputWrapper)}>
    <label htmlFor={id} {...stylex.props(styles.styledLabel)}>
      {label}
    </label>
    <input
      id={id}
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      {...stylex.props(styles.styledInput)}
    />
  </div>
);