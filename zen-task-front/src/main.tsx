import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './App.css'

const rootElement = document.getElementById('root');

if (rootElement) {
  const root = ReactDOM.createRoot(rootElement as HTMLElement);
  root.render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
} else {
  console.error("Elemento com id 'root' não encontrado no DOM.");
}''