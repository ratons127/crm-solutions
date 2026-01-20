'use client';

const features = [
  {
    icon: (
      <svg
        className="w-6 h-6 text-white"
        fill="currentColor"
        viewBox="0 0 20 20"
      >
        <path d="M5.5 16a3.5 3.5 0 01-.369-6.98 4 4 0 117.753-1.977A4.5 4.5 0 1113.5 16h-8z" />
      </svg>
    ),
    bgColor: 'bg-cyan-400',
    cardBg: 'bg-blue-50',
    title: 'Multi-tenant SaaS Architecture',
    description: 'Secure and scalable for any company size',
  },
  {
    icon: (
      <svg
        className="w-6 h-6 text-white"
        fill="currentColor"
        viewBox="0 0 20 20"
      >
        <path
          fillRule="evenodd"
          d="M6.625 2.655A9 9 0 0119 11a1 1 0 11-2 0 7 7 0 00-9.625-6.492 1 1 0 11-.75-1.853zM4.662 4.959A1 1 0 014.75 6.37 6.97 6.97 0 003 11a1 1 0 11-2 0 8.97 8.97 0 012.25-5.953 1 1 0 011.412-.088z"
          clipRule="evenodd"
        />
        <path
          fillRule="evenodd"
          d="M5 11a5 5 0 1110 0 1 1 0 11-2 0 3 3 0 10-6 0c0 1.677-.345 3.276-.968 4.729a1 1 0 11-1.838-.789A9.964 9.964 0 005 11z"
          clipRule="evenodd"
        />
      </svg>
    ),
    bgColor: 'bg-green-500',
    cardBg: 'bg-green-50',
    title: 'Biometric Integration Ready',
    description: 'Cams API, ZKTeco, and custom SDKs',
  },
  {
    icon: (
      <svg
        className="w-6 h-6 text-white"
        fill="currentColor"
        viewBox="0 0 20 20"
      >
        <path
          fillRule="evenodd"
          d="M10 18a8 8 0 100-16 8 8 0 000 16zM4.332 8.027a6.012 6.012 0 011.912-2.706C6.512 5.73 6.974 6 7.5 6A1.5 1.5 0 019 7.5V8a2 2 0 004 0 2 2 0 011.523-1.943A5.977 5.977 0 0116 10c0 .34-.028.675-.083 1H15a2 2 0 00-2 2v2.197A5.973 5.973 0 0110 16v-2a2 2 0 00-2-2 2 2 0 01-2-2 2 2 0 00-1.668-1.973z"
          clipRule="evenodd"
        />
      </svg>
    ),
    bgColor: 'bg-purple-500',
    cardBg: 'bg-purple-50',
    title: 'Localized Compliance',
    description: 'Built with Bangladesh Labour Act 2006 & Rules 2015',
  },
  {
    icon: (
      <svg
        className="w-6 h-6 text-white"
        fill="currentColor"
        viewBox="0 0 20 20"
      >
        <path d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z" />
      </svg>
    ),
    bgColor: 'bg-orange-300',
    cardBg: 'bg-orange-50',
    title: 'User-Friendly Dashboards',
    description: 'HR/Admin and Employee self-service layouts',
  },
  {
    icon: (
      <svg
        className="w-6 h-6 text-white"
        fill="currentColor"
        viewBox="0 0 20 20"
      >
        <path
          fillRule="evenodd"
          d="M2.166 4.999A11.954 11.954 0 0010 1.944 11.954 11.954 0 0017.834 5c.11.65.166 1.32.166 2.001 0 5.225-3.34 9.67-8 11.317C5.34 16.67 2 12.225 2 7c0-.682.057-1.35.166-2.001zm11.541 3.708a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
          clipRule="evenodd"
        />
      </svg>
    ),
    bgColor: 'bg-purple-500',
    cardBg: 'bg-purple-50',
    title: 'Cloud-native Reliability',
    description: '99.9% uptime, secure backups, and role-based access',
  },
  {
    icon: (
      <svg
        className="w-6 h-6 text-white"
        fill="currentColor"
        viewBox="0 0 20 20"
      >
        <path
          fillRule="evenodd"
          d="M11.3 1.046A1 1 0 0112 2v5h4a1 1 0 01.82 1.573l-7 10A1 1 0 018 18v-5H4a1 1 0 01-.82-1.573l7-10a1 1 0 011.12-.38z"
          clipRule="evenodd"
        />
      </svg>
    ),
    bgColor: 'bg-orange-400',
    cardBg: 'bg-orange-50',
    title: 'Real-time Insights',
    description: 'Live attendance feeds, exception reports, and alerts',
  },
];

const WhyChooseSection = () => {
  return (
    <div className="relative overflow-hidden bg-gradient-to-br from-orange-50 via-pink-50 to-purple-50 py-16 px-4">
      {/* 🌈 Top-right backdrop blur */}
      <div
        className="absolute top-0
      right-40 w-96 h-96 bg-gradient-to-bl from-[#ebba95] via-[#ecbb96] to-transparent opacity-40 blur-3xl backdrop-blur-3xl rounded-full"
      />

      {/* 🌈 Bottom-left backdrop blur */}
      <div className="absolute bottom-0 left-80 w-96 h-96 bg-gradient-to-tr from-[#FB64B6] to-[#F54900] opacity-40 blur-3xl backdrop-blur-3xl rounded-full" />

      <div className="max-w-6xl mx-auto relative z-10">
        {/* Header */}
        <div className="text-center mb-16">
          <h1 className="text-3xl font-bold text-gray-900 mb-4">
            Why Choose{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-orange-500 to-pink-600">
              Betopia HRM
            </span>
            ?
          </h1>
          <p className="text-gray-500 max-w-sm text-sm mx-auto">
            A modern HR platform designed for today's distributed,
            compliance-focused organizations.
          </p>
        </div>

        {/* Features Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {features.map((feature, index) => (
            <div
              key={index}
              className={`${feature.cardBg} rounded-2xl p-6 shadow-sm hover:shadow-md transition-shadow duration-300 relative overflow-hidden`}
            >
              <div className="absolute top-0 right-0 w-14 h-14">
                <div className="bg-gradient-to-bl from-[#2B7FFF] to-[#00B8DB] absolute top-0 right-0 w-full h-full rounded-bl-[80px] opacity-5" />
              </div>

              <div className="relative z-10">
                <div
                  className={`${feature.bgColor} w-12 h-12 rounded-xl flex items-center justify-center mb-4`}
                >
                  {feature.icon}
                </div>
                <h3 className="text-gray-900 font-semibold text-lg mb-2">
                  {feature.title}
                </h3>
                <p className="text-gray-600 text-sm leading-relaxed">
                  {feature.description}
                </p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default WhyChooseSection;
