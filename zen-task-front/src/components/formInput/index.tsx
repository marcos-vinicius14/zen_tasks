import React from 'react';
import * as stylex from '@stylexjs/stylex';
import { styles } from './FormInput.styles';

// Definindo as propriedades que o componente aceita
// Isso permite que o componente receba qualquer propriedade de um input normal (like placeholder, etc)
interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
}

export const FormInput: React.FC<FormInputProps> = ({ label, id, ...props }) => {
  return (
    // A div principal que aplica os estilos de espaçamento
    <div {...stylex.props(styles.inputWrapper)}>
      <label htmlFor={id} {...stylex.props(styles.styledLabel)}>
        {label}
      </label>
      <input id={id} {...stylex.props(styles.styledInput)} {...props} />
    </div>
  );
};

