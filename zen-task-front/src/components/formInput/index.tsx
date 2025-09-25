import React from 'react';
import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';

function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
  error?: string;
  className?: string;
}

export const FormInput: React.FC<FormInputProps> = ({ label, id, error, className, ...props }) => {
  return (
    <div className={cn("w-full flex flex-col gap-2", className)}>
      <label 
        htmlFor={id} 
        className="text-sm font-medium text-gray-400 sm:text-base"
      >
        {label}
      </label>
      
      <input 
        id={id} 
        {...props} 
        className={cn(
          // Estilos base
          'w-full rounded-lg bg-gray-700 border text-white transition-colors duration-200',
          'px-4 py-3 text-base',
          'placeholder:text-gray-400',

          'focus:outline-none focus:ring-2 disabled:opacity-50 disabled:cursor-not-allowed',
          'hover:border-blue-500',

          error 
            ? 'border-red-500 focus:border-red-500 focus:ring-red-500/50' 
            : 'border-gray-600 focus:border-blue-500 focus:ring-blue-500/50',
            
        )} 
      />

      {error && <p className="text-sm text-red-500 mt-1">{error}</p>}
    </div>
  );
};