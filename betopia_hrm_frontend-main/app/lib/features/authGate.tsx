'use client';

import { useRouter } from 'next/navigation';
import { useAppSelector } from '../hooks';
import { useEffect, useState } from 'react';
import { decryptJSONFromLocalStorage } from '../utils/secureStorage';

interface User {
  id: string;
  name: string;
  email: string;
  roles: string[];
}

export default function AuthGate({
  children,
  roles = [],
}: {
  children: React.ReactNode;
  roles?: string[];
}) {
  const storeUser = useAppSelector(state => state.auth.user) as User | null;
  const [user, setUser] = useState<User | null>(storeUser);
  const router = useRouter();

  // ✅ Load from localStorage if Redux store empty
    // Load from encrypted storage if Redux store empty
  useEffect(() => {
    let cancelled = false;
    if (!storeUser) {
      (async () => {
        try {
          const dec = await decryptJSONFromLocalStorage<User>('user');
          if (!cancelled) setUser(dec ?? null);
        } catch {
          // ignore
        }
      })();
    } else {
      setUser(storeUser);
    }
    return () => { cancelled = true; };
  }, [storeUser]);

  // ✅ Redirect based on login + roles
  useEffect(() => {
    if (!user) {
      router.push('/auth/login');
    } else if (roles.length > 0 && !roles.some(r => user.roles?.includes(r))) {
      router.push('/unauthorized');
    }
  }, [user, roles, router]);

  return <>{children}</>;
}

