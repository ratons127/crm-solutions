import { ActionIcon, Flex, Menu } from '@mantine/core';
import { ReactNode } from 'react';

interface RowActionDropdownProps {
  children: ReactNode;
  data: {
    label: string;
    icon?: ReactNode;
    action: () => void | Promise<void>;
    disabled?: boolean;
  }[];
}

export default function RowActionDropdown(props: RowActionDropdownProps) {
  return (
    <Menu radius={5} position="bottom-end">
      <Menu.Target>
        <ActionIcon variant="subtle">{props.children}</ActionIcon>
      </Menu.Target>
      <Menu.Dropdown classNames={{ dropdown: 'shadow-xl!' }}>
        {props.data.map((x, i) => (
          <Menu.Item
            key={i}
            disabled={!!x.disabled}
            onClick={() => {
              if (!x.disabled) x.action();
            }}
          >
            <Flex gap={10} align={'center'}>
              {x.icon ?? null}
              {x.label}
            </Flex>
          </Menu.Item>
        ))}
      </Menu.Dropdown>
    </Menu>
  );
}
