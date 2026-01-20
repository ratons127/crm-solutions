import { PropsWithChildren } from 'react';
import StoreProvider from '../StoreProvider';
import PresentationProvider from './PresentationProvider';
import UIProvider from './UIProvider';
import AuthHydrator from './AuthHydrator';

export default function RootProvider(props: PropsWithChildren) {
  return (
    <StoreProvider>
      <UIProvider>
        <AuthHydrator />
        <PresentationProvider />
        {props.children}
      </UIProvider>
    </StoreProvider>
  );
}
