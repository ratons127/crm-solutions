'use client';

import { usePathname } from 'next/navigation';
import Link from 'next/link';
import { generateBreadcrumb } from '../../lib/breadcrumbs';

export default function Breadcrumbs() {
  const pathname = usePathname();
  const crumbs = generateBreadcrumb(pathname);
  const pageTitle =
    crumbs.length > 0 ? crumbs[crumbs.length - 1].label : 'Dashboard';

  const isHomeActive = pathname === '/';

  return (
    <div className="flex items-center text-xs text-gray-700">
      <h1 className="font-semibold text-sm mr-2">{pageTitle} -</h1>

      {/* Home */}
      {isHomeActive ? (
        <span className="text-black font-medium" aria-current="page">
          Home
        </span>
      ) : (
        <Link href="/" className="hover:underline text-gray-500 transition-colors">
          Home
        </Link>
      )}

      {/* Trail */}
      {crumbs.map((crumb, idx) => {
        const isLast = idx === crumbs.length - 1;
        return (
          <span key={crumb.href} className="flex items-center">
            <span className="mx-1.5 text-gray-400">/</span>
            {isLast ? (
              <span
                className="capitalize text-gray-800 font-medium"
                aria-current="page"
              >
                {crumb.label}
              </span>
            ) : (
              <Link
                href={crumb.href}
                className="hover:underline capitalize text-gray-500 transition-colors"
              >
                {crumb.label}
              </Link>
            )}
          </span>
        );
      })}
    </div>
  );
}
