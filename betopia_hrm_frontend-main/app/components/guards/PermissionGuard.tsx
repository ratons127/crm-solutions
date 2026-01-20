'use client';

import { ReactNode } from 'react';
import { usePermissions } from '@/lib/hooks/usePermissions';

interface PermissionGuardProps {
  children: ReactNode;
  permission?: string;
  permissions?: string[];
  requireAll?: boolean;
  fallback?: ReactNode;
}

/**
 * Component to conditionally render children based on permissions
 * @param permission - Single permission to check
 * @param permissions - Multiple permissions to check
 * @param requireAll - If true, user must have all permissions. If false, user needs at least one
 * @param fallback - Component to render if user doesn't have permission
 */
export const PermissionGuard = ({
  children,
  permission,
  permissions,
  requireAll = false,
  fallback = null,
}: PermissionGuardProps) => {
  const { hasPermission, hasAnyPermission, hasAllPermissions } = usePermissions();

  let hasAccess = false;

  if (permission) {
    hasAccess = hasPermission(permission);
  } else if (permissions && permissions.length > 0) {
    hasAccess = requireAll 
      ? hasAllPermissions(permissions)
      : hasAnyPermission(permissions);
  } else {
    // No permission specified, allow access
    hasAccess = true;
  }

  if (!hasAccess) {
    return <>{fallback}</>;
  }

  return <>{children}</>;
};
