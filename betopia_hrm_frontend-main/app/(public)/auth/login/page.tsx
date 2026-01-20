'use client';

import { Button, PasswordInput, TextInput } from '@mantine/core';
import { useFormik } from 'formik';
import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';
import { FaRegEye } from 'react-icons/fa';
import { FaEyeSlash } from 'react-icons/fa6';
import * as Yup from 'yup';
import betopia_logo from '../../../../public/images/betopia-logo_01.png';
import dashboard_img from '../../../../public/images/dashboard_img.png';
import { loginUser } from '../../../lib/features/auth/authAPI';
import { useAppDispatch, useAppSelector } from '../../../lib/hooks';

// simple validators for UX hints (backend still receives raw "identifier")
const emailRe = /\S+@\S+\.\S+/;
const phoneRe = /^[\d+()\-\s]{6,}$/; // allows +880..., 017..., spaces, dashes, etc.

export default function LoginPage() {
  const dispatch = useAppDispatch();
  const auth = useAppSelector(state => state.auth);
  const router = useRouter();

  const formik = useFormik({
    initialValues: {
      identifier: '', // <-- email or phone
      password: '',
      rememberMe: false,
    },
    validationSchema: Yup.object({
      identifier: Yup.string()
        .required('Email or phone is required')
        .test(
          'email-or-phone',
          'Enter a valid email or phone number',
          v => !!v && (emailRe.test(v) || phoneRe.test(v))
        ),
      password: Yup.string()
        .min(8, 'Password must be at least 8 characters')
        .required('Password is required'),
    }),
    onSubmit: values => {
      // Send single identifier to backend
      dispatch(
        loginUser({
          identifier: values.identifier,
          password: values.password,
          // pass rememberMe if you want to store token in cookies/localStorage
        } as any)
      );
      if (values.rememberMe) {
        localStorage.setItem('rememberMe', 'true');
      } else {
        localStorage.removeItem('rememberMe');
      }
    },
  });

  // Redirect if logged in
  useEffect(() => {
    if (auth.user && auth.token) {
      router.push('/');
    }
  }, [auth.user, auth.token, router]);

  return (
    <div>
      <div className="grid grid-cols-1 lg:grid-cols-[40%_60%] px-0">
        {/*  */}
        <div
          className="hidden lg:flex min-h-screen w-full flex-col justify-center items-center bg-cover bg-center bg-no-repeat"
          style={{ backgroundImage: 'url(/images/logging_Dashboard_img.png)' }}
        >
          <div>
            <Image
              src={dashboard_img}
              width={500}
              height={500}
              alt="dashboard_img"
              style={{ height: 'auto' }}
            />
          </div>
          <div className="flex flex-col items-center justify-center text-center py-6 ">
            <h5 className="text-[20px] font-semibold text-white ">
              The best HRM system you have ever used!
            </h5>
            <h6 className="text-[20px] text-[#F69348] tracking-wider ">
              A Betopia group product.
            </h6>
          </div>
        </div>
        {/*  */}
        <div className="flex items-center justify-center min-h-screen px-4 py-8">
          <div className="bg-white bg-opacity-90 px-4 sm:px-6 md:px-8 py-8 rounded-2xl shadow-md max-w-md w-full">
            <div className="flex items-center justify-center py-4">
              <Image
                src={betopia_logo}
                width={150}
                height={50}
                alt="betopia_logo"
                className="w-32 sm:w-40 h-auto"
                style={{ height: 'auto' }}
                priority
              />
            </div>
            <h2 className="text-2xl sm:text-3xl font-bold mb-6 text-gray-800">
              Login
            </h2>

            <form onSubmit={formik.handleSubmit} className="space-y-6">
              <TextInput
                radius="md"
                variant="filled"
                label="Email or Phone"
                type="text"
                name="identifier"
                placeholder="Email or Phone"
                value={formik.values.identifier}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={
                  formik.touched.identifier
                    ? (formik.errors.identifier as string)
                    : ''
                }
                required
              />
              {/* <TextInput
                radius="md"
                variant="filled"
                label="Password"
                type="password"
                name="password"
                placeholder="Enter your password"
                value={formik.values.password}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={
                  formik.touched.password
                    ? (formik.errors.password as string)
                    : ''
                }
                required
              /> */}
              <PasswordInput
                radius="md"
                size="xs"
                variant="filled"
                label="Password"
                name="password"
                placeholder="Enter your password"
                value={formik.values.password}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                visibilityToggleIcon={({ reveal }) =>
                  reveal ? (
                    <FaEyeSlash
                      size={15}
                      className="text-[#94A3B8] cursor-pointer"
                    />
                  ) : (
                    <FaRegEye
                      size={15}
                      className="text-[#94A3B8] cursor-pointer"
                    />
                  )
                } // optional custom icons
                error={
                  formik.touched.password
                    ? (formik.errors.password as string)
                    : ''
                }
                required
              />

              <Button
                radius="md"
                type="submit"
                loading={auth.loading}
                fullWidth
                variant="primary"
                size="sm"
              >
                Login
              </Button>
            </form>
            <Link
              href="/auth/forgot-password"
              className="text-sm text-blue-600 hover:underline flex justify-end mt-3"
            >
              Forgot Password?
            </Link>
            {auth.loading && (
              <p className="mt-4 text-center text-gray-700">Loading...</p>
            )}
            {auth.error && (
              <p className="mt-4 text-center text-red-500">{auth.error}</p>
            )}

            {/* <div className="flex flex-col justify-end items-center mt-5">
              <Link
                href="/auth/registration"
                className="text-sm text-blue-600 hover:underline hover:text-blue-800 transition duration-200"
              >
                Not a member? <span className="font-semibold">Sign up now</span>
              </Link>
            </div> */}
          </div>
        </div>
      </div>
    </div>
  );
}
