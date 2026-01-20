import { PropsWithChildren } from 'react';

export default function ProfileLayout({ children }: PropsWithChildren) {
  return <div className=" bg-white py-10 px-5 rounded-2xl">{children}</div>;
}
