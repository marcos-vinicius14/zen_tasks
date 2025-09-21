import { InputWrapper, StyledInput, StyledLabel } from "./style";

interface FormInputProps {
  id: string;
  type: 'text' | 'password' | 'email' | 'number';
  label: string;
  value: string | number;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
}

export const FormInput: React.FC<FormInputProps> = ({ id, type, label, value, onChange, placeholder }) => (
  <InputWrapper>
    <StyledLabel htmlFor={id}>
      {label}
    </StyledLabel>
    <StyledInput
      id={id}
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
    />
  </InputWrapper>
);