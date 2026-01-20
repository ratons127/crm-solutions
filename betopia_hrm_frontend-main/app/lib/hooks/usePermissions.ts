'use client';

import { useEffect, useMemo, useState } from 'react';
import { useAppState } from '../features/app/appSlice';
import { Permission, Menu } from '../types/auth';
import { decryptJSONFromLocalStorage } from '../utils/secureStorage';

/**
 * Hook to access user permissions and check access
 */
export const usePermissions = () => {
  const { auth } = useAppState();
  const [cachedPermissions, setCachedPermissions] = useState<Permission[]>([]);
  const [cachedMenus, setCachedMenus] = useState<Menu[]>([]);

  // Attempt to decrypt cached permissions/menus when not present in Redux
  useEffect(() => {
    let cancelled = false;
    (async () => {
      if (auth.user?.permissions?.length || auth.user?.menus?.length) return;
      try {
        const [perms, menus] = await Promise.all([
          decryptJSONFromLocalStorage<Permission[]>('permissions'),
          decryptJSONFromLocalStorage<Menu[]>('menus'),
        ]);
        if (!cancelled) {
          if (Array.isArray(perms)) setCachedPermissions(perms);
          if (Array.isArray(menus)) setCachedMenus(menus);
        }
      } catch {
        // ignore
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [auth.user]);
  
  const permissions: Permission[] = useMemo(() => {
    // Try to get from Redux state first
    if (auth.user?.permissions && auth.user.permissions.length > 0) {
      return auth.user.permissions;
    }
    // Then try decrypted cache
    if (cachedPermissions.length > 0) {
      return cachedPermissions;
    }
    
    // Fallback to localStorage
    if (typeof window !== 'undefined') {
      try {
        const stored = localStorage.getItem('permissions');
        if (stored) {
          const parsed = JSON.parse(stored);
          return Array.isArray(parsed) ? parsed : [];
        }
      } catch {
        // ignore
      }
    }
    
    return [];
  }, [auth.user, cachedPermissions]);

  const menus: Menu[] = useMemo(() => {
    // Try to get from Redux state first
    if (auth.user?.menus && auth.user.menus.length > 0) {
      return auth.user.menus;
    }
    // Then try decrypted cache
    if (cachedMenus.length > 0) {
      return cachedMenus;
    }
    
    // Fallback to localStorage
    if (typeof window !== 'undefined') {
      try {
        const stored = localStorage.getItem('menus');
        if (stored) {
          const parsed = JSON.parse(stored);
          return Array.isArray(parsed) ? parsed : [];
        }
      } catch {
        // ignore
      }
    }
    
    return [];
  }, [auth.user, cachedMenus]);

  /**
   * Check if user has a specific permission
   */
  const hasPermission = (permissionName: string): boolean => {
    return permissions.some(p => p.name === permissionName);
  };

  /**
   * Check if user has any of the specified permissions
   */
  const hasAnyPermission = (permissionNames: string[]): boolean => {
    return permissionNames.some(name => 
      permissions.some(p => p.name === name)
    );
  };

  /**
   * Check if user has all of the specified permissions
   */
  const hasAllPermissions = (permissionNames: string[]): boolean => {
    return permissionNames.every(name => 
      permissions.some(p => p.name === name)
    );
  };

  /**
   * Check if user has access to a specific menu/route
   */
  const hasMenuAccess = (url: string): boolean => {
    const checkMenuRecursive = (menuList: Menu[]): boolean => {
      for (const menu of menuList) {
        if (menu.url === url && menu.status) {
          return true;
        }
        if (menu.children && menu.children.length > 0) {
          if (checkMenuRecursive(menu.children)) {
            return true;
          }
        }
      }
      return false;
    };
    
    return checkMenuRecursive(menus);
  };

  /**
   * Get all active menu URLs
   */
  const getAllowedRoutes = (): string[] => {
    const routes: string[] = [];
    
    const extractRoutes = (menuList: Menu[]) => {
      menuList.forEach(menu => {
        if (menu.url && menu.url !== '#' && menu.status) {
          routes.push(menu.url);
        }
        if (menu.children && menu.children.length > 0) {
          extractRoutes(menu.children);
        }
      });
    };
    
    extractRoutes(menus);
    return routes;
  };

  return {
    permissions,
    menus,
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    hasMenuAccess,
    getAllowedRoutes,
  };
};
