'use client';

import { useMemo } from 'react';
import { usePermissions } from './usePermissions';

/**
 * Hook to check permissions for a specific resource
 * Automatically checks common CRUD permissions: create, edit, update, delete, list, view
 * 
 * @param resource - The resource name (e.g., 'leave-group-assign', 'user', 'role')
 * @param customActions - Additional custom actions beyond CRUD (e.g., ['approve', 'reject'])
 * 
 * @example
 * const { canCreate, canEdit, canDelete, canList } = useResourcePermissions('leave-group-assign');
 * 
 * @example With custom actions
 * const { canCreate, canApprove, canReject } = useResourcePermissions('leave-request', ['approve', 'reject']);
 */
export const useResourcePermissions = (
  resource: string,
  customActions: string[] = []
) => {
  const { hasPermission } = usePermissions();

  return useMemo(() => {
    // Standard CRUD actions
    const standardActions = {
      canCreate: hasPermission(`${resource}-create`),
      canEdit: hasPermission(`${resource}-edit`),
      canUpdate: hasPermission(`${resource}-update`),
      canDelete: hasPermission(`${resource}-delete`),
      canList: hasPermission(`${resource}-list`),
      canView: hasPermission(`${resource}-view`),
      canRead: hasPermission(`${resource}-read`),
    };

    // Custom actions (e.g., approve, reject, publish, etc.)
    const customPermissions = customActions.reduce((acc, action) => {
      const camelCaseAction = `can${action.charAt(0).toUpperCase()}${action.slice(1)}`;
      acc[camelCaseAction] = hasPermission(`${resource}-${action}`);
      return acc;
    }, {} as Record<string, boolean>);

    return {
      ...standardActions,
      ...customPermissions,
      // Helper to check any permission for this resource
      hasResourcePermission: (action: string) => hasPermission(`${resource}-${action}`),
    };
  }, [resource, customActions, hasPermission]);
};
