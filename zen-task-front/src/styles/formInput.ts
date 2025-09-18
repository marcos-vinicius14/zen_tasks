import styled from "styled-components";

export const InputWrapper = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
`;

export const StyledLabel = styled.label`
  font-size: 0.875rem;
  font-weight: 500; 
  color: #9ca3af;
  margin-bottom: 0.25rem;
`;

export const StyledInput = styled.input`
  display: block;
  width: 100%;
  padding: 0.75rem 1rem;
  background-color: #374151;
  border: 1px solid #4b5563;
  border-radius: 0.375rem;
  color: #ffffff;
  transition: border-color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;

  &::placeholder {
    color: #6b7280;
  }

  &:focus {
    outline: none; /* focus:outline-none */
    border-color: #3b82f6; /* focus:border-blue-500 */
    box-shadow: 0 0 0 2px #3b82f6; 
  }
`;