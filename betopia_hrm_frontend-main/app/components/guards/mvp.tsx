import { ReactNode } from 'react';

export default function NonMVP(props: { children: ReactNode }) {
  if (process.env.NEXT_PUBLIC_MVP_ENABLED) return null;

  return props.children;
}
