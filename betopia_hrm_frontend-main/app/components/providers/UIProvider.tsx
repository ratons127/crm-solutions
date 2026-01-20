import { createTheme, MantineProvider } from '@mantine/core';
import { ModalsProvider } from '@mantine/modals';
import { Notifications } from '@mantine/notifications';
import { PropsWithChildren } from 'react';
const theme = createTheme({
  // color
  colors: {
    // custom orange palette (10 shades)
    orange: [
      '#FFF4E6', // 0
      '#FFE8CC', // 1
      '#FFD8A8', // 2
      '#FFC078', // 3
      '#FFA94D', // 4
      '#FF922B', // 5
      '#FD7E14', // 6
      '#F76707', // 7
      '#E8590C', // 8
      '#D9480F', // 9
    ],
  },
  primaryColor: 'orange', // use your orange as the primary color
  primaryShade: 6, // pick the default shade (here index 6 = "#FD7E14")

  /** Put your mantine theme override here */
  components: {
    // switch
    Switch: {
      defaultProps: {
        size: 'xs',
        // labelPosition: 'left',
      },
    },

    Select: {
      defaultProps: {
        size: 'xs',
      },
    },
    MultiSelect: {
      defaultProps: {
        size: 'xs',
      },
    },
    TextInput: {
      defaultProps: {
        size: 'xs',
      },
    },
    Textarea: {
      defaultProps: {
        size: 'xs',
      },
    },
    FileInput: {
      defaultProps: {
        size: 'xs',
      },
    },
    NumberInput: {
      defaultProps: {
        size: 'xs',
      },
    },
    DateInput: {
      defaultProps: {
        size: 'xs',
      },
    },
    // Modal
    Modal: {
      defaultProps: {
        closeOnClickOutside: false,
        size: 'lg',
        withCloseButton: false,
        padding: 20,
        centered: true,
      },
    },
    // Paper
    Paper: {
      defaultProps: {
        radius: 5,
        padding: 20,
      },
    },
  },
});
export default function UIProvider(props: PropsWithChildren) {
  return (
    <MantineProvider theme={theme}>
      <Notifications />
      <ModalsProvider>{props.children}</ModalsProvider>
    </MantineProvider>
  );
}
