'use client';
import React, { createContext, useContext, useState } from 'react';

interface CalendarContextType {
  selectedDates: string[];
  setSelectedDates: (dates: string[]) => void;
}

const CalendarContext = createContext<CalendarContextType | undefined>(undefined);

export const CalendarProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [selectedDates, setSelectedDates] = useState<string[]>([]);
  return (
    <CalendarContext.Provider value={{ selectedDates, setSelectedDates }}>
      {children}
    </CalendarContext.Provider>
  );
};

export const useCalendarContext = () => {
  const context = useContext(CalendarContext);
  if (!context) throw new Error('useCalendarContext must be used within CalendarProvider');
  return context;
};
