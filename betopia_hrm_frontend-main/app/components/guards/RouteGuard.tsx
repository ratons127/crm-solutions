'use client';

import { ReactNode, useEffect } from 'react';
import { useRouter, usePathname } from 'next/navigation';
import { usePermissions } from '@/lib/hooks/usePermissions';
import { useAppState } from '@/lib/features/app/appSlice';

interface RouteGuardProps {
  children: ReactNode;
  requiredPermission?: string;
  requiredPermissions?: string[];
  requireAll?: boolean;
}

/**
 * Route-level guard that redirects unauthorized users
 */
export const RouteGuard = ({
  children,
  requiredPermission,
  requiredPermissions,
  requireAll = false,
}: RouteGuardProps) => {
  const router = useRouter();
  const pathname = usePathname();
  const { auth } = useAppState();
  const { hasPermission, hasAnyPermission, hasAllPermissions, hasMenuAccess } = usePermissions();

  useEffect(() => {
    // Check if user is authenticated
    if (!auth.user || !auth.accessToken) {
      router.replace('/auth/login');
      return;
    }

    // Check menu access
    if (!hasMenuAccess(pathname)) {
      router.replace('/unauthorized');
      return;
    }

    // Check specific permissions if provided
    if (requiredPermission) {
      if (!hasPermission(requiredPermission)) {
        router.replace('/unauthorized');
        return;
      }
    }

    if (requiredPermissions && requiredPermissions.length > 0) {
      const hasAccess = requireAll
        ? hasAllPermissions(requiredPermissions)
        : hasAnyPermission(requiredPermissions);

      if (!hasAccess) {
        router.replace('/unauthorized');
        return;
      }
    }
  }, [
    auth.user,
    auth.accessToken,
    pathname,
    requiredPermission,
    requiredPermissions,
    requireAll,
    router,
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    hasMenuAccess,
  ]);

  // Only render children if user is authenticated
  if (!auth.user || !auth.accessToken) {
    return null;
  }

  return <>{children}</>;
};
