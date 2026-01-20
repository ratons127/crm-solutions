'use client';

import { Button, Container, Group, Image } from '@mantine/core';
import Link from 'next/link';

export default function Navbar() {
  return (
    <header className="bg-white sticky top-0 z-50">
      <Container size="xl" className="flex items-center justify-between py-3">
        {/* Left: Logo */}
        <Link href="/">
          <Image src="/images/betopia-logo_01.png" alt="Betopia Group" h={40} />
        </Link>

        {/* Center: Nav links */}
        <Group gap="xl" className="hidden md:flex">
          {['Products', 'Solutions', 'Resources', 'Pricing'].map(item => (
            <Link
              key={item}
              href={`/${item.toLowerCase()}`}
              className="text-sm text-gray-800 hover:text-orange-500 transition"
            >
              {item}
            </Link>
          ))}
        </Group>

        {/* Right: Auth + CTA */}
        <Group gap="sm">
          <Link
            href="/signin"
            className="text-sm text-gray-700 hover:text-orange-500 transition"
          >
            Sign in
          </Link>
          <Button
            variant="default"
            radius="md"
            component={Link}
            href="/contact"
            className="border-gray-300 hover:border-orange-400 text-sm"
          >
            Contact us
          </Button>
          <Button
            radius="md"
            component={Link}
            href="/get-started"
            className="bg-orange-500 hover:bg-orange-600 text-white text-sm px-5"
            rightSection={<span>→</span>}
          >
            Get started
          </Button>
        </Group>
      </Container>
    </header>
  );
}
