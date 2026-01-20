# PageHeader Component - Global Navigation Solution

## 🎯 Overview

The **PageHeader** component is a **PERFECT, REUSABLE, GLOBAL NAVIGATION** solution designed for the Betopia HRM Frontend. It provides a consistent, professional, and fully responsive sticky header across all pages.

## ✨ Features

- ✅ **Sticky Navigation** - Always visible at the top while scrolling
- ✅ **100% Responsive** - Mobile-first design with perfect breakpoints
- ✅ **Glassmorphism Effect** - Modern frosted glass appearance
- ✅ **Collapsible Filters** - Advanced filter section with smooth animations
- ✅ **Breadcrumbs Integration** - Built-in navigation context
- ✅ **Custom Icon Support** - Any React icon with gradient backgrounds
- ✅ **Touch Optimized** - 44px minimum touch targets for accessibility
- ✅ **Highly Customizable** - 15+ props for full control
- ✅ **TypeScript Support** - Full type safety
- ✅ **Performance Optimized** - GPU-accelerated animations

---

## 📦 Installation

The component is already installed at:
```
app/components/common/PageHeader.tsx
```

---

## 🚀 Quick Start

### Basic Usage (No Filters)

```tsx
import PageHeader from '@/components/common/PageHeader';
import { TbReport } from 'react-icons/tb';

export default function MyPage() {
  return (
    <div className="flex flex-col min-h-screen bg-gray-50">
      <PageHeader
        title="My Page Title"
        description="Brief description of the page"
        icon={<TbReport />}
      />

      {/* Your page content */}
      <div className="flex-1 px-4 sm:px-6 lg:px-8 py-6">
        {/* Content here */}
      </div>
    </div>
  );
}
```

### Advanced Usage (With Filters)

```tsx
import PageHeader from '@/components/common/PageHeader';
import { Select, DateInput } from '@mantine/core';
import { TbReport } from 'react-icons/tb';
import { IoCalendarClearOutline } from 'react-icons/io5';

export default function MyPage() {
  const [selectedRows, setSelectedRows] = useState<number[]>([]);

  const handleResetFilters = () => {
    // Reset filter logic
  };

  const filterContent = (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      <Select
        label="Department"
        placeholder="Select department"
        data={['IT', 'Finance', 'HR']}
        size="md"
        radius="md"
        clearable
      />
      <DateInput
        leftSection={<IoCalendarClearOutline className="w-4 h-4" />}
        label="Start Date"
        placeholder="Select date"
        size="md"
        radius="md"
        clearable
      />
    </div>
  );

  return (
    <div className="flex flex-col min-h-screen bg-gray-50">
      <PageHeader
        title="Attendance Report"
        description="Generate detailed attendance reports"
        icon={<TbReport />}
        filterContent={filterContent}
        onResetFilters={handleResetFilters}
        selectedCount={selectedRows.length}
      />

      {/* Your page content */}
      <div className="flex-1 px-4 sm:px-6 lg:px-8 py-6">
        {/* Content here */}
      </div>
    </div>
  );
}
```

---

## 📋 Props API

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `title` | `string` | **Required** | Page title (main heading) |
| `description` | `string` | `undefined` | Subtitle/description text below title |
| `icon` | `ReactNode` | `undefined` | Icon element (e.g., `<TbReport />`) |
| `iconGradient` | `object` | Orange gradient | Custom gradient colors for icon background |
| `actions` | `ReactNode` | `undefined` | Additional custom action buttons |
| `filterContent` | `ReactNode` | `undefined` | Filter form content (enables filter section) |
| `showBreadcrumbs` | `boolean` | `true` | Show/hide breadcrumbs |
| `customBreadcrumbs` | `ReactNode` | `undefined` | Custom breadcrumb component |
| `onResetFilters` | `() => void` | `undefined` | Callback when reset button clicked |
| `hideResetButton` | `boolean` | `false` | Hide the reset filters button |
| `hideFilterToggle` | `boolean` | `false` | Hide the filter toggle button |
| `defaultFilterOpen` | `boolean` | `false` | Default state of filter section |
| `customButtons` | `ReactNode` | `undefined` | Additional custom buttons before reset/filter |
| `selectedCount` | `number` | `undefined` | Show selected items count in filters |
| `backgroundColor` | `string` | `rgba(255, 255, 255, 0.95)` | Header background color |

