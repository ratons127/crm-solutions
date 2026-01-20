/** @type {import('tailwindcss').Config} */
module.exports = {
  // darkMode: 'class',
  content: [
    './app/**/*.{js,jsx,ts,tsx}',
    './components/**/*.{js,jsx,ts,tsx}',
    './pages/**/*.{js,jsx,ts,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#F69348',
          light: '#FFE2E2',
          dark: '#E7000B',
        },
        background: '#ffffff',
        title: {
          DEFAULT: '#1F2937',
          sub: '#6B7280',
        },
      },
      spacing: {
        'touch': '44px', // Minimum touch target size for mobile
      },
      screens: {
        'xs': '475px',
        // sm: '640px' (default)
        // md: '768px' (default)
        // lg: '1024px' (default)
        // xl: '1280px' (default)
        '2xl': '1536px',
      },
      fontSize: {
        'responsive-xs': ['0.75rem', { lineHeight: '1rem' }],
        'responsive-sm': ['0.875rem', { lineHeight: '1.25rem' }],
        'responsive-base': ['1rem', { lineHeight: '1.5rem' }],
        'responsive-lg': ['1.125rem', { lineHeight: '1.75rem' }],
        'responsive-xl': ['1.25rem', { lineHeight: '1.75rem' }],
        'responsive-2xl': ['1.5rem', { lineHeight: '2rem' }],
        'responsive-3xl': ['1.875rem', { lineHeight: '2.25rem' }],
        'responsive-4xl': ['2.25rem', { lineHeight: '2.5rem' }],
      },
    },
  },
  plugins: [],
};
