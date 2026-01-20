'use client';

import { Button, TextInput } from '@mantine/core';
import { FormikHelpers, useFormik } from 'formik';
import Image from 'next/image';
import { useState } from 'react';
import * as Yup from 'yup';
import betopia_logo from '../../../../public/images/betopia-logo_01.png';
import dashboard_img from '../../../../public/images/dashboard_img.png';
import { registerUser } from '../../../lib/features/auth/authAPI';
import { useAppDispatch, useAppSelector } from '../../../lib/hooks';

interface RegisterFormValues {
  name: string;
  email: string;
  password: string;
  password_confirmation: string;
}

export default function RegisterPage() {
  const dispatch = useAppDispatch();
  const auth = useAppSelector(state => state.auth);
  const [submitted, setSubmitted] = useState(false);

  const formik = useFormik<RegisterFormValues>({
    initialValues: {
      name: '',
      email: '',
      password: '',
      password_confirmation: '',
    },
    validationSchema: Yup.object({
      name: Yup.string().required('Full name is required'),
      email: Yup.string()
        .email('Invalid email address')
        .required('Email is required'),
      password: Yup.string()
        .min(6, 'Password must be at least 6 characters')
        .required('Password is required'),
      password_confirmation: Yup.string()
        .oneOf([Yup.ref('password')], 'Passwords must match')
        .required('Please confirm your password'),
    }),
    onSubmit: async (
      values: RegisterFormValues,
      { setSubmitting }: FormikHelpers<RegisterFormValues>
    ) => {
      await dispatch(registerUser(values as any));
      setSubmitted(true);
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
              style={{ height: "auto" }}
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
          <div className="bg-white dark:bg-gray-900 bg-opacity-90 px-5 sm:px-8 md:px-10 lg:px-12 py-6 sm:py-8 md:py-10 lg:py-12 rounded-2xl shadow-md w-full max-w-md lg:max-w-lg">
            <div className="flex items-center justify-center py-3 sm:py-4 mb-1 sm:mb-2">
              <Image
                src={betopia_logo}
                width={150}
                height={50}
                alt="betopia_logo"
                className="w-28 sm:w-32 md:w-36 lg:w-40 xl:w-44 h-auto"
                style={{ height: "auto" }}
                priority
              />
            </div>
            <h2 className="text-xl sm:text-2xl md:text-3xl lg:text-4xl font-bold mb-4 sm:mb-6 md:mb-8 text-gray-800 dark:text-white text-center">Register</h2>

            {submitted && !auth.error ? (
              <p className="text-center text-green-600 font-medium text-xs sm:text-sm md:text-base lg:text-lg">
                Registration successful! Please check your email to verify your
                account.
              </p>
            ) : (
              <form
                onSubmit={formik.handleSubmit}
                className="space-y-4 sm:space-y-5 md:space-y-6"
                noValidate
              >
                <TextInput
                  variant="filled"
                  label="Name"
                  name="name"
                  radius="md"
                  placeholder="Enter your full name"
                  value={formik.values.name}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={
                    formik.touched.name && formik.errors.name
                      ? formik.errors.name
                      : ''
                  }
                  required
                  classNames={{
                    input: 'text-xs sm:text-sm md:text-base lg:text-lg',
                    label: 'text-xs sm:text-sm md:text-base lg:text-lg'
                  }}
                />

                <TextInput
                  radius="md"
                  variant="filled"
                  label="Email"
                  type="email"
                  name="email"
                  placeholder="Enter your email"
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
                    label: 'text-xs sm:text-sm md:text-base lg:text-lg'
                  }}
                />

                <TextInput
                  radius="md"
                  variant="filled"
                  label="Password"
                  type="password"
                  name="password"
                  placeholder="Create a password"
                  value={formik.values.password}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={
                    formik.touched.password && formik.errors.password
                      ? formik.errors.password
                      : ''
                  }
                  required
                  classNames={{
                    input: 'text-xs sm:text-sm md:text-base lg:text-lg',
                    label: 'text-xs sm:text-sm md:text-base lg:text-lg'
                  }}
                />

                <TextInput
                  radius="md"
                  variant="filled"
                  label="Confirmation Password"
                  type="password"
                  name="password_confirmation"
                  placeholder="Re-enter your password"
                  value={formik.values.password_confirmation}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  error={
                    formik.touched.password_confirmation &&
                    formik.errors.password_confirmation
                      ? formik.errors.password_confirmation
                      : ''
                  }
                  required
                  classNames={{
                    input: 'text-xs sm:text-sm md:text-base lg:text-lg',
                    label: 'text-xs sm:text-sm md:text-base lg:text-lg'
                  }}
                />

                <Button
                  radius="md"
                  type="submit"
                  loading={auth.loading}
                  fullWidth
                  disabled={auth.loading}
                  variant="primary"
                  classNames={{
                    label: 'text-xs sm:text-sm md:text-base lg:text-lg font-medium',
                    root: 'bg-gradient-to-t! from-slate-900 to-slate-700 text-white hover:from-slate-800 hover:to-slate-600 transition-all duration-200 h-10 sm:h-11 md:h-12 lg:h-14',
                  }}
                >
                  Register
                </Button>
              </form>
            )}

            {auth.error && (
              <p className="mt-3 sm:mt-4 text-center text-xs sm:text-sm md:text-base text-red-500">{auth.error}</p>
            )}

            <div className="mt-4 sm:mt-5 md:mt-6 text-center">
              <a
                href="/auth/login"
                className="text-xs sm:text-sm md:text-base lg:text-lg text-blue-600 hover:text-blue-800 hover:underline transition duration-200"
              >
                Already have an account?{' '}
                <span className="font-semibold">Log in</span>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
