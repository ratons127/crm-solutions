'use client';

import { useEffect, useRef, useState, useCallback, useMemo } from 'react';

export type Section = {
  id: string;
  title: string;
  content: React.ReactNode;
  actions?: React.ReactNode;
};

type FooterRender = (
  activeSectionId: string,
  activeSection: Section | undefined
) => React.ReactNode;

type Props = {
  title: string;
  sections: Section[];
  headerExtra?: React.ReactNode;
  footer?: React.ReactNode | FooterRender;
  footerPlacement?: 'below' | 'header';
  className?: string;
  initialActiveId?: string;
};

export default function ConfigPageLayout({
  title,
  sections,
  headerExtra,
  footer,
  footerPlacement = 'below',
  className = '',
  initialActiveId,
}: Props) {
  const firstId = sections[0]?.id || '';
  const [active, setActive] = useState<string>(initialActiveId || firstId);
  const paneRef = useRef<HTMLDivElement | null>(null);

  const setActiveAndSync = useCallback((id: string, scrollToTop = true) => {
    setActive(id);
    if (typeof window !== 'undefined') {
      window.history.replaceState?.(null, '', `#${id}`);
    }
    if (scrollToTop) paneRef.current?.scrollTo({ top: 0, behavior: 'auto' });
  }, []);

  useEffect(() => {
    const applyHash = () => {
      const hash =
        typeof window !== 'undefined' ? window.location.hash.slice(1) : '';
      if (hash && sections.some(s => s.id === hash)) {
        setActive(hash);
      } else if (!active) {
        setActive(initialActiveId || firstId);
      }
    };
    applyHash();
    const onHashChange = () => applyHash();
    window.addEventListener('hashchange', onHashChange);
    return () => window.removeEventListener('hashchange', onHashChange);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sections, initialActiveId]);

  const onKeyDownTab = (e: React.KeyboardEvent<HTMLDivElement>) => {
    const idx = sections.findIndex(s => s.id === active);
    if (idx < 0) return;
    if (e.key === 'ArrowDown') {
      e.preventDefault();
      const next = sections[(idx + 1) % sections.length].id;
      setActiveAndSync(next);
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      const prev = sections[(idx - 1 + sections.length) % sections.length].id;
      setActiveAndSync(prev);
    }
  };

  const activeSection = useMemo(
    () => sections.find(s => s.id === active) ?? sections[0],
    [sections, active]
  );

  const headerActions = activeSection
    ? activeSection.actions ??
      (footerPlacement === 'header'
        ? typeof footer === 'function'
          ? footer(active, activeSection)
          : footer
        : undefined)
    : undefined;

  const footerNode =
    footerPlacement === 'below'
      ? typeof footer === 'function'
        ? footer(active, activeSection)
        : footer
      : undefined;

  return (
    <div
      className={`mx-auto w-full ${className} rounded-lg border border-gray-200 bg-white p-4 sm:p-6 shadow-sm`}
    >
      {/* Top header */}
      <div className="mb-4 flex flex-wrap items-center justify-between gap-3">
        {headerExtra}
        {footerNode ? <div className="mt-0">{footerNode}</div> : null}
      </div>

      {/* Responsive layout:
         - base..md: stack (menu on top, content below)
         - lg+:   row (menu left, content right)
      */}
      <div className="flex flex-col lg:flex-row gap-4 lg:gap-6">
        {/* LEFT MENU */}
        <aside className="w-full lg:w-64 lg:shrink-0">
        <div
          className="rounded-lg border border-gray-200 bg-white p-2"
          role="tablist"
          aria-label={title}
          onKeyDown={onKeyDownTab}
        >
          <p className="px-3 py-2 text-xs font-semibold uppercase text-gray-500">
            {title}
          </p>

          {/* 
            ✅ Mobile: 1 item per row, horizontal scroll
            ✅ Tablet: 2 items per row
            ✅ Desktop: vertical scrollable list 
          */}
          <nav
            className="
              grid grid-cols-1 sm:grid-cols-2 lg:flex lg:flex-col
              gap-2
              max-h-full lg:max-h-90
              overflow-visible lg:overflow-y-auto
              scrollbar-thin scrollbar-thumb-gray-300 scrollbar-track-transparent
              pb-1
            "
          >
            {sections.map((s) => {
              const isActive = active === s.id;
              return (
                <MenuItem
                  key={s.id}
                  id={`${s.id}-tab`}
                  controls={`${s.id}-panel`}
                  label={s.title}
                  active={isActive}
                  onClick={() => setActiveAndSync(s.id)}
                />
              );
            })}
          </nav>
        </div>
      </aside>

        {/* RIGHT PANE */}
        <section
          ref={paneRef}
          // base..md: let page scroll naturally; lg+: fixed-height scrollable pane
          className="flex-1 overflow-visible lg:overflow-auto lg:h-[calc(100vh-12rem)]"
        >
          {activeSection && (
            <div
              key={activeSection.id}
              id={`${activeSection.id}-panel`}
              role="tabpanel"
              aria-labelledby={`${activeSection.id}-tab`}
              className="animate-fadeIn"
            >
              <Card title={activeSection.title} actions={headerActions}>
                {activeSection.content}
              </Card>
            </div>
          )}
        </section>
      </div>
    </div>
  );
}

/* ---------- UI bits ---------- */
function MenuItem({
  id,
  controls,
  label,
  active,
  onClick,
}: {
  id: string;
  controls: string;
  label: string;
  active: boolean;
  onClick: () => void;
}) {
  return (
    <button
      type="button"
      id={id}
      aria-controls={controls}
      role="tab"
      aria-selected={active}
      onClick={onClick}
      className={`flex shrink-0 items-center gap-2 rounded-md px-3 py-2 text-left text-sm transition
        ${
          active
            ? 'bg-[#fadfcb] text-gray-700 ring-1 ring-inset ring-[#fadfcb]'
            : 'text-gray-500 hover:bg-[#f7ede6]'
        }`}
    >
      <span
        className={`h-2 w-2 rounded-full ${
          active ? 'bg-[#F69348]' : 'bg-gray-300'
        }`}
      />
      {label}
    </button>
  );
}

function Card({
  children,
}: {
  title?: string;
  actions?: React.ReactNode;
  children: React.ReactNode;
}) {
  return (
    <div className="rounded-lg border border-gray-200 bg-white p-3 sm:p-4 shadow-sm">
      {/* Uncomment if you want a header inside the card */}
      {/* {title || actions ? (
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-base font-semibold text-gray-800">{title}</h2>
          <div>{actions}</div>
        </div>
      ) : null} */}
      {children}
    </div>
  );
}