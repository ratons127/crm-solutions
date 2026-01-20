'use client';

import { changePassword } from '@/lib/features/auth/authAPI';
import type { AppDispatch } from '@/lib/store';
import { Button, LoadingOverlay, PasswordInput } from '@mantine/core';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import Link from 'next/link';
import { useParams, useRouter } from 'next/navigation';
import { useState } from 'react';
import { MdArrowBack } from 'react-icons/md';
import { RiLockPasswordLine } from 'react-icons/ri';
import { useDispatch } from 'react-redux';

interface PasswordFormValues {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

export default function ChangePasswordPage() {
  const { id }: { id: string } = useParams();
  const router = useRouter();
  const dispatch = useDispatch<AppDispatch>();
  const [loading, setLoading] = useState(false);

  const form = useForm<PasswordFormValues>({
    initialValues: {
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
    },
    validate: {
      currentPassword: value =>
        value.trim().length < 1 ? 'Current password is required' : null,
      newPassword: value => {
        if (value.length < 8) {
          return 'Password must be at least 8 characters long';
        }
        if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(value)) {
          return 'Password must contain uppercase, lowercase, and number';
        }
        return null;
      },
      confirmPassword: (value, values) =>
        value !== values.newPassword ? 'Passwords do not match' : null,
    },
  });

  const handleSubmit = async (values: PasswordFormValues) => {
    setLoading(true);
    try {
      const res = await dispatch(changePassword(values) as any);
      const { success, message } = res;

      if (success) {
        notifications.show({
          title: 'Success',
          message: message || 'Password changed successfully!',
          color: 'green',
        });
        form.reset();
        router.push(`/employees/employees-profile/profile/${id}/view`);
      } else {
        notifications.show({
          title: 'Error',
          message: message || 'Failed to change password. Please try again.',
          color: 'red',
        });
      }
    } catch (err: any) {
      notifications.show({
        title: 'Error',
        message:
          err?.response?.data?.message ||
          'Something went wrong. Please try again later.',
        color: 'red',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white w-full rounded-lg shadow-lg px-5 py-5 relative">
      <LoadingOverlay visible={loading} />

      {/* Header */}
      <div className="flex items-center gap-4 mb-6 pb-4 border-b border-gray-200">
        <Link href={`/employees/employees-profile/profile/${id}/view`}>
          <Button
            variant="subtle"
            color="gray"
            leftSection={<MdArrowBack size={18} />}
            size="sm"
          >
            Back
          </Button>
        </Link>
        <div className="flex items-center gap-3">
          <RiLockPasswordLine size={28} className="text-orange-500" />
          <h1 className="text-[28px] md:text-[32px] text-[#1D293D] font-bold">
            Change Password
          </h1>
        </div>
      </div>

      {/* Form */}
      <div className="max-w-2xl">
        <form onSubmit={form.onSubmit(handleSubmit)} className="space-y-6">
          <PasswordInput
            label="Current Password"
            placeholder="Enter your current password"
            required
            size="sm"
            radius="md"
            variant="filled"
            {...form.getInputProps('currentPassword')}
          />

          <PasswordInput
            label="New Password"
            placeholder="Enter your new password"
            required
            size="sm"
            radius="md"
            variant="filled"
            {...form.getInputProps('newPassword')}
          />

          <PasswordInput
            label="Confirm New Password"
            placeholder="Confirm your new password"
            required
            size="sm"
            radius="md"
            variant="filled"
            {...form.getInputProps('confirmPassword')}
          />

          <div className="flex items-center gap-4 pt-4">
            <Button
              type="submit"
              size="md"
              variant="filled"
              color="orange"
              leftSection={<RiLockPasswordLine size={16} />}
              loading={loading}
            >
              Change Password
            </Button>
            <Link href={`/employees/employees-profile/profile/${id}/view`}>
              <Button type="button" size="md" variant="outline" color="gray">
                Cancel
              </Button>
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}
