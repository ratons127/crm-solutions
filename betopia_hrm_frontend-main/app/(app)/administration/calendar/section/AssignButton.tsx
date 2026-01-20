import React, { useMemo, useState } from 'react';
import { Chip, Group, Button, TextInput } from '@mantine/core';

const ALL = '__ALL__';
const GROUPS = ['Software', 'bd calling', 'Fire AI', 'Sales', 'Marketing'] as const;

export default function AssignButton() {
  // store only the *real* groups (exclude ALL token in state)
  const [selected, setSelected] = useState<string[]>([]);
  const [holidayName, setHolidayName] = useState('');

  const allSelected = selected.length === GROUPS.length;
  const someSelected = !allSelected && selected.length > 0;


  const groupValueForUI = useMemo(
    () => (allSelected ? [ALL, ...selected] : selected),
    [allSelected, selected]
  );

  const handleChange = (next: string[]) => {
   
    if (next.includes(ALL)) {
      setSelected(allSelected ? [] : [...GROUPS]);
      return;
    }

    const cleaned = next.filter((x) => x !== ALL);
    setSelected(cleaned);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
   
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <TextInput
        label="Holiday name"
        placeholder="Enter holiday name"
        value={holidayName}
        onChange={(e) => setHolidayName(e.currentTarget.value)}
        required
        variant="filled"
        size="md"
        radius="md"
      />

      <div>
        <h4 className="text-lg text-[#0A0A0A]">Workplace Groups</h4>

        <Chip.Group multiple value={groupValueForUI} onChange={handleChange}>
          <Group justify="start" mt="md" className="flex flex-wrap gap-2">
            {/* ALL chip */}
            <Chip
              value={ALL}
              radius="md"
              // make ALL look filled when all selected, outline when partial, subtle when none
              variant={allSelected ? 'filled' : someSelected ? 'outline' : 'light'}
            >
              All Groups
            </Chip>

            {/* Individual chips */}
            {GROUPS.map((g) => (
              <Chip key={g} value={g} radius="md" variant="filled">
                {g}
              </Chip>
            ))}
          </Group>
        </Chip.Group>
      </div>
      <Button
        type="submit"
        radius="md"
        variant="filled"
        fullWidth
        styles={{ root: { backgroundColor: '#FF6900' }, label: { color: '#fff' } }}
        className="!py-3"
      >
        Add Holiday
      </Button>
    </form>
  );
}
