'use client';

import { useState } from 'react';
import Breadcrumbs from '../../components/common/Breadcrumbs';

interface Notification {
  id: number;
  message: string;
  type: string;
  time: string;
  status: 'Read' | 'Unread';
}

export default function NotificationsPage() {
  const [notifications] = useState<Notification[]>([
    {
      id: 1,
      message: 'New user registered 🎉',
      type: 'System',
      time: '2 min ago',
      status: 'Unread',
    },
    {
      id: 2,
      message: 'Server restarted successfully ✅',
      type: 'System',
      time: '10 min ago',
      status: 'Read',
    },
    {
      id: 3,
      message: 'You have 3 pending tasks 📌',
      type: 'Task',
      time: '1 hr ago',
      status: 'Unread',
    },
    {
      id: 4,
      message: 'Password updated successfully 🔒',
      type: 'Security',
      time: '2 hrs ago',
      status: 'Read',
    },
    {
      id: 5,
      message: 'New comment on your post 💬',
      type: 'Social',
      time: 'Yesterday',
      status: 'Unread',
    },
  ]);

  return (
    <div>
      {/* Card Container */}
      <div className="bg-white rounded-lg shadow-sm p-4">
        {/* Top Bar: Breadcrumbs */}
        <div className="mb-3">
          <Breadcrumbs />
        </div>

        {/* Table */}
        <div className="overflow-x-auto">
          <table className="min-w-full border border-gray-200 text-sm rounded-md">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-3 py-1.5 text-left text-sm font-medium text-gray-600">
                  Message
                </th>
                <th className="px-3 py-1.5 text-left text-sm font-medium text-gray-600">
                  Type
                </th>
                <th className="px-3 py-1.5 text-left text-sm font-medium text-gray-600">
                  Time
                </th>
                <th className="px-3 py-1.5 text-left text-sm font-medium text-gray-600">
                  Status
                </th>
                <th className="px-3 py-1.5 text-right text-sm font-medium text-gray-600">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {notifications.map(notif => (
                <tr
                  key={notif.id}
                  className="hover:bg-gray-50 transition-colors"
                >
                  <td className="px-3 py-1.5 text-gray-800">
                    {notif.message}
                  </td>
                  <td className="px-3 py-1.5 text-gray-600">
                    {notif.type}
                  </td>
                  <td className="px-3 py-1.5 text-gray-600 whitespace-nowrap">
                    {notif.time}
                  </td>
                  <td className="px-3 py-1.5">
                    {notif.status === 'Unread' ? (
                      <span className="inline-block px-2 py-0.5 text-xs rounded bg-blue-100 text-blue-700 font-medium">
                        Unread
                      </span>
                    ) : (
                      <span className="inline-block px-2 py-0.5 text-xs rounded bg-gray-100 text-gray-600 font-medium">
                        Read
                      </span>
                    )}
                  </td>
                  <td className="px-3 py-1.5 text-right">
                    <button className="text-gray-500 hover:text-gray-700 p-1 rounded hover:bg-gray-100 transition-colors">
                      •••
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
