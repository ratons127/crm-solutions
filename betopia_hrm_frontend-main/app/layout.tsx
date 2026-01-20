// app/layout.tsx
import { ColorSchemeScript } from '@mantine/core';
import '@mantine/core/styles.css';
import '@mantine/dates/styles.css';
import '@mantine/notifications/styles.css';
import RootProvider from './components/providers/RootProvider';
import './globals.css';
import '@mantine/charts/styles.css';

export const metadata = {
  title: 'Betopia - HRM',
  description: 'Human Resource Management System',
  icons: {
    icon: '/images/betopia-logo-sm.svg',
  },
};

export const viewport = {
  width: 'device-width',
  initialScale: 1,
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" suppressHydrationWarning>
      <head>
        <ColorSchemeScript />
      </head>
      <body suppressHydrationWarning={true}>
        <RootProvider>{children}</RootProvider>
      </body>
    </html>
  );
}
