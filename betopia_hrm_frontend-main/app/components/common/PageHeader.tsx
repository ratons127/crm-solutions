'use client';

import { Button, Collapse } from '@mantine/core';
import { ReactNode, useState } from 'react';
import { FiRefreshCcw } from 'react-icons/fi';
import { GoChevronDown, GoChevronUp } from 'react-icons/go';
import Breadcrumbs from './Breadcrumbs';

/* ============================
   Type Definition
============================ */
interface PageHeaderProps {
  /** Page title */
  title: string;
  /** Page description/subtitle */
  description?: string;
  /** Icon element to display */
  icon?: ReactNode;
  /** Icon background gradient colors */
  iconGradient?: {
    from: string;
    to: string;
    via?: string;
  };
  /** Additional action buttons to display on the right */
  actions?: ReactNode;
  /** Filter content to show in collapsible section */
  filterContent?: ReactNode;
  /** Show breadcrumbs */
  showBreadcrumbs?: boolean;
  /** Custom breadcrumb component */
  customBreadcrumbs?: ReactNode;
  /** Callback when reset filters is clicked */
  onResetFilters?: () => void;
  /** Hide the reset filters button */
  hideResetButton?: boolean;
  /** Hide the filter toggle button */
  hideFilterToggle?: boolean;
  /** Default filter open state */
  defaultFilterOpen?: boolean;
  /** Additional custom buttons */
  customButtons?: ReactNode;
  /** Show selected count */
  selectedCount?: number;
  /** Background color */
  backgroundColor?: string;
}

