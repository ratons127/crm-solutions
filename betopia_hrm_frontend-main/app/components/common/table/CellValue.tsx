import { PropsWithChildren } from 'react';

export default function CellValue(props: PropsWithChildren) {
  return <span>{props.children === true ? 'Yes' : 'No'}</span>;
}
