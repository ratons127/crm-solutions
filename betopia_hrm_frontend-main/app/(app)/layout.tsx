'use client';

import { decryptJSONFromLocalStorage } from '@/lib/utils/secureStorage';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';

import Navbar from '../components/layout/Navbar';
import Sidebar from '../components/layout/Sidebar';

interface AppLayoutProps {
  children: React.ReactNode;
}

export default function AppLayout({ children }: AppLayoutProps) {
  const router = useRouter();

  // Track hydration and auth so server and first client render match
  const [hydrated, setHydrated] = useState(false);
  const [authorized, setAuthorized] = useState<boolean | null>(null);

  // Header background on scroll
  const [scrolled, setScrolled] = useState(false);
  // Sidebar collapsed state (read after mount to avoid SSR mismatch)
  const [sidebarCollapsed, setSidebarCollapsed] = useState<boolean>(false);

  useEffect(() => {
    // Auth gate (decrypt token)
    (async () => {
      let t: string | null = null;
      try {
        t = await decryptJSONFromLocalStorage<string>('token');
        if (!t) t = localStorage.getItem('token'); // migration fallback
      } catch {}
      if (!t) {
        setAuthorized(false);
        router.replace('/auth/login');
      } else {
        setAuthorized(true);
      }
    })();

    // Init UI state that depends on window/localStorage
    const saved = localStorage.getItem('sidebar-collapsed');
    if (saved === 'true') setSidebarCollapsed(true);

    const onScroll = () => setScrolled(window.scrollY > 4);
    onScroll();
    window.addEventListener('scroll', onScroll, { passive: true });

    const onSidebarChange = (e: any) => {
      if (e && typeof e.detail === 'boolean') {
        setSidebarCollapsed(e.detail);
      } else {
        const saved = localStorage.getItem('sidebar-collapsed');
        setSidebarCollapsed(saved === 'true');
      }
    };
    window.addEventListener('sidebar-collapsed-changed', onSidebarChange);

    setHydrated(true);

    return () => {
      window.removeEventListener('scroll', onScroll);
      window.removeEventListener('sidebar-collapsed-changed', onSidebarChange);
    };
  }, [router]);

  // Keep server and first client render identical; render only after hydration.
  if (!hydrated || !authorized) return null;

  return (
    <>
      <div className="flex min-h-screen overflow-x-hidden">
        {/* Fixed sidebar rendered once; spacer keeps content offset */}
        <Sidebar />
        {/* Sidebar spacer - only on desktop */}
        <div
          className={`hidden lg:block ${sidebarCollapsed ? 'w-16' : 'w-64'}`}
        />

        <div className="flex-1 flex flex-col min-w-0">
          {/* sticky header that changes bg on scroll */}
          <div
            className={`sticky top-0 z-40 transition-colors duration-200 ${
              scrolled
                ? 'bg-white/60 backdrop-blur-md border-b border-gray-200 shadow-sm'
                : 'bg-transparent'
            }`}
          >
            <Navbar />
          </div>

          <main className="px-4 py-4 sm:px-6 lg:px-8 min-w-0">{children}</main>
        </div>
      </div>
    </>
  );
}
