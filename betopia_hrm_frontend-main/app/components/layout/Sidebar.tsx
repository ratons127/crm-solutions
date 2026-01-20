'use client';

import { usePermissions } from '@/lib/hooks/usePermissions';
import { Menu } from '@/lib/types/auth';
import { DynamicIcon } from '@/lib/utils/dynamicIconResolver';
import { ChevronRightIcon as ChevronRightSmall } from '@heroicons/react/24/outline';
import Image from 'next/image';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import React, { useCallback, useEffect, useMemo, useState } from 'react';

interface NavLink {
  href?: string;
  label: string;
  icon?: React.ReactNode;
  children?: NavLink[];
}

interface SidebarItemProps {
  item: NavLink;
  collapsed: boolean;
  depth?: number;
  onLinkClick?: () => void;
}

function SidebarItem({
  item,
  collapsed,
  depth = 0,
  onLinkClick,
}: SidebarItemProps) {
  const pathname = usePathname();
  const [open, setOpen] = useState(false);
  const hasChildren = !!item.children?.length;

  // Helper function to check if any descendant matches the current path
  const hasActiveDescendant = useCallback(
    (items: NavLink[]): boolean => {
      return items.some(child => {
        if (child.href && pathname.startsWith(child.href)) {
          return true;
        }
        if (child.children && child.children.length > 0) {
          return hasActiveDescendant(child.children);
        }
        return false;
      });
    },
    [pathname]
  );

  // ✅ Auto-open only once (on mount) if pathname matches any descendant
  useEffect(() => {
    if (hasChildren && item.children) {
      const shouldOpen = hasActiveDescendant(item.children);
      if (shouldOpen) {
        setOpen(true);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // run only on first mount

  // Leaf link
  if (!hasChildren && item.href) {
    return (
      <div className="py-0 border-b border-transparent last:border-b-0">
        <Link
          href={item.href}
          onClick={onLinkClick}
          className={`flex items-center rounded-lg hover:bg-[#F69348] hover:text-white transition my-1 py-1.5 text-sm min-h-11 lg:min-h-0
            ${
              pathname === item.href
                ? 'bg-[#F69348] text-white border-l-4 border-[#F69348]'
                : 'border-l-4 border-transparent'
            }
          `}
          style={{ paddingLeft: collapsed ? 7 : depth * 9 + 7 }}
        >
          <span className="w-5 h-5 flex items-center justify-center">
            {item.icon}
          </span>
          <span
            className={`ml-3 whitespace-nowrap transition-all duration-200
              ${collapsed ? 'opacity-0 invisible w-0' : 'opacity-100 visible'}
            `}
          >
            {item.label}
          </span>
        </Link>
      </div>
    );
  }

  // Parent link
  return (
    <div className="py-1 border-b border-transparent last:border-b-0">
      <button
        onClick={() => setOpen(v => !v)}
        className={`flex items-center w-full rounded-lg py-1.5 pr-2 text-sm transition min-h-11 lg:min-h-0
          hover:bg-[#F69348] hover:text-white border-l-4 border-transparent
        `}
        style={{ paddingLeft: collapsed ? 7 : depth * 9 + 7 }}
      >
        <span className="w-5 h-5 flex items-center justify-center">
          {item.icon}
        </span>

        <span
          className={`ml-3 flex-1 text-left whitespace-nowrap transition-all duration-200
            ${collapsed ? 'opacity-0 invisible w-0' : 'opacity-100 visible'}
          `}
        >
          {item.label}
        </span>

        {!collapsed && (
          <ChevronRightSmall
            className={`h-4 w-4 transition-transform duration-500 ${
              open ? 'rotate-90' : 'rotate-0'
            }`}
          />
        )}
      </button>

      <div
        className={`ml-2 grid transition-all duration-500 ease-in-out ${
          hasChildren && !collapsed && open
            ? 'grid-rows-[1fr] opacity-100'
            : 'grid-rows-[0fr] opacity-0'
        }`}
      >
        {hasChildren && !collapsed && (
          <div className="overflow-hidden">
            {item.children!.map(child => (
              <SidebarItem
                key={child.label}
                item={child}
                collapsed={collapsed}
                depth={depth + 1}
                onLinkClick={onLinkClick}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default function Sidebar() {
  const [collapsed, setCollapsed] = useState(false);
  const [hovered, setHovered] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);

  // Fetch fresh menu data from API (will auto-update when menus are modified)
  // const { data: apiMenus } = useGetSidebarMenusQuery();

  // Fallback to cached menus from login if API hasn't loaded yet
  const { menus: cachedMenus } = usePermissions();

  // Use API menus if available, otherwise use cached menus
  const sidebarMenus = cachedMenus;

  useEffect(() => {
    const saved = localStorage.getItem('sidebar-collapsed');
    if (saved !== null) setCollapsed(saved === 'true');
  }, []);

  useEffect(() => {
    localStorage.setItem('sidebar-collapsed', collapsed.toString());
    try {
      window.dispatchEvent(
        new CustomEvent('sidebar-collapsed-changed', { detail: collapsed })
      );
    } catch (_) {
      // no-op for non-browser environments
    }
  }, [collapsed]);

  // Listen for mobile menu toggle from Navbar
  useEffect(() => {
    const handleMobileToggle = () => {
      setMobileOpen(prev => !prev);
    };
    window.addEventListener('toggle-mobile-menu', handleMobileToggle);
    return () =>
      window.removeEventListener('toggle-mobile-menu', handleMobileToggle);
  }, []);

  // const toggleCollapse = () => setCollapsed(prev => !prev);
  const effectiveCollapsed = collapsed && !hovered;

  // Close mobile menu when clicking a link
  const closeMobileMenu = () => {
    if (window.innerWidth < 1024) {
      setMobileOpen(false);
    }
  };

  const resolveIcon = useCallback((iconName?: string) => {
    return <DynamicIcon iconName={iconName} />;
  }, []);

  const dynamicLinks: NavLink[] = useMemo(() => {
    if (!sidebarMenus?.length) {
      return [];
    }

    // Filter out "Menu" item and any other items you want to hide
    const filteredMenus = sidebarMenus.filter(
      m => m.name !== 'Menu' && m.name !== 'Permissions'
    );

    // Check if menus already have children nested (from API)
    const hasNestedChildren = filteredMenus.some(
      m => m.children && m.children.length > 0
    );

    // Helper function to recursively convert Menu to NavLink with sorting by menuOrder
    const convertMenuToNavLink = (menu: Menu): NavLink => {
      // Sort children by menuOrder if they exist, also filter out "Menu"
      const sortedChildren =
        menu.children && menu.children.length > 0
          ? [...menu.children]
              .filter(
                child => child.name !== 'Menu' && child.name !== 'Permissions'
              )
              .sort((a, b) => (a.menuOrder || 0) - (b.menuOrder || 0))
              .map(child => convertMenuToNavLink(child))
          : [];

      return {
        label: menu.name,
        href: menu.url && menu.url !== '#' ? menu.url : undefined,
        icon: resolveIcon(menu.icon),
        children: sortedChildren,
      };
    };

    // If menus already have nested children, just convert them
    if (hasNestedChildren) {
      // Only return top-level menus (those without parent_id), sorted by menuOrder
      const topLevelMenus = filteredMenus
        .filter(m => !m.parent_id && !m.parentId)
        .sort((a, b) => (a.menuOrder || 0) - (b.menuOrder || 0));
      return topLevelMenus.map(menu => convertMenuToNavLink(menu));
    }

    // Otherwise, build hierarchy from parent_id relationships

    // Sort menus: first by hierarchy (parents first), then by menuOrder
    const sortedMenus = [...filteredMenus].sort((a, b) => {
      // First, sort by hierarchy (parents before children)
      const aIsParent = !a.parent_id && !a.parentId;
      const bIsParent = !b.parent_id && !b.parentId;

      if (aIsParent && !bIsParent) return -1;
      if (!aIsParent && bIsParent) return 1;

      // If both are at the same level, sort by menuOrder
      return (a.menuOrder || 0) - (b.menuOrder || 0);
    });

    // Build a lookup map by id - create ALL nodes first
    const map = new Map<number, NavLink>();

    sortedMenus.forEach((menu: Menu) => {
      map.set(menu.id, {
        label: menu.name,
        href: menu.url && menu.url !== '#' ? menu.url : undefined,
        icon: resolveIcon(menu.icon),
        children: [],
      });
    });

    // Build hierarchy - assign children to parents
    const roots: NavLink[] = [];

    sortedMenus.forEach((menu: Menu) => {
      const node = map.get(menu.id)!;
      const parentId = menu.parentId || menu.parent_id;

      if (parentId) {
        const parent = map.get(parentId);
        if (parent) {
          parent.children = parent.children || [];
          parent.children.push(node);
        } else {
          roots.push(node);
        }
      } else {
        roots.push(node);
      }
    });

    return roots;
  }, [sidebarMenus, resolveIcon]);

  // Use only dynamic links from API
  const mainLinks = dynamicLinks;

  return (
    <>
      {/* Mobile Backdrop */}
      {mobileOpen && (
        <div
          className="fixed inset-0 bg-black/50 z-40 lg:hidden"
          onClick={() => setMobileOpen(false)}
        />
      )}

      <aside
        className={`fixed left-0 top-0 z-50 text-gray-800 h-screen flex flex-col transition-all duration-300 bg-white shadow-lg
          ${effectiveCollapsed ? 'w-16' : 'w-64'}
          ${mobileOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
        `}
        onMouseEnter={() => setHovered(true)}
        onMouseLeave={() => setHovered(false)}
      >
        {/* Header */}
        <div className="flex items-center justify-center p-4 border-b border-transparent min-h-14 max-h-14 relative">
          <Link
            href="/"
            className="flex items-center space-x-2"
            onClick={closeMobileMenu}
          >
            <Image
              src="/images/betopia-logo.svg"
              alt="Betopia Group"
              width={110}
              height={30}
              priority
              className={effectiveCollapsed ? 'hidden' : 'block'}
            />
            {effectiveCollapsed && (
              <Image
                src="/images/betopia-logo.svg"
                alt="B"
                width={32}
                height={32}
                priority
              />
            )}
          </Link>

          {/* Mobile Close Button */}
          <button
            onClick={() => setMobileOpen(false)}
            className="absolute right-4 top-4 lg:hidden p-2 rounded-md hover:bg-gray-100"
            aria-label="Close menu"
          >
            <svg
              className="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        {/* Collapse Button - Desktop Only */}
        {/* <button
          onClick={toggleCollapse}
          className="hidden lg:block absolute top-4 -right-3 z-10
            bg-white border border-gray-300 rounded-full p-1.5 shadow-md
            text-gray-500 hover:text-gray-700 transition"
        >
          {collapsed ? (
            <ChevronRightIcon className="h-3 w-3" />
          ) : (
            <ChevronLeftIcon className="h-3 w-3" />
          )}
        </button> */}

        {/* Main Navigation */}
        <nav className="flex-1 px-2 py-4 overscroll-contain overflow-y-auto [&::-webkit-scrollbar]:hidden [-ms-overflow-style:none] [scrollbar-width:none]">
          {mainLinks?.map(item => (
            <SidebarItem
              key={item.label}
              item={item}
              collapsed={effectiveCollapsed}
              onLinkClick={closeMobileMenu}
            />
          ))}
        </nav>
      </aside>
    </>
  );
}
