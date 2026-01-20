// lib/breadcrumbs.ts
export function generateBreadcrumb(pathname: string) {
  // Remove query string & hash
  const cleanPath = pathname.split(/[?#]/)[0];

  // Split path into parts
  const parts = cleanPath.split('/').filter(Boolean);

  return parts.map((part, idx) => {
    const href = '/' + parts.slice(0, idx + 1).join('/');
    return {
      label: part.charAt(0).toUpperCase() + part.slice(1), // capitalize
      href,
    };
  });
}
