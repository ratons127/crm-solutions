'use client';

import { Button, Container, Title } from '@mantine/core';
import { useRouter } from 'next/navigation';
import { TbError404 } from 'react-icons/tb';

export default function NotFoundPage() {
  const router = useRouter();

  return (
    <div className="relative flex items-center justify-center min-h-screen overflow-hidden bg-gradient-to-br from-orange-100 via-white to-purple-200 px-6">
      {/* Decorative background circle */}
      <div className="absolute -top-40 -left-40 w-80 h-80 bg-indigo-200/30 rounded-full blur-3xl" />
      <div className="absolute -bottom-40 -right-40 w-96 h-96 bg-purple-300/30 rounded-full blur-3xl" />

      <Container className="relative z-10 flex flex-col items-center text-center">
        {/* 404 Icon */}
        <div className="flex items-center justify-center mb-4">
          <TbError404 className="text-[150px] md:text-[180px] text-primary drop-shadow-md" />
        </div>

        {/* Title */}
        <Title order={2} className="text-3xl md:text-5xl font-extrabold">
          Oops! Page Not Found
        </Title>

        {/* Description */}
        <p className="text-gray-600 max-w-md mt-4 leading-relaxed">
          The page you’re looking for doesn’t exist, or it might have been
          moved. Let’s help you find your way back home.
        </p>

        {/* Action Buttons */}
        <div className="flex flex-wrap gap-4 justify-center mt-8">
          <Button
            size="md"
            radius="xl"
            className="px-6 shadow hover:shadow-lg transition-all duration-200"
            onClick={() => router.back()}
          >
            ← Go Back
          </Button>
          <Button
            size="md"
            radius="xl"
            variant="outline"
            color="gray"
            className="px-6 shadow-sm hover:shadow-md transition-all duration-200"
            onClick={() => router.push('/')}
          >
            Go Home
          </Button>
        </div>
      </Container>
    </div>
  );
}
