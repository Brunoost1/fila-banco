import { ButtonHTMLAttributes } from 'react';
import classNames from 'classnames';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline';
}

export function Button({ 
  children, 
  variant = 'primary', 
  className,
  ...props 
}: ButtonProps) {
  return (
    <button
      className={classNames(
        'px-4 py-2 rounded-lg font-medium transition-colors',
        {
          'bg-blue-600 text-white hover:bg-blue-700': variant === 'primary',
          'bg-gray-200 text-gray-800 hover:bg-gray-300': variant === 'secondary',
          'border-2 border-blue-600 text-blue-600 hover:bg-blue-50': variant === 'outline',
        },
        className
      )}
      {...props}
    >
      {children}
    </button>
  );
}