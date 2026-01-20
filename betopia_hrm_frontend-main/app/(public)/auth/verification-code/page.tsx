'use client';

import { Button, PinInput } from '@mantine/core';
import Image from 'next/image';
import { Suspense, useEffect, useState } from 'react';
import betopia_logo from '../../../../public/images/betopia-logo_01.png';
import dashboard_img from '../../../../public/images/dashboard_img.png';
import { clearAuthMessages } from '../../../lib/features/auth/authSlice';
import { useAppDispatch, useAppSelector } from '../../../lib/hooks';

function VerificationCodeForm() {
  const dispatch = useAppDispatch();
  // const searchParams = useSearchParams();
  const { loading, error, success } = useAppSelector(state => state.auth);
  const [verificationCode, setVerificationCode] = useState('');
  const [localError, setLocalError] = useState<string | null>(null);

  // Get email from query params if available
  // const email = searchParams.get('email') || '';

  useEffect(() => {
    // Clean up messages on unmount
    return () => {
      dispatch(clearAuthMessages());
    };
  }, [dispatch]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (verificationCode.length !== 4) {
      setLocalError('Please enter a valid 4-digit code');
      return;
    }

    setLocalError(null);

    // TODO: Dispatch verification action with code and email
    // await dispatch(verifyCode({ email, code: verificationCode }));

    // For now, simulate success and redirect to reset password
    //  console.log('Verification code:', verificationCode, 'Email:', email);
    // router.push(`/auth/reset-password?token=${verificationCode}&email=${email}`);
  };

  return (
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

        <h2 className="text-xl sm:text-2xl md:text-3xl lg:text-4xl font-bold mb-3 sm:mb-4 text-gray-800 dark:text-white text-center">
          Verification Code
        </h2>

        <p className="text-xs sm:text-sm md:text-base lg:text-lg text-gray-600 dark:text-gray-400 text-center mb-4 sm:mb-6 md:mb-8">
          Enter the 4-digit code sent to your email
        </p>

        {success ? (
          <p className="text-center text-green-600 font-medium text-xs sm:text-sm md:text-base lg:text-lg">
            {success}
          </p>
        ) : (
          <form
            onSubmit={handleSubmit}
            className="space-y-4 sm:space-y-5 md:space-y-6"
            noValidate
          >
            <div className="flex items-center justify-center">
              <PinInput
                type={/^[0-9]*$/}
                inputType="tel"
                inputMode="numeric"
                length={4}
                value={verificationCode}
                onChange={setVerificationCode}
                classNames={{
                  input: 'border-gray-300 dark:border-gray-600 dark:bg-gray-800 dark:text-white rounded-lg text-center text-base sm:text-lg md:text-xl lg:text-2xl font-medium focus:border-blue-500 focus:ring-2 focus:ring-blue-200 w-11 h-11 sm:w-12 sm:h-12 md:w-14 md:h-14 lg:w-16 lg:h-16',
                }}
              />
            </div>
            {(localError || error) && (
              <p className="text-xs sm:text-sm md:text-base text-red-500 text-center">{localError || error}</p>
            )}
            <Button
              radius="md"
              type="submit"
              loading={loading}
              fullWidth
              disabled={loading || verificationCode.length !== 4}
              variant="primary"
              classNames={{
                label: 'text-xs sm:text-sm md:text-base lg:text-lg font-medium',
                root: 'bg-gradient-to-t from-slate-900 to-slate-700 text-white hover:from-slate-800 hover:to-slate-600 transition-all duration-200 h-10 sm:h-11 md:h-12 lg:h-14',
              }}
            >
              Verify Code
            </Button>
          </form>
        )}

        <div className="mt-4 sm:mt-5 md:mt-6 text-center">
          <a
            href="/auth/login"
            className="text-xs sm:text-sm md:text-base lg:text-lg text-blue-600 hover:text-blue-800 hover:underline transition duration-200"
          >
            Back to Login
          </a>
        </div>
      </div>
    </div>
  );
}

export default function VerificationCodePage() {
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
        <Suspense fallback={<div className="flex items-center justify-center min-h-screen text-xs sm:text-sm md:text-base">Loading...</div>}>
          <VerificationCodeForm />
        </Suspense>
      </div>
    </div>
  );
}
