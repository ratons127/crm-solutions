'use client';

import { notifications } from '@mantine/notifications';

type NotificationStatus = 200 | 201 | number | boolean;

interface AppNotificationOptions {
  status?: NotificationStatus;
  message?: string;
  title?: string;
  onSuccess?: () => void;
  onError?: () => void;
}

/**
 * Handles API result notifications in a consistent, reusable way.
 */
export function showApiNotification({
  status,
  message,
  title = 'Notification',
  onSuccess,
  onError,
}: AppNotificationOptions) {
  if (status === 201) {
    notifications.show({
      title,
      message: message || 'Created successfully',
      color: 'green',
    });
    onSuccess?.();
  } else if (status === 200) {
    notifications.show({
      title,
      message: message || 'Updated successfully',
      color: 'blue',
    });
    onSuccess?.();
  } else {
    notifications.show({
      title,
      message: message || 'Something went wrong',
      color: 'red',
    });
    onError?.();
  }
}