---

## 🎨 Customization Examples

### 1. Custom Icon Gradient

```tsx
<PageHeader
  title="Employee Management"
  icon={<FiUsers />}
  iconGradient={{
    from: '#3B82F6',  // blue-500
    via: '#2563EB',   // blue-600
    to: '#1D4ED8',    // blue-700
  }}
/>
```

### 2. Custom Action Buttons

```tsx
<PageHeader
  title="Reports"
  icon={<TbReport />}
  actions={
    <>
      <Button variant="outline" leftSection={<FiDownload />}>
        Export
      </Button>
      <Button variant="filled" leftSection={<FiPlus />}>
        Add New
      </Button>
    </>
  }
/>
```

### 3. Without Breadcrumbs

```tsx
<PageHeader
  title="Dashboard"
  description="Overview of key metrics"
  icon={<TbDashboard />}
  showBreadcrumbs={false}
/>
```

### 4. Filters Open by Default

```tsx
<PageHeader
  title="Search Results"
  icon={<FiSearch />}
  filterContent={myFilters}
  defaultFilterOpen={true}
/>
```

### 5. Custom Background Color

```tsx
<PageHeader
  title="Dark Theme Page"
  icon={<FiMoon />}
  backgroundColor="rgba(17, 24, 39, 0.95)" // gray-900 with opacity
/>
```

---

## 🎯 Icon Gradient Presets

```tsx
// Orange (Default)
iconGradient={{ from: '#F97316', via: '#EA580C', to: '#C2410C' }}

// Blue
iconGradient={{ from: '#3B82F6', via: '#2563EB', to: '#1D4ED8' }}

// Green
iconGradient={{ from: '#10B981', via: '#059669', to: '#047857' }}

// Purple
iconGradient={{ from: '#8B5CF6', via: '#7C3AED', to: '#6D28D9' }}

// Red
iconGradient={{ from: '#EF4444', via: '#DC2626', to: '#B91C1C' }}

// Pink
iconGradient={{ from: '#EC4899', via: '#DB2777', to: '#BE185D' }}

// Indigo
iconGradient={{ from: '#6366F1', via: '#4F46E5', to: '#4338CA' }}
```

---

## 📱 Responsive Behavior

| Breakpoint | Width | Layout Changes |
|------------|-------|----------------|
| **Mobile** | < 640px | Single column, stacked buttons, compact spacing |
| **Small Tablet** | 640px - 768px | 2 columns, side-by-side layout starts |
| **Tablet** | 768px - 1024px | 3 columns, optimized spacing |
| **Desktop** | 1024px+ | Full layout, all columns visible |
| **Ultra Wide** | 1920px+ | Centered with max-width container |

---

## 🔧 Filter Content Styling Guidelines

For consistent filter styling, use these Mantine classNames:

```tsx
const filterInputClasses = {
  input: 'text-sm border-gray-300 focus:border-orange-500 transition-colors',
  label: 'text-sm font-semibold text-gray-700 mb-2'
};

<Select
  label="Department"
  placeholder="Select"
  size="md"
  radius="md"
  clearable
  classNames={filterInputClasses}
/>
```

---

## 💡 Best Practices

### ✅ DO:
- Always wrap your page in `min-h-screen` container
- Use consistent icon sizes (prefer react-icons)
- Provide descriptive titles and descriptions
- Use `clearable` on filter inputs
- Keep filter grids responsive: `grid-cols-1 sm:grid-cols-2 lg:grid-cols-4`
- Use the `selectedCount` prop for bulk actions

### ❌ DON'T:
- Don't use very long titles (they will truncate)
- Don't put heavy components in filterContent (use lazy loading if needed)
- Don't forget to handle `onResetFilters` callback
- Don't use fixed widths in filter content
- Don't nest PageHeader components

---

## 🌟 Real-World Examples

### Example 1: Leave Management Page

```tsx
import PageHeader from '@/components/common/PageHeader';
import { TbCalendarEvent } from 'react-icons/tb';

export default function LeaveManagement() {
  return (
    <div className="flex flex-col min-h-screen bg-gray-50">
      <PageHeader
        title="Leave Management"
        description="Manage employee leave requests and balances"
        icon={<TbCalendarEvent />}
        iconGradient={{
          from: '#10B981',
          via: '#059669',
          to: '#047857',
        }}
        actions={
          <Button variant="filled" color="green">
            Approve Selected
          </Button>
        }
      />
      {/* Content */}
    </div>
  );
}
```

