'use client';

import { useEffect } from 'react';
import { useAppDispatch } from '@/lib/hooks';
import { setAuth } from '@/lib/features/app/appSlice';
import { loginSuccess } from '@/lib/features/auth/authSlice';
import { decryptJSONFromLocalStorage } from '@/lib/utils/secureStorage';

export default function AuthHydrator() {
  const dispatch = useAppDispatch();

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const [token, refreshToken, user, permissions, menus] = await Promise.all([
          decryptJSONFromLocalStorage<string>('token'),
          decryptJSONFromLocalStorage<string>('refreshToken'),
          decryptJSONFromLocalStorage<any>('user'),
          decryptJSONFromLocalStorage<any[]>('permissions'),
          decryptJSONFromLocalStorage<any[]>('menus'),
        ]);

        if (cancelled) return;
        if (token && user) {
          dispatch(setAuth({ accessToken: token, refreshToken: refreshToken ?? null, user }));
          dispatch(
            loginSuccess({
              user,
              token,
              role: (user as any).role,
              permissions: Array.isArray(permissions) ? permissions : [],
              menus: Array.isArray(menus) ? menus : [],
              employeeId: (user as any).employeeId ?? null,
              companyId: (user as any).companyId ?? null,
              employeeSerialId: (user as any).employeeSerialId ?? null,
            })
          );
        }
      } catch {
        // ignore
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [dispatch]);

  return null;
}

