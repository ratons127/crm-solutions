'use client';

import { ReactNode, ButtonHTMLAttributes } from 'react';
import { usePermissions } from '@/lib/hooks/usePermissions';

interface PermissionButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode;
  permission?: string;
  permissions?: string[];
  requireAll?: boolean;
  hideIfNoAccess?: boolean;
}

/**
 * Button component with permission-based rendering
 * @param permission - Single permission to check
 * @param permissions - Multiple permissions to check
 * @param requireAll - If true, user must have all permissions
 * @param hideIfNoAccess - If true, button is hidden. If false, button is disabled
 */
export const PermissionButton = ({
  children,
  permission,
  permissions,
  requireAll = false,
  hideIfNoAccess = false,
  disabled,
  ...props
}: PermissionButtonProps) => {
  const { hasPermission, hasAnyPermission, hasAllPermissions } = usePermissions();

  let hasAccess = true;

  if (permission) {
    hasAccess = hasPermission(permission);
  } else if (permissions && permissions.length > 0) {
    hasAccess = requireAll 
      ? hasAllPermissions(permissions)
      : hasAnyPermission(permissions);
  }

  if (!hasAccess && hideIfNoAccess) {
    return null;
  }

  return (
    <button
      {...props}
      disabled={disabled || !hasAccess}
      className={`${props.className} ${!hasAccess ? 'opacity-50 cursor-not-allowed' : ''}`}
    >
      {children}
    </button>
  );
};
