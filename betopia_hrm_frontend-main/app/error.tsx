'use client';

import { Button, Container, Title } from '@mantine/core';
import Link from 'next/link';
import { useEffect } from 'react';
import { TbAlertTriangle } from 'react-icons/tb';

export default function GlobalError({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  useEffect(() => {
    console.error('Global error caught:', error);
  }, [error]);

  const isDev = process.env.NODE_ENV === 'development';

  return (
    <div className="flex items-center justify-center min-h-screen bg-white px-6">
      <Container className="flex flex-col items-center text-center max-w-lg">
        {/* Icon */}
        <div className="mb-6">
          <TbAlertTriangle
            className="text-[80px] text-red-500"
            strokeWidth={1.5}
          />
        </div>

        {/* Title */}
        <Title
          order={1}
          className="text-3xl md:text-4xl font-semibold text-gray-900"
        >
          Something went wrong
        </Title>

        {/* Subtitle */}
        <p className="text-gray-500 text-base leading-relaxed max-w-md mt-3">
          {isDev
            ? 'An error occurred while rendering this page.'
            : 'We encountered an unexpected error. Please try again.'}
        </p>

        {/* Error Message (visible in dev mode only) */}
        {isDev && error.message && (
          <div className="w-full p-4 bg-gray-50 rounded-lg border border-gray-200 mt-3">
            <p className="text-sm text-gray-700 font-mono text-left break-words">
              {error.message}
            </p>
          </div>
        )}

        {/* Buttons */}
        <div className="flex gap-3 mt-5">
          <Button
            size="md"
            radius="md"
            className="bg-gray-900 hover:bg-gray-800 text-white font-medium px-6"
            onClick={() => reset()}
          >
            Try again
          </Button>

          <Link href="/">
            <Button
              size="md"
              radius="md"
              variant="outline"
              className="border-gray-300 text-gray-700 hover:bg-gray-50 font-medium px-6"
            >
              Go home
            </Button>
          </Link>
        </div>
      </Container>
    </div>
  );
}
