'use client';

import { ShieldExclamationIcon } from '@heroicons/react/24/outline';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

export default function UnauthorizedPage() {
  const router = useRouter();

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4 py-8 sm:py-12">
      <div className="max-w-md w-full bg-white shadow-lg rounded-2xl px-4 sm:px-6 md:px-8 py-8 sm:py-10 text-center">
        <div className="flex justify-center mb-6">
          <ShieldExclamationIcon className="h-20 w-20 sm:h-24 sm:w-24 text-red-500" />
        </div>

        <h1 className="text-2xl sm:text-3xl md:text-4xl font-bold text-gray-900 mb-4">
          Access Denied
        </h1>

        <p className="text-sm sm:text-base text-gray-600 mb-8 px-2 sm:px-0">
          You don't have permission to access this page. Please contact your administrator if you believe this is an error.
        </p>

        <div className="flex flex-col sm:flex-row gap-3 sm:gap-4 justify-center">
          <button
            onClick={() => router.back()}
            className="w-full sm:w-auto px-6 py-2.5 text-sm sm:text-base bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors duration-200 font-medium"
          >
            Go Back
          </button>

          <Link
            href="/"
            className="w-full sm:w-auto px-6 py-2.5 text-sm sm:text-base bg-[#F69348] text-white rounded-lg hover:bg-[#e57f31] transition-colors duration-200 font-medium inline-block"
          >
            Go to Dashboard
          </Link>
        </div>
      </div>
    </div>
  );
}