### Example 2: Employee Directory

```tsx
import PageHeader from '@/components/common/PageHeader';
import { FiUsers } from 'react-icons/fi';

export default function EmployeeDirectory() {
  const filterContent = (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">
      <Select label="Department" placeholder="All" data={depts} />
      <Select label="Location" placeholder="All" data={locations} />
      <Select label="Status" placeholder="All" data={statuses} />
      <TextInput label="Search" placeholder="Name or ID" />
      <Select label="Role" placeholder="All" data={roles} />
    </div>
  );

  return (
    <div className="flex flex-col min-h-screen bg-gray-50">
      <PageHeader
        title="Employee Directory"
        description="Browse and search all employees"
        icon={<FiUsers />}
        iconGradient={{
          from: '#3B82F6',
          via: '#2563EB',
          to: '#1D4ED8',
        }}
        filterContent={filterContent}
        onResetFilters={() => {
          // Reset all filter states
        }}
      />
      {/* Content */}
    </div>
  );
}
```

---

## 🚀 Migration Guide

### Converting Existing Pages

**Before:**
```tsx
export default function MyPage() {
  return (
    <div>
      <div className="px-5 py-6">
        <h1>My Title</h1>
        <p>Description</p>
        {/* Filters */}
      </div>
      {/* Content */}
    </div>
  );
}
```

**After:**
```tsx
import PageHeader from '@/components/common/PageHeader';

export default function MyPage() {
  return (
    <div className="flex flex-col min-h-screen bg-gray-50">
      <PageHeader
        title="My Title"
        description="Description"
        icon={<YourIcon />}
        filterContent={filterContent}
      />
      <div className="flex-1 px-4 sm:px-6 lg:px-8 py-6">
        {/* Content */}
      </div>
    </div>
  );
}
```

---

## 🎨 Advanced Styling

### Custom Filter Header

You can customize the filter section appearance by wrapping content:

```tsx
const filterContent = (
  <div>
    {/* Custom header */}
    <div className="bg-blue-50 p-4 rounded-lg mb-4">
      <p className="text-sm text-blue-800">
        💡 Tip: Use filters to narrow down results
      </p>
    </div>

    {/* Filter inputs */}
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      {/* Your filters */}
    </div>
  </div>
);
```

---

## 🐛 Troubleshooting

### Issue: Header not sticky
**Solution:** Ensure parent container has `flex flex-col min-h-screen`

### Issue: Icon not showing
**Solution:** Check that you're passing a React element: `icon={<TbReport />}` not `icon={TbReport}`

### Issue: Filters not collapsing
**Solution:** Ensure Mantine's Collapse component is properly installed

### Issue: Breadcrumbs not showing
**Solution:** Set `showBreadcrumbs={true}` and ensure Breadcrumbs component exists

---

## 📊 Performance Tips

1. **Memoize filter content** if it contains complex logic:
```tsx
const filterContent = useMemo(() => (
  <div>
    {/* Filters */}
  </div>
), [dependencies]);
```

2. **Lazy load heavy filters**:
```tsx
const HeavyFilters = lazy(() => import('./HeavyFilters'));
```

3. **Use callback memoization**:
```tsx
const handleResetFilters = useCallback(() => {
  // Reset logic
}, [dependencies]);
```

---

## 🎯 Accessibility

The PageHeader component follows WCAG 2.1 AA guidelines:

- ✅ Minimum 44px touch targets
- ✅ Keyboard navigation support
- ✅ Proper heading hierarchy (h1 for title)
- ✅ Semantic HTML structure
- ✅ Focus indicators
- ✅ Screen reader friendly
- ✅ Color contrast compliant

---

## 📝 License

This component is part of the Betopia HRM Frontend project.

---

## 🤝 Contributing

To improve the PageHeader component:

1. Add new features as props with sensible defaults
2. Maintain backward compatibility
3. Update this README with examples
4. Test on all breakpoints
5. Ensure TypeScript types are correct

---

## 📞 Support

For questions or issues with the PageHeader component:

1. Check this README first
2. Review the example in `attendance-report/page.tsx`
3. Contact the development team

---

**Created with ❤️ for Betopia HRM**
