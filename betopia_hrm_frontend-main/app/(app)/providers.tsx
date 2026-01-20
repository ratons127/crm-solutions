// app/providers.tsx
'use client';

import { ThemeProvider } from 'next-themes';

export function Providers({ children }: { children: React.ReactNode }) {
  return (
    <ThemeProvider
      attribute="class" // adds `class="dark"` on <html>
      defaultTheme="system" // start with system preference
      enableSystem // enable system dark/light
    >
      <>{children}</>
    </ThemeProvider>
  );
}
