'use client';

import { ActionIcon, Button, TextInput } from '@mantine/core';
import { useFormik } from 'formik';
import Image from 'next/image';
import { useSearchParams } from 'next/navigation';
import { Suspense, useState } from 'react';
import { FaEyeSlash, FaRegEye } from 'react-icons/fa6';
import * as Yup from 'yup';
import betopia_logo from '../../../../public/images/betopia-logo_01.png';
import dashboard_img from '../../../../public/images/dashboard_img.png';
import { resetPassword } from '../../../lib/features/auth/authAPI';
import { useAppDispatch } from '../../../lib/hooks';

interface ResetPasswordFormValues {
  newPassword: string;
  confirmPassword: string;
}

interface ResetPasswordPayload extends ResetPasswordFormValues {
  token: string;
}

function ResetPasswordForm() {
  const dispatch = useAppDispatch();
  const searchParams = useSearchParams();
  const [success, setSuccess] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  // Extract token from URL query params
  const token = searchParams.get('token') || '';

  // Formik setup
  const formik = useFormik<ResetPasswordFormValues>({
    initialValues: {
      newPassword: '',
      confirmPassword: '',
    },
    validationSchema: Yup.object({
      newPassword: Yup.string()
        .min(8, 'Password must be at least 8 characters')
        .required('Password is required'),
      confirmPassword: Yup.string()
        .oneOf([Yup.ref('newPassword')], 'Passwords must match')
        .required('Please confirm your password'),
    }),
    onSubmit: async values => {
      if (!token) {
        setError('Invalid reset link. Please request a new password reset.');
        setSuccess(null);
        return;
      }

      const payload: ResetPasswordPayload = { ...values, token };
      try {
        const res: any = await dispatch(resetPassword(payload) as any);
        if (res?.success) {
          setSuccess(res?.message);
          setError(null);
        } else {
          setError(res?.message);
          setSuccess(null);
        }
      } catch (err: any) {
        const apiError = err?.response?.data?.message;
        setError(apiError);
        setSuccess(null);
      }
    },
  });

  return (
    <div className="flex items-center justify-center w-full px-4 py-6 sm:py-8 sm:px-6 lg:px-8 xl:px-12">
      <div className="bg-white px-5 sm:px-8 md:px-10 lg:px-12 py-6 sm:py-8 md:py-10 lg:py-12 rounded-2xl shadow-md w-full max-w-md lg:max-w-lg">
        <div className="flex items-center justify-center py-3 sm:py-4 mb-1 sm:mb-2">
          <Image
            src={betopia_logo}
            width={150}
            height={50}
            alt="betopia_logo"
            className="w-28 sm:w-32 md:w-36 lg:w-40 xl:w-44 h-auto"
            style={{ height: 'auto' }}
            priority
          />
        </div>
        <h2 className="text-xl sm:text-2xl md:text-3xl lg:text-4xl font-bold mb-4 sm:mb-6 md:mb-8 text-gray-800 dark:text-white text-center">
          Reset Password
        </h2>

        {success ? (
          <p className="text-green-600 text-center text-xs sm:text-sm md:text-base lg:text-lg font-medium">
            {success}
          </p>
        ) : (
          <form
            onSubmit={formik.handleSubmit}
            className="space-y-4 sm:space-y-5 md:space-y-6"
            noValidate
          >
            <TextInput
              radius="md"
              variant="filled"
              label="New Password"
              type={showNewPassword ? 'text' : 'password'}
              name="newPassword"
              placeholder="Enter new password"
              value={formik.values.newPassword}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={
                formik.touched.newPassword && formik.errors.newPassword
                  ? formik.errors.newPassword
                  : ''
              }
              required
              rightSectionPointerEvents="auto"
              rightSection={
                <ActionIcon
                  variant="subtle"
                  color="gray"
                  aria-label={
                    showNewPassword ? 'Hide password' : 'Show password'
                  }
                  onClick={() => setShowNewPassword(v => !v)}
                >
                  {showNewPassword ? (
                    <FaEyeSlash size={15} />
                  ) : (
                    <FaRegEye size={15} />
                  )}
                </ActionIcon>
              }
              classNames={{
                input: 'text-xs sm:text-sm md:text-base lg:text-lg',
                label: 'text-xs sm:text-sm md:text-base lg:text-lg',
              }}
            />

            <TextInput
              radius="md"
              variant="filled"
              label="Confirm Password"
              type={showConfirmPassword ? 'text' : 'password'}
              name="confirmPassword"
              placeholder="Re-enter your password"
              value={formik.values.confirmPassword}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              error={
                formik.touched.confirmPassword && formik.errors.confirmPassword
                  ? formik.errors.confirmPassword
                  : ''
              }
              required
              rightSectionPointerEvents="auto"
              rightSection={
                <ActionIcon
                  variant="subtle"
                  color="gray"
                  aria-label={
                    showConfirmPassword ? 'Hide password' : 'Show password'
                  }
                  onClick={() => setShowConfirmPassword(v => !v)}
                >
                  {showConfirmPassword ? (
                    <FaEyeSlash size={15} />
                  ) : (
                    <FaRegEye size={15} />
                  )}
                </ActionIcon>
              }
              classNames={{
                input: 'text-xs sm:text-sm md:text-base lg:text-lg',
                label: 'text-xs sm:text-sm md:text-base lg:text-lg',
              }}
            />

            {error && (
              <p className="text-red-600 text-center text-xs sm:text-sm md:text-base">
                {error}
              </p>
            )}

            <Button radius="md" type="submit" fullWidth>
              Reset Password
            </Button>
          </form>
        )}

        <div className="mt-4 sm:mt-5 md:mt-6 text-center">
          <a
            href="/auth/login"
            className="text-sm text-blue-600 hover:underline"
          >
            Back to Login
          </a>
        </div>
      </div>
    </div>
  );
}

export default function ResetPasswordPage() {
  return (
    <div className="min-h-screen">
      <div className="grid grid-cols-1 lg:grid-cols-[40%_60%] min-h-screen">
        {/* Left Side - Dashboard Preview (Hidden on mobile) */}
        <div
          className="hidden lg:flex w-full flex-col justify-center items-center bg-cover bg-center bg-no-repeat p-4 xl:p-8 2xl:p-12"
          style={{ backgroundImage: 'url(/images/logging_Dashboard_img.png)' }}
        >
          <div className="w-full max-w-lg xl:max-w-xl 2xl:max-w-2xl">
            <Image
              src={dashboard_img}
              width={500}
              height={500}
              alt="dashboard_img"
              className="w-full h-auto"
              style={{ height: 'auto' }}
            />
          </div>
          <div className="flex flex-col items-center justify-center text-center py-4 lg:py-6 xl:py-8 px-4">
            <h5 className="text-base lg:text-lg xl:text-xl 2xl:text-2xl font-semibold text-white">
              The best HRM system you have ever used!
            </h5>
            <h6 className="text-base lg:text-lg xl:text-xl 2xl:text-2xl text-[#F69348] tracking-wider">
              A Betopia group product.
            </h6>
          </div>
        </div>

        {/* Right Side - Form */}
        <Suspense
          fallback={
            <div className="flex items-center justify-center min-h-screen text-xs sm:text-sm md:text-base">
              Loading...
            </div>
          }
        >
          <ResetPasswordForm />
        </Suspense>
      </div>
    </div>
  );
}
