import { Permission, Menu } from '../types/auth';

/**
 * Get permissions from localStorage
 */
export const getPermissions = (): Permission[] => {
  if (typeof window === 'undefined') return [];
  try {
    const permissions = localStorage.getItem('permissions');
    return permissions ? JSON.parse(permissions) : [];
  } catch {
    return [];
  }
};

/**
 * Get menus from localStorage
 */
export const getMenus = (): Menu[] => {
  if (typeof window === 'undefined') return [];
  try {
    const menus = localStorage.getItem('menus');
    return menus ? JSON.parse(menus) : [];
  } catch {
    return [];
  }
};

/**
 * Check if user has a specific permission by name
 */
export const hasPermission = (permissionName: string): boolean => {
  const permissions = getPermissions();
  return permissions.some(p => p.name === permissionName);
};

/**
 * Check if user has any of the specified permissions
 */
export const hasAnyPermission = (permissionNames: string[]): boolean => {
  const permissions = getPermissions();
  return permissionNames.some(name => 
    permissions.some(p => p.name === name)
  );
};

/**
 * Check if user has all of the specified permissions
 */
export const hasAllPermissions = (permissionNames: string[]): boolean => {
  const permissions = getPermissions();
  return permissionNames.every(name => 
    permissions.some(p => p.name === name)
  );
};

/**
 * Get permission object by name
 */
export const getPermission = (permissionName: string): Permission | undefined => {
  const permissions = getPermissions();
  return permissions.find(p => p.name === permissionName);
};

/**
 * Check if user has access to a specific menu/route
 */
export const hasMenuAccess = (url: string): boolean => {
  const menus = getMenus();
  
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
 * Get all active menu URLs (for route validation)
 */
export const getAllowedRoutes = (): string[] => {
  const menus = getMenus();
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