/* ============================
   Perfect Global Page Header Component
============================ */
export default function PageHeader({
  title,
  description,
  icon,
  iconGradient = {
    from: '#F97316', // orange-500
    to: '#C2410C',   // orange-700
    via: '#EA580C',  // orange-600
  },
  actions,
  filterContent,
  showBreadcrumbs = true,
  customBreadcrumbs,
  onResetFilters,
  hideResetButton = false,
  hideFilterToggle = false,
  defaultFilterOpen = false,
  customButtons,
  selectedCount,
  backgroundColor = 'rgba(255, 255, 255, 0.95)',
}: PageHeaderProps) {
  const [filtersOpen, setFiltersOpen] = useState(defaultFilterOpen);

  const toggleFilters = () => {
    setFiltersOpen(prev => !prev);
  };

  const handleResetFilters = () => {
    if (onResetFilters) {
      onResetFilters();
    }
    // Optionally close filters after reset
    // setFiltersOpen(false);
  };

  return (
    <div
      className="sticky top-0 z-40 border-b border-gray-200 shadow-sm backdrop-blur-md"
      style={{ backgroundColor }}
    >
      <div className="max-w-[1920px] mx-auto">
        {/* Breadcrumbs Section */}
        {showBreadcrumbs && (
          <div className="px-4 sm:px-6 lg:px-8 pt-3 sm:pt-4 pb-2">
            {customBreadcrumbs || <Breadcrumbs />}
          </div>
        )}

        {/* Main Header Content */}
        <div className="px-4 sm:px-6 lg:px-8 pb-3 sm:pb-4">
          {/* Title and Actions Row */}
          <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 sm:gap-5 lg:gap-6">
            {/* Title Section with Icon */}
            <div className="flex items-start sm:items-center gap-3 sm:gap-4 min-w-0 flex-1">
              {/* Icon */}
              {icon && (
                <div className="flex-shrink-0">
                  <div
                    className="w-11 h-11 sm:w-12 sm:h-12 lg:w-14 lg:h-14 flex items-center justify-center rounded-xl sm:rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 transform hover:scale-105"
                    style={{
                      background: iconGradient.via
                        ? `linear-gradient(135deg, ${iconGradient.from} 0%, ${iconGradient.via} 50%, ${iconGradient.to} 100%)`
                        : `linear-gradient(135deg, ${iconGradient.from} 0%, ${iconGradient.to} 100%)`,
                    }}
                  >
                    <div className="text-white w-5 h-5 sm:w-6 sm:h-6 lg:w-7 lg:h-7">
                      {icon}
                    </div>
                  </div>
                </div>
              )}

              {/* Title and Description */}
              <div className="flex-1 min-w-0">
                <h1 className="text-xl sm:text-2xl lg:text-3xl xl:text-4xl font-bold text-gray-900 tracking-tight truncate">
                  {title}
                </h1>
                {description && (
                  <p className="text-xs sm:text-sm lg:text-base text-gray-600 mt-0.5 sm:mt-1 line-clamp-2">
                    {description}
                  </p>
                )}
              </div>
            </div>

            {/* Action Buttons Section */}
            <div className="flex flex-wrap sm:flex-nowrap items-center gap-2 sm:gap-3 lg:gap-4">
              {/* Custom Buttons */}
              {customButtons}

              {/* Reset Filters Button */}
              {!hideResetButton && filterContent && (
                <Button
                  leftSection={<FiRefreshCcw className="w-4 h-4" />}
                  variant="light"
                  color="gray"
                  radius="lg"
                  size="md"
                  onClick={handleResetFilters}
                  className="flex-1 sm:flex-none transition-all duration-200 hover:shadow-md active:scale-95"
                  styles={{
                    root: {
                      backgroundColor: '#F8FAFC',
                      border: '1px solid #E2E8F0',
                      color: '#475569',
                      fontWeight: 600,
                      fontSize: '14px',
                      minHeight: '44px',
                      paddingLeft: '16px',
                      paddingRight: '16px',
                    },
                    section: { marginRight: 8 },
                  }}
                >
                  <span className="hidden sm:inline">Reset Filters</span>
                  <span className="sm:hidden">Reset</span>
                </Button>
              )}

              {/* Filter Toggle Button */}
              {!hideFilterToggle && filterContent && (
                <Button
                  onClick={toggleFilters}
                  variant="gradient"
                  gradient={{ from: 'orange.6', to: 'orange.7', deg: 135 }}
                  radius="lg"
                  size="md"
                  className="flex-1 sm:flex-none transition-all duration-200 hover:shadow-lg active:scale-95"
                  leftSection={
                    filtersOpen ? (
                      <GoChevronUp className="w-4 h-4 transition-transform duration-300" />
                    ) : (
                      <GoChevronDown className="w-4 h-4 transition-transform duration-300" />
                    )
                  }
                  styles={{
                    root: {
                      fontWeight: 600,
                      fontSize: '14px',
                      minHeight: '44px',
                      paddingLeft: '18px',
                      paddingRight: '18px',
                    },
                    section: { marginRight: 8 },
                  }}
                >
                  <span className="hidden sm:inline">
                    {filtersOpen ? 'Hide Filters' : 'Show Filters'}
                  </span>
                  <span className="sm:hidden">Filters</span>
                </Button>
              )}

              {/* Additional Actions */}
              {actions}
            </div>
          </div>

          {/* Collapsible Filter Section */}
          {filterContent && (
            <Collapse in={filtersOpen} transitionDuration={300}>
              <div className="mt-4 sm:mt-5 lg:mt-6 bg-gradient-to-br from-white to-gray-50/50 px-4 sm:px-6 lg:px-8 py-5 sm:py-6 rounded-xl sm:rounded-2xl shadow-md border border-gray-200/60 backdrop-blur-sm">
                {/* Filter Header with Selected Count */}
                <div className="flex items-center justify-between mb-4 sm:mb-5 lg:mb-6">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 sm:w-9 sm:h-9 bg-gradient-to-br from-blue-500/10 to-blue-600/10 rounded-lg flex items-center justify-center">
                      <svg
                        className="w-4 h-4 sm:w-5 sm:h-5 text-blue-600"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z"
                        />
                      </svg>
                    </div>
                    <h3 className="text-base sm:text-lg font-semibold text-gray-900">
                      Advanced Filters
                    </h3>
                  </div>
                  {selectedCount !== undefined && selectedCount > 0 && (
                    <div className="text-xs sm:text-sm text-gray-600 font-medium bg-blue-50 px-3 py-1 rounded-full border border-blue-200">
                      {selectedCount} selected
                    </div>
                  )}
                </div>

                {/* Filter Content */}
                {filterContent}
              </div>
            </Collapse>
          )}
        </div>
      </div>
    </div>
  );
}
