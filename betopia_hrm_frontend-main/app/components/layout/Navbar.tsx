'use client';

import { BellIcon } from '@heroicons/react/24/outline';
import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useEffect, useRef, useState } from 'react';
import { logoutUser } from '../../lib/features/auth/authAPI';
import { useAppDispatch, useAppSelector } from '../../lib/hooks';
import SearchBar from './SearchBar';
import { useGetEmployeeByIdQuery } from '@/services/api/employee/employeeProfileAPI';

export default function Navbar() {
  const [mounted, setMounted] = useState(false);
  const dispatch = useAppDispatch();
  const router = useRouter();

  const { user } = useAppSelector(s => s.auth);
  const [profileDropdownOpen, setProfileDropdownOpen] = useState(false);
  const [notificationDropdownOpen, setNotificationDropdownOpen] =
    useState(false);
  const [mobileSearchOpen, setMobileSearchOpen] = useState(false);

  const profileDropdownRef = useRef<HTMLDivElement>(null);
  const notificationDropdownRef = useRef<HTMLDivElement>(null);

  // ✅ Fetch employee profile data if logged in
  const { data: employeeRes } = useGetEmployeeByIdQuery(
    { id: Number(user?.employeeId) },
    { skip: !user?.employeeId }
  );

  const profileImage =
    (employeeRes?.data as any)?.imageUrl || '/images/profile.webp';

  useEffect(() => {
    setMounted(true);

    // NOTE: use native Event / MouseEvent here (not React.MouseEvent)
    const handleClickOutside = (event: MouseEvent) => {
      const target = event.target as Node | null;

      if (
        profileDropdownRef.current &&
        target &&
        !profileDropdownRef.current.contains(target)
      ) {
        setProfileDropdownOpen(false);
      }
      if (
        notificationDropdownRef.current &&
        target &&
        !notificationDropdownRef.current.contains(target)
      ) {
        setNotificationDropdownOpen(false);
      }
    };

    // Listen for close mobile search event from SearchBar
    const handleCloseMobileSearch = () => {
      setMobileSearchOpen(false);
    };

    document.addEventListener('mousedown', handleClickOutside);
    window.addEventListener('close-mobile-search', handleCloseMobileSearch);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      window.removeEventListener(
        'close-mobile-search',
        handleCloseMobileSearch
      );
    };
  }, []);

  if (!mounted) return null;

  const toggleProfileDropdown = () => setProfileDropdownOpen(o => !o);
  const toggleNotificationDropdown = () => setNotificationDropdownOpen(o => !o);

  const handleLogout = async () => {
    await dispatch(logoutUser() as any);
    router.push('/auth/login');
  };

  const toggleMobileMenu = () => {
    window.dispatchEvent(new Event('toggle-mobile-menu'));
  };

  return (
    <header className="px-4 py-2 flex items-center justify-between gap-4">
      {/* Left section: Hamburger Menu for Mobile */}
      <div className="flex items-center flex-shrink-0">
        <button
          onClick={toggleMobileMenu}
          className="lg:hidden p-2 rounded-md hover:bg-gray-100 transition mr-2"
          aria-label="Toggle menu"
        >
          <svg
            className="w-6 h-6 text-gray-700"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M4 6h16M4 12h16M4 18h16"
            />
          </svg>
        </button>
      </div>

      {/* Center section: Search Bar (Desktop) */}
      <div className="hidden md:flex flex-1 justify-center max-w-2xl mx-auto">
        <SearchBar />
      </div>

      {/* Right section: Icons and Profile */}
      <div className="flex items-center space-x-2 sm:space-x-4 flex-shrink-0">
        {/* Mobile Search Button */}
        <button
          onClick={() => setMobileSearchOpen(true)}
          className="md:hidden p-2 rounded-full bg-white hover:bg-gray-200 transition min-h-[44px] min-w-[44px] flex items-center justify-center"
          type="button"
          aria-label="Search"
        >
          <svg
            className="h-5 w-5 text-gray-600"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
            />
          </svg>
        </button>
        {/* Theme Toggle */}
        {/* <button
          onClick={toggleTheme}
          className="p-2 rounded-full bg-white hover:bg-gray-200 dark:hover:bg-gray-800 transition"
          aria-label="Toggle dark mode"
          type="button"
        >
          {isDark ? (
            <SunIcon className="h-5 w-5 text-yellow-400" />
          ) : (
            <MoonIcon className="h-5 w-5 text-gray-800" />
          )}
        </button> */}

        {/* Notifications */}
        <div className="relative" ref={notificationDropdownRef}>
          <button
            onClick={toggleNotificationDropdown}
            className="p-2 rounded-full bg-white hover:bg-gray-200 transition min-h-[44px] min-w-[44px] flex items-center justify-center"
            type="button"
            aria-label="Notifications"
          >
            <BellIcon className="h-5 w-5 text-gray-600" />
          </button>

          {notificationDropdownOpen && (
            <>
              {/* Backdrop for mobile */}
              <div
                className="fixed inset-0 bg-black/20 z-40 sm:hidden"
                onClick={() => setNotificationDropdownOpen(false)}
              />

              {/* Notification dropdown */}
              <div className="fixed sm:absolute left-4 right-4 sm:left-auto sm:right-0 top-16 sm:top-auto mt-0 sm:mt-2 w-auto sm:w-80 md:w-96 bg-white rounded-lg shadow-xl border border-gray-200 overflow-hidden z-50">
                <div className="px-4 py-3 text-base font-semibold text-gray-800 border-b border-gray-200">
                  Notifications -{' '}
                  <span className="font-normal text-red-500">
                    Coming Soon..
                  </span>
                </div>
                <div className="divide-y divide-gray-200 max-h-[60vh] sm:max-h-80 overflow-y-auto">
                  <div className="px-4 py-3 hover:bg-gray-50 cursor-pointer transition-colors">
                    <p className="text-sm text-gray-700 leading-relaxed">
                      New user registered 🎉
                    </p>
                  </div>
                  <div className="px-4 py-3 hover:bg-gray-50 cursor-pointer transition-colors">
                    <p className="text-sm text-gray-700 leading-relaxed">
                      Server restarted successfully ✅
                    </p>
                  </div>
                  <div className="px-4 py-3 hover:bg-gray-50 cursor-pointer transition-colors">
                    <p className="text-sm text-gray-700 leading-relaxed">
                      You have 3 pending tasks 📌
                    </p>
                  </div>
                </div>
                <div className="px-4 py-3 border-t border-gray-200 bg-gray-50">
                  <Link
                    href="/notifications"
                    className="block text-center text-sm font-semibold text-blue-600 hover:text-blue-700 hover:underline transition-colors"
                    onClick={() => setNotificationDropdownOpen(false)}
                  >
                    See all
                  </Link>
                </div>
              </div>
            </>
          )}
        </div>

        {/* Profile */}
        <div className="relative" ref={profileDropdownRef}>
          <button
            onClick={toggleProfileDropdown}
            className="relative flex items-center space-x-2 focus:outline-none min-h-[44px]"
            type="button"
            aria-haspopup="true"
            aria-expanded={profileDropdownOpen}
          >
            <Image
              className="h-9 w-9 sm:h-10 sm:w-10 rounded-full object-cover ring-2 ring-gray-300"
              src={profileImage}
              alt="Betopia Group"
              width={120}
              height={40}
              priority
              unoptimized
            />
            <div className="hidden md:flex flex-col items-start text-left">
              <span className="text-xs sm:text-sm font-semibold text-gray-800">
                {user?.name ?? 'User'}
              </span>
              <span className="text-xs text-gray-500">
                {user?.email ?? 'example@gmail.com'}
              </span>
            </div>
          </button>

          {profileDropdownOpen && (
            <div
              className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10"
              role="menu"
              aria-label="Profile options"
            >
              {user?.employeeId ? (
                <Link
                  href={`/employees/employees-profile/profile/${user?.employeeId}/view`}
                >
                  <button
                    className="block w-full text-left px-4 py-2 text-gray-800 hover:bg-gray-100"
                    role="menuitem"
                    type="button"
                  >
                    Profile
                  </button>
                </Link>
              ) : null}
              <button
                onClick={async () => {
                  setProfileDropdownOpen(false);
                  await handleLogout();
                }}
                className="block w-full text-left px-4 py-2 text-gray-800 hover:bg-gray-100"
                role="menuitem"
                type="button"
              >
                Logout
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Mobile Search Modal */}
      {mobileSearchOpen && (
        <div className="fixed inset-0 bg-black/50 z-50 md:hidden">
          <div className="absolute top-0 left-0 right-0 bg-white shadow-lg p-4">
            <div className="flex items-center gap-2">
              <div className="flex-1">
                <SearchBar />
              </div>
              <button
                onClick={() => setMobileSearchOpen(false)}
                className="p-2 rounded-md hover:bg-gray-100"
                aria-label="Close search"
              >
                <svg
                  className="w-6 h-6 text-gray-700"
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
          </div>
          <div
            className="absolute inset-0 top-20"
            onClick={() => setMobileSearchOpen(false)}
          />
        </div>
      )}
    </header>
  );
}
