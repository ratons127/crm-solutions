export type Role = 'user' | 'admin' | 'manager' | 'superadmin'; // define your roles here

export interface Permission {
  createdDate?: string;
  lastModifiedDate?: string;
  createdBy?: number;
  lastModifiedBy?: number;
  id: number;
  name: string;
  guardName: string;
}

export interface Menu {
  createdDate?: string;
  lastModifiedDate?: string;
  createdBy?: number;
  lastModifiedBy?: number;
  id: number;
  parentId?: number;
  parent_id?: number; // Keep for backward compatibility
  permission?: Permission;
  name: string;
  url: string;
  icon: string;
  headerMenu: boolean;
  sidebarMenu: boolean;
  dropdownMenu: boolean;
  childrenParentMenu: boolean;
  status: boolean;
  menuOrder: number;
  children: Menu[];
}

export interface User {
  id: string;
  name: string;
  email: string;
  roles: Role[] | any; // roles assigned to the user
  permissions?: Permission[];
  menus?: Menu[];
  employeeId?: number;
  employeeSerialId?: number;
}
