import { Menu } from "../types/auth";

/**
 * Recursively sorts menu items and their children by menuOrder
 */
export function sortMenusByOrder(menus: Menu[]): Menu[] {
  return [...menus]
    .sort((a, b) => (a.menuOrder || 0) - (b.menuOrder || 0))
    .map(menu => ({
      ...menu,
      children: menu.children?.length
        ? sortMenusByOrder(menu.children)
        : menu.children,
    }));
}

/**
 * Filters menus to only include active (status: true) items
 */
export function filterActiveMenus(menus: Menu[]): Menu[] {
  return menus
    .filter(menu => menu.status === true)
    .map(menu => ({
      ...menu,
      children: menu.children?.length
        ? filterActiveMenus(menu.children)
        : menu.children,
    }));
}

/**
 * Filters menus based on user permissions
 * Only includes menus that:
 * 1. Have no permission requirement, OR
 * 2. Have a permission that exists in userPermissions array
 */
export function filterMenusByPermissions(
  menus: Menu[],
  userPermissions: string[]
): Menu[] {
  return menus
    .filter(menu => {
      // If menu has no permission requirement, include it
      if (!menu.permission) return true;

      // If menu has permission requirement, check if user has it
      return userPermissions.includes(menu.permission.name);
    })
    .map(menu => ({
      ...menu,
      children: menu.children?.length
        ? filterMenusByPermissions(menu.children, userPermissions)
        : menu.children,
    }));
}

/**
 * Filters menus based on menu type flags
 * @param menus - Array of menu items
 * @param type - Type of menu to filter ('sidebar' | 'header' | 'dropdown')
 */
export function filterMenusByType(
  menus: Menu[],
  type: "sidebar" | "header" | "dropdown"
): Menu[] {
  const filterKey =
    type === "sidebar"
      ? "sidebarMenu"
      : type === "header"
        ? "headerMenu"
        : "dropdownMenu";

  return menus
    .filter(menu => menu[filterKey] === true)
    .map(menu => ({
      ...menu,
      children: menu.children?.length
        ? filterMenusByType(menu.children, type)
        : menu.children,
    }));
}

/**
 * Gets all menu items as a flat array (no hierarchy)
 */
export function flattenMenus(menus: Menu[]): Menu[] {
  const result: Menu[] = [];

  function traverse(menuList: Menu[]) {
    for (const menu of menuList) {
      result.push(menu);
      if (menu.children?.length) {
        traverse(menu.children);
      }
    }
  }

  traverse(menus);
  return result;
}

/**
 * Finds a menu item by ID
 */
export function findMenuById(menus: Menu[], id: number): Menu | undefined {
  for (const menu of menus) {
    if (menu.id === id) return menu;
    if (menu.children?.length) {
      const found = findMenuById(menu.children, id);
      if (found) return found;
    }
  }
  return undefined;
}

/**
 * Finds a menu item by URL path
 */
export function findMenuByUrl(menus: Menu[], url: string): Menu | undefined {
  for (const menu of menus) {
    if (menu.url === url) return menu;
    if (menu.children?.length) {
      const found = findMenuByUrl(menu.children, url);
      if (found) return found;
    }
  }
  return undefined;
}

/**
 * Gets breadcrumb trail for a given URL
 */
export function getMenuBreadcrumbs(menus: Menu[], url: string): Menu[] {
  const breadcrumbs: Menu[] = [];

  function traverse(menuList: Menu[], parents: Menu[]): boolean {
    for (const menu of menuList) {
      if (menu.url === url) {
        breadcrumbs.push(...parents, menu);
        return true;
      }
      if (menu.children?.length) {
        if (traverse(menu.children, [...parents, menu])) {
          return true;
        }
      }
    }
    return false;
  }

  traverse(menus, []);
  return breadcrumbs;
}

/**
 * Comprehensive menu processor that applies all filters and sorting
 */
export function processMenus(
  menus: Menu[],
  options?: {
    userPermissions?: string[];
    type?: "sidebar" | "header" | "dropdown";
    activeOnly?: boolean;
  }
): Menu[] {
  let processed = [...menus];

  // Filter by active status
  if (options?.activeOnly !== false) {
    processed = filterActiveMenus(processed);
  }

  // Filter by type
  if (options?.type) {
    processed = filterMenusByType(processed, options.type);
  }

  // Filter by permissions
  if (options?.userPermissions) {
    processed = filterMenusByPermissions(processed, options.userPermissions);
  }

  // Sort by menuOrder
  processed = sortMenusByOrder(processed);

  return processed;
}

/**
 * Builds a parent-child hierarchy from a flat menu array using parent_id/parentId
 */
export function buildMenuHierarchy(flatMenus: Menu[]): Menu[] {
  const menuMap = new Map<number, Menu>();
  const rootMenus: Menu[] = [];

  // First pass: Create a map of all menus with empty children arrays
  flatMenus.forEach(menu => {
    menuMap.set(menu.id, { ...menu, children: [] });
  });

  // Second pass: Build the hierarchy
  flatMenus.forEach(menu => {
    const menuNode = menuMap.get(menu.id)!;
    const parentId = menu.parentId || menu.parent_id;

    if (parentId) {
      const parent = menuMap.get(parentId);
      if (parent) {
        parent.children = parent.children || [];
        parent.children.push(menuNode);
      } else {
        // Parent not found, treat as root
        rootMenus.push(menuNode);
      }
    } else {
      // No parent, this is a root menu
      rootMenus.push(menuNode);
    }
  });

  return sortMenusByOrder(rootMenus);
}
