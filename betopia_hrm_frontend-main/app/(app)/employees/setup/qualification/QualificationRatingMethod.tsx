'use client';

import Breadcrumbs from '@/components/common/Breadcrumbs';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import RowActionDropdown from '@/components/common/table/RowActionDropdown';
import {
  useDeleteQualificationRatingMutation,
  useGetQualificationRatingQuery,
} from '@/lib/features/qualification/qualificationRatingAPI';
import { QualificationRatingMethod } from '@/lib/types/qualification';
import { PencilSquareIcon, TrashIcon } from '@heroicons/react/24/outline';
import { PlusIcon } from '@heroicons/react/24/solid';
import {
  Accordion,
  Box,
  Button,
  Card,
  Group,
  Modal,
  Table,
  Text,
} from '@mantine/core';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { useCallback, useMemo, useState } from 'react';
import { BsThreeDots } from 'react-icons/bs';
import QualificationRatingForm from './QualificationRatingForm';
import QualificationRatingMethodForm from './QualificationRatingMethodForm';
import RatingDetailsTable from './RatingDetailsTable';

type ModalState = {
  type: 'create' | 'add-more' | 'update' | null;
  item: QualificationRatingMethod | null;
};

export default function QualificationRatingMethodPage() {
  const { data, isLoading } = useGetQualificationRatingQuery(undefined);
  const [modalState, setModalState] = useState<ModalState>({
    type: null,
    item: null,
  });
  const [deleteItem] = useDeleteQualificationRatingMutation();

  const methods = useMemo<QualificationRatingMethod[]>(
    () => data?.data || [],
    [data?.data]
  );

  const handleCreate = () => {
    setModalState({ type: 'create', item: null });
  };

  const handleAddMore = (method: QualificationRatingMethod) => {
    setModalState({ type: 'add-more', item: method });
  };

  const handleEdit = (method: QualificationRatingMethod) => {
    setModalState({ type: 'update', item: method });
  };

  const handleCloseModal = () => {
    setModalState({ type: null, item: null });
  };

  const handleDelete = useCallback(
    async (method: QualificationRatingMethod) => {
      modals.openConfirmModal({
        title: 'Delete confirmation',
        centered: true,
        children: (
          <Text size="sm">
            Are you sure you want to delete? This action is destructive and you
            will have to contact support to restore your data.
          </Text>
        ),
        labels: {
          confirm: 'Delete',
          cancel: 'Cancel',
        },
        size: 'lg',
        confirmProps: { color: 'red' },
        onConfirm: async () => {
          try {
            await deleteItem({ id: method.id });
            notifications.show({
              title: 'Qualification Rating Deleted successfully',
              message: '',
              color: 'red',
            });
          } catch (error) {
            notifications.show({
              title: 'Failed to delete',
              message: 'Something went wrong',
              color: 'red',
            });
          }
        },
      });
    },
    [deleteItem]
  );

  if (isLoading) {
    return (
      <Group justify="center" mt="xl">
        <LoadingSpinner />
      </Group>
    );
  }

  return (
    <>
      <div className="flex justify-between items-center">
        <Breadcrumbs />
        <Button onClick={handleCreate}>Add</Button>
      </div>
      <Card shadow="sm" p="md" radius="md" withBorder className="mt-4">
        {methods.length > 0 ? (
          <Table withRowBorders={false} highlightOnHover>
            <Table.Thead>
              <Table.Tr>
                <Table.Th style={{ width: 60 }}>Sl.</Table.Th>
                <Table.Th>Method</Table.Th>
                <Table.Th style={{ width: 60 }} />
              </Table.Tr>
            </Table.Thead>

            <Table.Tbody>
              {methods.map((method, index) => (
                <Table.Tr key={method.id}>
                  <Table.Td>{index + 1}.</Table.Td>
                  <Table.Td>
                    <Accordion variant="contained" chevronPosition="left">
                      <Accordion.Item value={method.methodName}>
                        <Accordion.Control>
                          <Text className="capitalize">
                            {method.methodName}
                          </Text>
                        </Accordion.Control>
                        <Accordion.Panel>
                          <Box mt="xs">
                            <Group justify="space-between" mb="sm">
                              <Text fw={600}>Grade Details</Text>
                              <Button
                                onClick={() => handleAddMore(method)}
                                size="xs"
                                leftSection={
                                  <PlusIcon width={14} height={14} />
                                }
                                variant="default"
                              >
                                Add
                              </Button>
                            </Group>

                            <RatingDetailsTable details={method} />
                          </Box>
                        </Accordion.Panel>
                      </Accordion.Item>
                    </Accordion>
                  </Table.Td>
                  <Table.Td align="center">
                    <RowActionDropdown
                      data={[
                        {
                          label: 'Edit',
                          icon: <PencilSquareIcon height={16} />,
                          action: () => handleEdit(method),
                        },
                        {
                          label: 'Delete',
                          icon: <TrashIcon height={16} />,
                          action: () => handleDelete(method),
                        },
                      ]}
                    >
                      <BsThreeDots />
                    </RowActionDropdown>
                  </Table.Td>
                </Table.Tr>
              ))}
            </Table.Tbody>
          </Table>
        ) : (
          <div className="text-center">No data found</div>
        )}
      </Card>

      {/* Modal for Create/Add More */}
      <Modal
        title={`Qualification Rating ${
          modalState.type === 'create' ? 'Create' : 'Add'
        }`}
        opened={modalState.type === 'create' || modalState.type === 'add-more'}
        onClose={handleCloseModal}
      >
        <QualificationRatingForm
          action={modalState.type}
          onClose={handleCloseModal}
          data={modalState.item}
        />
      </Modal>

      {/* Modal for Update Method Name */}
      <Modal
        title="Qualification Method Name Update"
        opened={modalState.type === 'update'}
        onClose={handleCloseModal}
      >
        <QualificationRatingMethodForm
          data={modalState.item}
          onClose={handleCloseModal}
        />
      </Modal>
    </>
  );
}
