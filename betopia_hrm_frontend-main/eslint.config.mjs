// eslint.config.mjs
import { FlatCompat } from '@eslint/eslintrc'
import ts from '@typescript-eslint/eslint-plugin'
import tsParser from '@typescript-eslint/parser'

const asArray = (config) => (Array.isArray(config) ? config : [config]).filter(Boolean)
const compat = new FlatCompat({ baseDirectory: import.meta.dirname })

export default [
  {
    ignores: [
      'node_modules/**',
      '.next/**',
      'out/**',
      'build/**',
      'dist/**',
      'next-env.d.ts',
      '*.config.js',
      '*.config.mjs',
      '*.config.ts',
    ],
  },

  // Type-aware rules need a project for type info
  {
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        project: ['./tsconfig.json'], // adjust if monorepo
        tsconfigRootDir: import.meta.dirname,
        ecmaVersion: 'latest',
        sourceType: 'module',
      },
    },
    plugins: { '@typescript-eslint': ts },
    rules: {
      // Add/override TS rules here
    },
  },

  // Also lint JS without type info
  {
    files: ['**/*.{js,jsx}'],
    languageOptions: { ecmaVersion: 'latest', sourceType: 'module' },
  },

  // ✅ Next.js Core Web Vitals
  ...compat.extends('next/core-web-vitals'),

  // Optional: @typescript-eslint recommended + type-checked presets
  ...asArray(ts.configs.recommendedTypeChecked),
  ...asArray(ts.configs.stylisticTypeChecked),
]
