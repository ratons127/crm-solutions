'use client';

import { useState, useEffect, useRef, useMemo } from 'react';
import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import { useRouter } from 'next/navigation';
import { usePermissions } from '../../lib/hooks/usePermissions';
import { flattenMenus, getMenuBreadcrumbs } from '../../lib/utils/menuUtils';
import { Menu } from '../../lib/types/auth';

export default function SearchBar() {
  const [isOpen, setIsOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedIndex, setSelectedIndex] = useState(0);
  const searchRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const router = useRouter();
  const { menus } = usePermissions();

  // Flatten menus for searching
  const flatMenus = useMemo(() => {
    return flattenMenus(menus).filter(menu => menu.url && menu.url !== '#');
  }, [menus]);

  // Filter menus based on search query
  const filteredMenus = useMemo(() => {
    if (!searchQuery.trim()) return [];

    const query = searchQuery.toLowerCase();
    return flatMenus.filter(menu =>
      menu.name.toLowerCase().includes(query) ||
      menu.url.toLowerCase().includes(query)
    ).slice(0, 8); // Limit to 8 results
  }, [searchQuery, flatMenus]);

  // Handle click outside to close
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (searchRef.current && !searchRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Handle keyboard shortcuts (Cmd+K or Ctrl+K)
  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if ((event.metaKey || event.ctrlKey) && event.key === 'k') {
        event.preventDefault();
        setIsOpen(true);
        inputRef.current?.focus();
      }

      if (event.key === 'Escape') {
        setIsOpen(false);
        setSearchQuery('');
      }
    };

    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, []);

  // Handle keyboard navigation in results
  const handleKeyDown = (event: React.KeyboardEvent) => {
    if (filteredMenus.length === 0) return;

    if (event.key === 'ArrowDown') {
      event.preventDefault();
      setSelectedIndex(prev => (prev + 1) % filteredMenus.length);
    } else if (event.key === 'ArrowUp') {
      event.preventDefault();
      setSelectedIndex(prev => (prev - 1 + filteredMenus.length) % filteredMenus.length);
    } else if (event.key === 'Enter') {
      event.preventDefault();
      handleSelectMenu(filteredMenus[selectedIndex]);
    }
  };

  // Reset selected index when search query changes
  useEffect(() => {
    setSelectedIndex(0);
  }, [searchQuery]);

  // Handle menu selection
  const handleSelectMenu = (menu: Menu) => {
    router.push(menu.url);
    setIsOpen(false);
    setSearchQuery('');
    // Dispatch event to close mobile search modal if open
    window.dispatchEvent(new Event('close-mobile-search'));
  };

  // Get breadcrumb path for a menu
  const getBreadcrumbPath = (menu: Menu): string => {
    const breadcrumbs = getMenuBreadcrumbs(menus, menu.url);
    return breadcrumbs.map(m => m.name).join(' > ');
  };

  return (
    <div ref={searchRef} className="relative flex-1 max-w-xl mx-4">
      {/* Search Input */}
      <div className="relative">
        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <MagnifyingGlassIcon className="h-5 w-5 text-gray-400" />
        </div>
        <input
          ref={inputRef}
          type="text"
          className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg bg-white text-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          placeholder="Search"
          value={searchQuery}
          onChange={(e) => {
            setSearchQuery(e.target.value);
            setIsOpen(true);
          }}
          onFocus={() => setIsOpen(true)}
          onKeyDown={handleKeyDown}
        />
      </div>

      {/* Search Results Dropdown */}
      {isOpen && searchQuery && filteredMenus.length > 0 && (
        <>
          {/* Backdrop for mobile */}
          <div
            className="fixed inset-0 bg-black/20 z-40 sm:hidden"
            onClick={() => setIsOpen(false)}
          />

          {/* Results dropdown */}
          <div className="absolute left-0 right-0 mt-2 bg-white rounded-lg shadow-xl border border-gray-200 overflow-hidden z-50 max-h-[70vh] overflow-y-auto">
            <div className="py-1">
              {filteredMenus.map((menu, index) => (
                <button
                  key={menu.id}
                  onClick={() => handleSelectMenu(menu)}
                  onMouseEnter={() => setSelectedIndex(index)}
                  className={`w-full text-left px-4 py-3 hover:bg-gray-50 transition-colors ${
                    index === selectedIndex ? 'bg-blue-50' : ''
                  }`}
                  type="button"
                >
                  <div className="flex items-start justify-between">
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium text-gray-900 truncate">
                        {menu.name}
                      </p>
                      <p className="text-xs text-gray-500 truncate mt-1">
                        {getBreadcrumbPath(menu)}
                      </p>
                    </div>
                  </div>
                </button>
              ))}
            </div>
          </div>
        </>
      )}

      {/* No results message */}
      {isOpen && searchQuery && filteredMenus.length === 0 && (
        <div className="absolute left-0 right-0 mt-2 bg-white rounded-lg shadow-xl border border-gray-200 overflow-hidden z-50">
          <div className="px-4 py-8 text-center">
            <p className="text-sm text-gray-500">No menus found</p>
            <p className="text-xs text-gray-400 mt-1">Try a different search term</p>
          </div>
        </div>
      )}
    </div>
  );
}
