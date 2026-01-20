'use client';

import React, { ReactNode } from 'react';

type SlideOverProps = {
  open: boolean;
  title?: string;
  onClose: () => void;
  children: ReactNode;
  widthClass?: string; // e.g. "max-w-xl"
};

export default function SlideOver({
  open,
  title,
  onClose,
  children,
  widthClass = "max-w-2xl",
}: SlideOverProps) {
  return (
    <div
      className={`fixed inset-0 z-50 ${open ? '' : 'pointer-events-none'}`}
      aria-hidden={!open}
    >
      {/* backdrop */}
      <div
        className={`absolute inset-0 bg-black/40 transition-opacity ${open ? 'opacity-100' : 'opacity-0'}`}
        onClick={onClose}
      />

      {/* panel */}
      <div
        className={`absolute inset-y-0 right-0 w-[95vw] sm:w-full sm:${widthClass} bg-white shadow-xl transform transition-transform
        ${open ? 'translate-x-0' : 'translate-x-full'}`}
      >
        <div className="h-full flex flex-col">
          <div className="px-5 py-4 border-b flex items-center justify-between">
            <h3 className="text-base font-semibold">{title}</h3>
            <button
              type="button"
              onClick={onClose}
              className="rounded px-2 py-1 text-sm hover:bg-gray-100"
            >
              ✕
            </button>
          </div>
          <div className="p-5 overflow-auto">{children}</div>
        </div>
      </div>
    </div>
  );
}
