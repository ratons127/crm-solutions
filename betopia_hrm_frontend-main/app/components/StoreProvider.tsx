'use client';

import React, { useRef } from 'react';
import { Provider } from 'react-redux';
import { makeStore } from '../lib/store';

type Props = { children: React.ReactNode };
type AppStore = ReturnType<typeof makeStore>;

export default function StoreProvider({ children }: Props) {
  const storeRef = useRef<AppStore | null>(null);

  // Create the store once
  const store = storeRef.current ?? (storeRef.current = makeStore());

  return <Provider store={store}>{children}</Provider>;
}
