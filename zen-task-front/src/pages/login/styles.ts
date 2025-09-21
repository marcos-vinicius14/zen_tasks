import styled from 'styled-components';

export const PageContainer = styled.div`
  min-height: 100vh;
  background-color: #111827;
  color: #ffffff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-family: sans-serif;
  padding: 1rem;
`;

export const LoginWrapper = styled.div`
  width: 100%;
  max-width: 28rem;
  margin: 0 auto;
`;

export const Header = styled.header`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 2rem; // mb-8
`;

export const Title = styled.h1`
  font-size: 1.875rem; // text-3xl
  font-weight: 700; // font-bold
`;

export const Subtitle = styled.p`
  color: #9ca3af;
  margin-top: 0.25rem;
`;

export const MainCard = styled.main`
  position: relative;
  background-color: #1f2937;
  border-radius: 0.75rem;
  box-shadow:
    0 10px 15px -3px rgb(0 0 0 / 0.1),
    0 4px 6px -4px rgb(0 0 0 / 0.1);
`;

export const Triangle = styled.div`
  position: absolute;
  top: 0;
  left: 50%;
  width: 12rem; // w-48
  height: 6rem; // h-24
  background-color: #1f2937;

  clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
  transform: translateX(-50%) translateY(-99%) rotate(180deg);
`;

export const CardContent = styled.div`
  padding: 2rem;

  @media (min-width: 768px) {
    padding: 3rem;
  }
`;

export const FormTitle = styled.h2`
  font-size: 1.875rem; // text-3xl
  font-weight: 700; // font-bold
  text-align: center;
  margin-bottom: 2rem; // mb-8
`;

export const LoginForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

export const ErrorMessage = styled.div`
  color: #ef4444;
  font-size: 0.875rem;
  text-align: center;
  padding: 0.5rem;
  background-color: rgba(127, 29, 29, 0.2);
  border-radius: 0.375rem;
`;

export const SubmitButton = styled.button`
  width: 100%;
  background-color: #2563eb;
  color: white;
  font-weight: 700;
  padding: 0.75rem 1rem;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.3s ease-in-out;

  &:hover {
    background-color: #1d4ed8;
    transform: scale(1.05);
  }

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
`;

export const FooterText = styled.p`
  text-align: center;
  color: #9ca3af;
  margin-top: 2rem;
`;

export const FooterLink = styled.a`
  font-weight: 500;
  color: #3b82f6;
  text-decoration: none;

  &:hover {
    text-decoration: underline; // hover:underline
  }
`;
