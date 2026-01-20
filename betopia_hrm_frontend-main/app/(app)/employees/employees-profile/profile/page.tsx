'use client';

import { useAppState } from '@/lib/features/app/appSlice';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';

export default function Page() {
  const { auth } = useAppState();
  const router = useRouter();

  useEffect(() => {
    if (!!auth?.user?.employeeId) {
      router.push(
        `/employees/employees-profile/profile/${auth.user?.employeeId}/view`
      );
    }
  }, [auth]);

  return <div>{auth?.user?.employeeId ? '' : 'You are not an employee.'}</div>;
}
