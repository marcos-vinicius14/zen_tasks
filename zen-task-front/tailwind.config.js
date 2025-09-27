/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#2563eb',
        primaryHover: '#1d4ed8',
        text: '#ffffff',
        textSecondary: '#9ca3af',
        background: '#111827',
        cardBackground: '#1f2937',
        error: '#ef4444',
        errorBackground: 'rgba(127, 29, 29, 0.2)',
        inputBackground: '#374151',
        inputBorder: '#4b5563',
        inputPlaceholder: '#6b7280',
        inputFocusBorder: '#3b82f6',
        inputFocusShadow: '#3b82f6',
      },
      fontFamily: {
        title: ['Roboto', 'sans-serif'],
        body: ['Roboto Mono', 'monospace'],
      }
    },
  },
  plugins: [],
}
