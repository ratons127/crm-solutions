'use client';
import { enableIndexedDBKeyPersistence } from '@/lib/utils/secureStorage';

// Enable persistent, non-extractable CryptoKey across reloads
enableIndexedDBKeyPersistence(true);
import { PropsWithChildren } from 'react';
import AlertProvider from '../common/Alert';
import ModalProvider from '../common/modalProvider';

export default function PresentationProvider({ children }: PropsWithChildren) {
  return (
    <div>
      <AlertProvider />
      <ModalProvider />
      {children}
    </div>
  );
}
