'use client';

import { FormikHelpers, useFormik } from 'formik';
import { useEffect } from 'react';
import * as Yup from 'yup';
import { forgotPassword } from '../../../lib/features/auth/authAPI';
import { clearAuthMessages } from '../../../lib/features/auth/authSlice';
import { useAppDispatch, useAppSelector } from '../../../lib/hooks';

import { Button, TextInput } from '@mantine/core';
import Image from 'next/image';
import Link from 'next/link';
import betopia_logo from '../../../../public/images/betopia-logo_01.png';
import dashboard_img from '../../../../public/images/dashboard_img.png';

interface ForgotPasswordFormValues {
  email: string;
}

export default function ForgotPasswordPage() {
  const dispatch = useAppDispatch();
  const { loading, error, success } = useAppSelector(state => state.auth);

  useEffect(() => {
    // Clean up messages on unmount
    return () => {
      dispatch(clearAuthMessages());
    };
  }, [dispatch]);

  const formik = useFormik<ForgotPasswordFormValues>({
    initialValues: { email: '' },
    validationSchema: Yup.object({
      email: Yup.string().email('Invalid email').required('Email is required'),
    }),
    onSubmit: async (
      values: ForgotPasswordFormValues,
      { setSubmitting }: FormikHelpers<ForgotPasswordFormValues>
    ) => {
      await dispatch(forgotPassword(values.email));
      setSubmitting(false);
    },
  });

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
              Forgot Password
            </h2>

            {success ? (
              <p className="text-center text-green-600 font-medium text-xs sm:text-sm md:text-base lg:text-lg">
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
                  label="Email"
                  type="email"
                  name="email"
                  placeholder="Enter your email address"
                  value={formik.values.email}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={
                    formik.touched.email && formik.errors.email
                      ? formik.errors.email
                      : ''
                  }
                  required
                  classNames={{
                    input: 'text-xs sm:text-sm md:text-base lg:text-lg',
                    label: 'text-xs sm:text-sm md:text-base lg:text-lg',
                  }}
                />

                {error && (
                  <p className="text-xs sm:text-sm md:text-base text-red-500 text-center">
                    {error}
                  </p>
                )}

                <Button
                  radius="md"
                  type="submit"
                  loading={loading}
                  fullWidth
                  disabled={loading}
                  variant="primary"
                >
                  Send Reset Link
                </Button>
              </form>
            )}

            <div className="mt-4 sm:mt-5 md:mt-6 text-center">
              <Link
                href="/auth/login"
                className="text-sm text-blue-600 hover:underline"
              >
                Back to Login
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
