'use client';

import { AnimatePresence, motion } from 'framer-motion';
import { ReactNode, useEffect, useMemo } from 'react';

type ModalSize = 'sm' | 'md' | 'lg' | 'xl' | '2xl' | '3xl' | '4xl';

type ModalProps = {
  isOpen: boolean;
  onClose: () => void;
  children: ReactNode;
  title?: string;
  size?: ModalSize; // NEW: choose modal width
  className?: string; // optional extra classes for the panel
  hideCloseButton?: boolean;
};

export default function Modal({
  isOpen,
  onClose,
  children,
  title,
  size = 'lg', // default matches your previous max-w-lg
  className = '',
  hideCloseButton = false,
}: ModalProps) {
  // map sizes → Tailwind max-widths (responsive)
  const maxWidth = useMemo(() => {
    const map: Record<ModalSize, string> = {
      sm: 'max-w-[95vw] sm:max-w-sm',
      md: 'max-w-[95vw] sm:max-w-md',
      lg: 'max-w-[95vw] sm:max-w-lg',
      xl: 'max-w-[95vw] sm:max-w-xl',
      '2xl': 'max-w-[95vw] sm:max-w-2xl',
      '3xl': 'max-w-[95vw] sm:max-w-3xl',
      '4xl': 'max-w-[95vw] sm:max-w-4xl',
    };
    return map[size] || 'max-w-[95vw] sm:max-w-lg';
  }, [size]);

  // Close on ESC
  useEffect(() => {
    if (!isOpen) return;
    const handler = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    window.addEventListener('keydown', handler);
    return () => window.removeEventListener('keydown', handler);
  }, [isOpen, onClose]);

  return (
    <AnimatePresence>
      {isOpen && (
        <>
          {/* Backdrop */}
          <motion.div
            className="fixed inset-0 bg-black/50 z-40"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            onClick={onClose}
          />

          {/* Modal content */}
          <motion.div
            className="fixed inset-0 z-50 flex items-center sm:items-center justify-center p-2 sm:p-4"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 0.95 }}
            aria-modal
            role="dialog"
            aria-label={title || 'Modal'}
          >
            <div
              // stop click-through so clicking inside doesn't close
              onClick={e => e.stopPropagation()}
              className={[
                'w-full',
                maxWidth,
                'bg-white rounded-lg sm:rounded-2xl shadow-lg p-4 sm:p-6 relative',
                'max-h-[95vh] sm:max-h-[90vh] overflow-auto', // scroll if content is tall
                className,
              ].join(' ')}
            >
              {!hideCloseButton && (
                <button
                  onClick={onClose}
                  className="absolute top-2 right-2 sm:top-3 sm:right-3 text-gray-500 hover:text-gray-800 min-h-[44px] min-w-[44px] flex items-center justify-center text-xl sm:text-base"
                  aria-label="Close modal"
                >
                  ✕
                </button>
              )}

              {title && (
                <h2 className="text-base sm:text-lg font-semibold mb-3 sm:mb-4 text-gray-800 pr-10">
                  {title}
                </h2>
              )}

              <div>{children}</div>
            </div>
          </motion.div>
        </>
      )}
    </AnimatePresence>
  );
}
