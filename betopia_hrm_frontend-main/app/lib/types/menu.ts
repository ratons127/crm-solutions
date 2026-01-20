import { DBModel } from './index';

export interface MenuPermission extends DBModel {
  name: string;
  guardName: string;
}

export interface MenuItem extends DBModel {
  message?: string;
  permission?: MenuPermission | null;
  permission_id?: number | string | null;
  parent_id?: number | string | null; // legacy support
  parentId?: number | string | null; // new API field
  name: string;
  url: string;
  icon: string;
  headerMenu: boolean;
  sidebarMenu: boolean;
  dropdownMenu: boolean;
  childrenParentMenu: boolean;
  status: boolean;
  menuOrder?: number; // new API field (optional to keep compatibility)
  children: MenuItem[];
}
