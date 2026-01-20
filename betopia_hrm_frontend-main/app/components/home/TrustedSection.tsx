'use client';

import { Group, Text, ThemeIcon } from '@mantine/core';
import { FaCertificate, FaDatabase, FaLock, FaShieldAlt } from 'react-icons/fa';
import { LuShield } from 'react-icons/lu';
import { MdBook, MdStar } from 'react-icons/md';

export default function TrustedSection() {
  const features = [
    { icon: <FaLock size={18} />, label: 'MFA', color: 'blue' },
    { icon: <FaShieldAlt size={18} />, label: 'RBAC', color: 'pink' },
    {
      icon: <FaDatabase size={18} />,
      label: 'Data Encryption',
      color: 'green',
    },
    { icon: <FaCertificate size={18} />, label: 'GDPR', color: 'orange' },
    {
      icon: <MdBook size={18} />,
      label: 'BD Labour Act 2006',
      color: 'violet',
    },
  ];

  return (
    <section className="w-full py-20 bg-gradient-to-b from-white to-[#fafbff] text-center">
      {/* Trust Badge */}
      <div className="inline-flex items-center gap-2 border border-gray-200 rounded-full px-5 py-2 mx-auto">
        <MdStar color="gold" size={14} />
        <Text size="sm" fw={500}>
          Trusted by leading organizations
        </Text>
      </div>

      {/* Partner Logos */}
      <Group justify="center" mt="md" gap="xl" className="flex-wrap">
        <p className="font-semibold bg-gradient-to-r from-blue-500 to-sky-400 bg-clip-text text-transparent">
          Cams Biometrics
        </p>
        <p className="font-semibold bg-gradient-to-r from-violet-500 to-pink-400 bg-clip-text text-transparent">
          ZKTeco
        </p>
        <p className="font-semibold bg-gradient-to-r from-pink-500 to-rose-400 bg-clip-text text-transparent">
          Biomax
        </p>
        <p className="font-semibold bg-gradient-to-r from-orange-500 to-amber-400 bg-clip-text text-transparent">
          eSSL
        </p>
      </Group>

      {/* Feature Badges */}
      <Group justify="center" mt={40} gap="md" className="flex-wrap">
        {features.map((item, index) => (
          <div
            key={index}
            className="flex items-center gap-2 border-3 border-[#E5E7EB] rounded-full px-5 py-2.5 bg-white hover:shadow-lg transition"
          >
            <ThemeIcon
              variant="light"
              color={item.color}
              size={30}
              radius={100}
            >
              {item.icon}
            </ThemeIcon>
            <Text fw={500} size="sm">
              {item.label}
            </Text>
          </div>
        ))}
      </Group>

      {/* Compliance Card */}
      <div className="max-w-2xl mx-auto mt-14">
        <div
          className="
      bg-gradient-to-b from-white to-[#f9fbff]
      border-2 border-gray-100
      py-10 px-6
      rounded-3xl
      backdrop-blur-sm
      shadow-[0_8px_24px_rgba(0,0,0,0.05),_0_0_25px_#f0e9ff]
      hover:shadow-[0_12px_30px_rgba(0,0,0,0.08),_0_0_40px_#e4dbff]
      transition-all duration-300 ease-in-out
    "
        >
          <ThemeIcon
            color="orange"
            size={52}
            radius="md"
            className="mx-auto mb-4 shadow-[0_2px_10px_rgba(255,165,0,0.3)]"
          >
            <LuShield size={26} />
          </ThemeIcon>

          <Text size="lg" fw={500} className="text-gray-800">
            Built to comply with{' '}
            <span className="text-orange-500 font-semibold">
              Bangladesh Labour Act 2006
            </span>{' '}
            and{' '}
            <span className="text-orange-400 font-semibold">
              Labour Rules 2015
            </span>
          </Text>
        </div>
      </div>
    </section>
  );
}
