# 🏢 Betopia HRM Frontend

<div align="center">

![Next.js](https://img.shields.io/badge/Next.js-15.5.3-black?style=for-the-badge&logo=next.js&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-5.9.2-blue?style=for-the-badge&logo=typescript&logoColor=white)
![React](https://img.shields.io/badge/React-19.1.0-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-4.1.13-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)

**Modern Human Resource Management System Frontend**

_Built with Next.js, TypeScript, and Tailwind CSS for the modern workplace_

[🚀 Live Demo](https://your-demo-url.com) • [📖 Documentation](./VSCODE_SETUP.md) •
[🐛 Report Bug](https://github.com/your-org/betopia-hrm-frontend/issues) •
[✨ Request Feature](https://github.com/your-org/betopia-hrm-frontend/issues)

</div>

---

## 📋 Table of Contents

- [🌟 Overview](#-overview)
- [✨ Features](#-features)
- [🛠️ Tech Stack](#️-tech-stack)
- [🚀 Quick Start](#-quick-start)
- [📦 Installation](#-installation)
- [🎯 Development](#-development)
- [🏗️ Project Structure](#️-project-structure)
- [🎨 UI Components](#-ui-components)
- [🔧 Configuration](#-configuration)
- [🧪 Testing](#-testing)
- [🚢 Deployment](#-deployment)
- [📚 Documentation](#-documentation)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

---

## 🌟 Overview

Betopia HRM Frontend is a cutting-edge Human Resource Management System designed to streamline HR
operations for modern businesses. Built with the latest web technologies, it provides an intuitive,
responsive, and feature-rich interface for managing employees, attendance, applications, and
organizational workflows.

### 🎯 **Mission**

To revolutionize HR management through innovative technology, making workforce management efficient,
transparent, and user-friendly.

### 🌍 **Vision**

Empowering organizations with intelligent HR solutions that scale with business growth and adapt to
changing workplace dynamics.

---

## ✨ Features

### 👥 **Employee Management**

- 📊 **Dashboard Overview** - Real-time metrics and key performance indicators
- 👤 **Employee Profiles** - Comprehensive employee information management
- 📝 **Application Processing** - Streamlined leave and request management
- 💰 **Payroll Integration** - Transparent payslip and compensation tracking

### 🕒 **Attendance & Time Tracking**

- 📅 **Interactive Calendar** - Visual attendance tracking with multiple status indicators
- ⏰ **Check-in/Check-out** - Real-time attendance recording
- 📈 **Attendance Analytics** - Detailed reporting and trend analysis
- 🏠 **Remote Work Support** - Flexible work arrangement tracking

### 🛡️ **Security & Compliance**

- 🔐 **Secure Authentication** - JWT-based authentication with refresh tokens
- 👨‍💼 **Role-based Access** - Granular permission management
- 🔒 **Data Protection** - GDPR compliant data handling
- 📊 **Audit Trails** - Comprehensive activity logging

### 🎨 **User Experience**

- 📱 **Responsive Design** - Seamless experience across all devices
- 🌙 **Dark/Light Mode** - Customizable theme preferences
- ♿ **Accessibility** - WCAG 2.1 AA compliant
- 🌐 **Internationalization** - Multi-language support ready

### ⚡ **Performance & Quality**

- 🚀 **Lightning Fast** - Optimized with Next.js App Router
- 🔄 **Real-time Updates** - Live data synchronization
- 📱 **PWA Ready** - Progressive Web App capabilities
- 🛡️ **Type Safety** - Full TypeScript implementation

---

## 🛠️ Tech Stack

<div align="center">

| Category             | Technology                                                                                        | Version  | Purpose                         |
| -------------------- | ------------------------------------------------------------------------------------------------- | -------- | ------------------------------- |
| **Framework**        | ![Next.js](https://img.shields.io/badge/Next.js-black?style=flat-square&logo=next.js)             | 15.5.3   | React framework with App Router |
| **Language**         | ![TypeScript](https://img.shields.io/badge/TypeScript-blue?style=flat-square&logo=typescript)     | 5.9.2    | Type-safe development           |
| **UI Library**       | ![React](https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=react)                  | 19.1.0   | Component-based UI              |
| **Styling**          | ![Tailwind CSS](https://img.shields.io/badge/Tailwind-38B2AC?style=flat-square&logo=tailwind-css) | 4.1.13   | Utility-first CSS               |
| **State Management** | ![Redux Toolkit](https://img.shields.io/badge/Redux_Toolkit-764ABC?style=flat-square&logo=redux)  | 2.9.0    | Predictable state container     |
| **HTTP Client**      | ![Axios](https://img.shields.io/badge/Axios-5A29E4?style=flat-square&logo=axios)                  | 1.11.0   | Promise-based HTTP client       |
| **Forms**            | ![Formik](https://img.shields.io/badge/Formik-172B4D?style=flat-square)                           | 2.4.6    | Form validation and handling    |
| **Data Tables**      | ![TanStack Table](https://img.shields.io/badge/TanStack_Table-FF4154?style=flat-square)           | 8.21.3   | Powerful data tables            |
| **Icons**            | ![Heroicons](https://img.shields.io/badge/Heroicons-8B5CF6?style=flat-square)                     | 2.2.0    | Beautiful SVG icons             |
| **Animation**        | ![Framer Motion](https://img.shields.io/badge/Framer_Motion-0055FF?style=flat-square&logo=framer) | 12.23.16 | Smooth animations               |

</div>

### 🔧 **Development Tools**

- **ESLint** - Code quality and consistency
- **Prettier** - Code formatting
- **Husky** - Git hooks for quality assurance
- **TypeScript** - Static type checking
- **Turbopack** - Ultra-fast bundler

---

## 🚀 Quick Start

### ⚡ **TL;DR - Get Running in 2 Minutes**

```bash
# Clone and setup
git clone https://github.com/Betopia-Limited/betopia-hrm-frontend.git
cd betopia-hrm-frontend
npm install

# Start development
npm run dev

# Open http://localhost:3000 🎉
```

### 🎯 **For First-Time Contributors**

```bash
# 1. Setup development environment
npm run prepare          # Install git hooks
npm run validate        # Verify everything works

# 2. Open in VS Code with optimized settings
code hrm-frontend.code-workspace

# 3. Start coding with confidence!
```

---

## 📦 Installation

### 📋 **Prerequisites**

Ensure you have the following installed:

- **Node.js** >= 18.0.0 ([Download](https://nodejs.org/))
- **npm** >= 9.0.0 (comes with Node.js)
- **Git** ([Download](https://git-scm.com/))

### 🔍 **Verification**

```bash
node --version    # Should be >= 18.0.0
npm --version     # Should be >= 9.0.0
git --version     # Any recent version
```

### 📥 **Clone Repository**

```bash
# HTTPS
git clone https://github.com/Betopia-Limited/betopia-hrm-frontend.git

# SSH (recommended for contributors)
git clone git@github.com:Betopia-Limited/betopia-hrm-frontend.git

# Navigate to project
cd betopia-hrm-frontend
```

### 📦 **Install Dependencies**

```bash
# Install all dependencies
npm install

# Or use clean install for CI/production
npm ci
```

### ⚙️ **Environment Setup**

```bash
# Copy environment template
cp .env.example .env.local

# Edit with your configuration
# Required variables:
# NEXT_PUBLIC_API_URL=https://api-hrm.betopiagroup.com/api/
```

### 🛡️ **Setup Quality Assurance**

```bash
# Initialize git hooks (prevents bad commits)
npm run prepare

# Verify setup works
npm run validate
```

---

## 🎯 Development

### 🚀 **Development Server**

```bash
# Start development server with Turbopack
npm run dev

# Server will start at:
# Local:    http://localhost:3000
# Network:  http://[your-ip]:3000
```

### 🔧 **Available Scripts**

| Script       | Description               | Usage                |
| ------------ | ------------------------- | -------------------- |
| `dev`        | Start development server  | `npm run dev`        |
| `build`      | Build for production      | `npm run build`      |
| `start`      | Start production server   | `npm start`          |
| `lint`       | Check code quality        | `npm run lint`       |
| `lint:fix`   | Fix auto-fixable issues   | `npm run lint:fix`   |
| `type-check` | Check TypeScript types    | `npm run type-check` |
| `format`     | Format code with Prettier | `npm run format`     |
| `validate`   | Full quality check        | `npm run validate`   |
| `pre-commit` | Pre-commit validation     | `npm run pre-commit` |

### 🎨 **Development Workflow**

1. **Start Development:**

   ```bash
   npm run dev
   ```

2. **Create Feature Branch:**

   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Develop with Quality Checks:**

   ```bash
   npm run type-check    # Check types frequently
   npm run lint          # Check code quality
   ```

4. **Commit (Auto-validated):**

   ```bash
   git add .
   git commit -m "feat: add new feature"
   # ✅ Pre-commit hooks ensure quality
   ```

5. **Push and Create PR:**

   ```bash
   git push origin feature/your-feature-name
   ```

### 🛡️ **Quality Assurance**

Our project enforces strict quality standards:

- ✅ **TypeScript** - No compilation errors allowed
- ✅ **ESLint** - Code quality checks pass
- ✅ **Prettier** - Consistent code formatting
- ✅ **Build Success** - Production build must work
- ✅ **Git Hooks** - Automatic validation on commit

**Quality Gates:**

- 🚫 **Cannot commit** with TypeScript errors
- 🚫 **Cannot commit** with ESLint errors
- 🚫 **Cannot push** if build fails
- ✅ **CI/CD validation** on every pull request

---

## 🏗️ Project Structure

```
📁 betopia-hrm-frontend/
├── 📁 app/                          # Next.js App Router
│   ├── 📁 (app)/                   # Authenticated app routes
│   │   ├── 📁 administration/      # Admin pages
│   │   ├── 📁 employees/           # Employee management
│   │   ├── 📁 notifications/       # Notification center
│   │   ├── layout.tsx              # App layout wrapper
│   │   ├── page.tsx                # Dashboard home
│   │   └── providers.tsx           # Context providers
│   ├── 📁 (public)/                # Public routes (auth)
│   │   └── 📁 auth/                # Login, register, etc.
│   ├── 📁 components/              # Reusable UI components
│   │   ├── 📁 common/              # Generic components
│   │   └── 📁 layout/              # Layout components
│   ├── 📁 lib/                     # Utility libraries
│   │   ├── 📁 features/            # Redux features
│   │   └── store.ts                # Redux store setup
│   ├── 📁 services/                # API services
│   │   └── apiClient.ts            # HTTP client
│   ├── 📁 types/                   # TypeScript definitions
│   │   └── index.ts                # Shared type exports
│   └── globals.css                 # Global styles
├── 📁 .github/                     # GitHub workflows
│   └── 📁 workflows/               # CI/CD pipelines
├── 📁 .vscode/                     # VS Code configuration
│   ├── extensions.json             # Recommended extensions
│   ├── launch.json                 # Debug configurations
│   ├── settings.json               # Editor settings
│   ├── snippets.code-snippets      # Code snippets
│   └── tasks.json                  # Task automation
├── 📁 public/                      # Static assets
├── 📁 scripts/                     # Build and utility scripts
├── 📄 .env.example                 # Environment template
├── 📄 .eslintrc.config.mjs         # ESLint configuration
├── 📄 .gitignore                   # Git ignore rules
├── 📄 .prettierrc                  # Prettier configuration
├── 📄 next.config.mjs              # Next.js configuration
├── 📄 package.json                 # Project dependencies
├── 📄 tailwind.config.js           # Tailwind CSS config
├── 📄 tsconfig.json                # TypeScript configuration
└── 📄 README.md                    # You are here! 👋
```

### 📝 **Key Directories Explained:**

- **`app/`** - Next.js App Router structure with route groups
- **`components/`** - Reusable UI components following atomic design
- **`types/`** - Centralized TypeScript type definitions
- **`services/`** - API integration and external service wrappers
- **`lib/`** - Utility functions, hooks, and configurations
- **`.vscode/`** - Optimized VS Code development environment

---

## 🎨 UI Components

### 🧱 **Component Architecture**

Our components follow **Atomic Design** principles:

```
🔸 Atoms        → Basic building blocks (Button, Input, Badge)
🔹 Molecules    → Simple combinations (SearchBox, FormField)
🔷 Organisms    → Complex components (Navbar, DataTable, Card)
🔶 Templates    → Page layouts and structures
🔺 Pages        → Complete user interfaces
```

### 📚 **Available Components**

#### **🎯 Core Components**

- **WorkCard** - Dashboard metric cards
- **DataTable** - Feature-rich data tables with sorting, filtering
- **Modal** - Accessible modal dialogs
- **Badge** - Status and category indicators
- **Button** - Various button styles and states

#### **📝 Form Components**

- **TextInput** - Text input with validation
- **SelectInput** - Dropdown selections
- **FloatingInput** - Modern floating label inputs
- **FormField** - Complete form field with error handling

#### **📊 Data Components**

- **AttendanceCalendar** - Interactive attendance calendar
- **LeaveBalanceCard** - Leave balance visualization
- **ApplicationCard** - Application status cards
- **PayslipCard** - Payroll information display

#### **🧭 Navigation Components**

- **Navbar** - Top navigation with user menu
- **Sidebar** - Main navigation sidebar
- **Breadcrumbs** - Navigation breadcrumb trail

### 🎨 **Styling Guidelines**

```typescript
// ✅ Good: Use Tailwind utility classes
<div className="bg-white shadow-md rounded-xl p-4">

// ✅ Good: Use CSS custom properties for theme colors
<div className="bg-bg-primary text-title">

// ✅ Good: Responsive design with mobile-first approach
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4">
```

### 🏷️ **Component Usage Examples**

```typescript
// WorkCard Component
<WorkCard
  icon={<ClockIcon className="h-5 w-5" />}
  label="Today's working period"
  value="2 hours 36 min"
  iconBg="bg-primary"
/>

// DataTable Component
<DataTable
  data={employees}
  columns={columns}
  searchable
  filterable
  pagination
/>

// Badge Component
<Badge status="approved" size="sm">
  Approved
</Badge>
```

---

## 🔧 Configuration

### ⚙️ **Environment Variables**

Create `.env.local` with required variables:

```bash
# API Configuration
NEXT_PUBLIC_API_URL=https://test-api.betopia.peoplixhr.com/api/

# Application Settings
NEXT_PUBLIC_APP_NAME=Betopia HRM
NEXT_PUBLIC_APP_VERSION=1.0.0

# Feature Flags (optional)
NEXT_PUBLIC_ENABLE_DARK_MODE=true
NEXT_PUBLIC_ENABLE_NOTIFICATIONS=true

# Development (optional)
NEXT_PUBLIC_DEBUG_MODE=false
```

### 🎯 **TypeScript Configuration**

Our `tsconfig.json` is optimized for:

- ✅ **Strict Mode** - Maximum type safety
- ✅ **Path Aliases** - Clean import statements
- ✅ **Latest ECMAScript** - Modern JavaScript features
- ✅ **Next.js Integration** - Seamless framework integration

```json
{
  "compilerOptions": {
    "strict": true,
    "baseUrl": ".",
    "paths": {
      "@/*": ["./app/*"],
      "@/components/*": ["./app/components/*"],
      "@/types/*": ["./app/types/*"]
    }
  }
}
```

### 🎨 **Tailwind Configuration**

Custom theme with brand colors:

```javascript
module.exports = {
  theme: {
    extend: {
      colors: {
        primary: '#F69348', // Brand orange
        'primary-hover': '#e87e2e', // Darker orange
        'bg-primary': '#ffffff', // Card backgrounds
        title: '#1F2937', // Text headings
        'title-sub': '#6B7280', // Subtitle text
      },
    },
  },
};
```

### 🔧 **ESLint & Prettier**

Automated code quality with:

- **ESLint** - Code quality rules
- **Prettier** - Consistent formatting
- **Auto-fix on save** - Seamless development experience

---

## 🧪 Testing

### 🎯 **Testing Strategy**

```bash
# Unit Tests (coming soon)
npm run test

# E2E Tests (coming soon)
npm run test:e2e

# Test Coverage (coming soon)
npm run test:coverage
```

### 🛡️ **Quality Assurance**

Current testing includes:

- ✅ **TypeScript Compilation** - Zero type errors
- ✅ **ESLint Validation** - Code quality standards
- ✅ **Build Testing** - Production build success
- ✅ **Pre-commit Hooks** - Automated validation

### 🔮 **Planned Testing**

- 🔄 **Unit Tests** with Jest and React Testing Library
- 🔄 **Component Tests** with Storybook
- 🔄 **E2E Tests** with Playwright
- 🔄 **Visual Regression** with Chromatic

---

## 🚢 Deployment

### 🌐 **Production Build**

```bash
# Create optimized production build
npm run build

# Test production build locally
npm start
```

### ☁️ **Deployment Platforms**

#### **Vercel (Recommended)**

```bash
# Install Vercel CLI
npm i -g vercel

# Deploy
vercel --prod
```

#### **Docker**

```dockerfile
# Dockerfile included for containerized deployment
docker build -t betopia-hrm-frontend .
docker run -p 3000:3000 betopia-hrm-frontend
```

#### **Traditional Hosting**

```bash
# Build static export (if needed)
npm run build
npm run export
```

### 🔒 **Environment Setup**

Production environment checklist:

- [ ] Set `NEXT_PUBLIC_API_URL` to production API
- [ ] Configure authentication endpoints
- [ ] Enable production optimizations
- [ ] Setup monitoring and analytics
- [ ] Configure CDN for static assets

---

## 📚 Documentation

### 📖 **Available Guides**

| Document                                         | Description                   | Audience       |
| ------------------------------------------------ | ----------------------------- | -------------- |
| [VSCODE_SETUP.md](./VSCODE_SETUP.md)             | Complete VS Code setup guide  | Developers     |
| [DEVELOPMENT_GUIDE.md](./DEVELOPMENT_GUIDE.md)   | Code quality and git workflow | Developers     |
| [SETUP_INSTRUCTIONS.md](./SETUP_INSTRUCTIONS.md) | Quick setup instructions      | New developers |

### 🏗️ **Architecture Decisions**

#### **Why Next.js App Router?**

- 🚀 **Performance** - Built-in optimizations
- 📱 **SSR/SSG** - Better SEO and performance
- 🔄 **File-based routing** - Intuitive navigation structure
- 🛡️ **Type safety** - Excellent TypeScript support

#### **Why Tailwind CSS?**

- ⚡ **Developer velocity** - Rapid prototyping
- 📦 **Small bundle size** - Purged unused styles
- 🎨 **Design consistency** - Utility-first approach
- 📱 **Responsive design** - Mobile-first utilities

#### **Why Redux Toolkit?**

- 🏪 **State management** - Predictable state updates
- 🔧 **DevTools** - Excellent debugging experience
- 📡 **API integration** - RTK Query for data fetching
- 🔄 **Time travel** - Debugging with state history

---

## 🤝 Contributing

We welcome contributions! Please see our [contribution guidelines](./CONTRIBUTING.md) for details.

### 🚀 **Quick Contribution Steps**

1. **Fork the repository**
2. **Create feature branch:** `git checkout -b feature/amazing-feature`
3. **Make changes** with proper commit messages
4. **Ensure quality:** `npm run validate`
5. **Submit pull request** with clear description

### 📝 **Commit Convention**

We use [Conventional Commits](https://conventionalcommits.org/):

```bash
feat: add user profile management
fix: resolve authentication timeout issue
docs: update API integration guide
style: improve component spacing
refactor: optimize data fetching logic
test: add unit tests for user service
chore: update dependencies
```

### 🏆 **Contributors**

<div align="center">

[![Contributors](https://contrib.rocks/image?repo=Betopia-Limited/betopia-hrm-frontend)](https://github.com/Betopia-Limited/betopia-hrm-frontend/graphs/contributors)

</div>

---

## 📞 Support & Community

### 🆘 **Getting Help**

- 📖 **Documentation** - Check our comprehensive guides
- 🐛 **Bug Reports** -
  [Create an issue](https://github.com/Betopia-Limited/betopia-hrm-frontend/issues)
- 💡 **Feature Requests** -
  [Request features](https://github.com/Betopia-Limited/betopia-hrm-frontend/issues)
- 💬 **Discussions** -
  [GitHub Discussions](https://github.com/Betopia-Limited/betopia-hrm-frontend/discussions)

### 🌟 **Show Your Support**

If this project helps you, please consider:

- ⭐ **Star this repository**
- 🍴 **Fork for your projects**
- 📢 **Share with your network**
- 🤝 **Contribute improvements**

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](./LICENSE) file for details.

---

## 🙏 Acknowledgments

### 💝 **Built With Love By**

<div align="center">

**Betopia Limited Development Team**

_Crafting the future of HR technology_

---

### 🛠️ **Powered By Amazing Open Source**

- [Next.js](https://nextjs.org/) - The React Framework for Production
- [TypeScript](https://www.typescriptlang.org/) - JavaScript That Scales
- [Tailwind CSS](https://tailwindcss.com/) - Rapidly Build Modern Websites
- [Redux Toolkit](https://redux-toolkit.js.org/) - The Official Redux Toolset
- [Heroicons](https://heroicons.com/) - Beautiful Hand-crafted SVG Icons

</div>

---

<div align="center">

**⚡ Made with passion for efficient HR management ⚡**

[🏠 Homepage](https://betopiagroup.com) • [📧 Contact](mailto:dev@betopiagroup.com) •
[🐦 Twitter](https://twitter.com/betopiagroup)

---

_© 2024 Betopia Limited. Built with ❤️ for the modern workplace._

</div>
