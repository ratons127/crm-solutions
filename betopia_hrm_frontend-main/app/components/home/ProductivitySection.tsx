'use client';

const ProductivitySection = () => {
  return (
    <section className="relative py-24 px-4 bg-gradient-to-b from-white to-orange-50 overflow-hidden">
      <div className="max-w-6xl mx-auto">
        {/* Section Title */}
        <div className="text-center mb-20">
          <h2 className="text-4xl md:text-5xl font-extrabold text-gray-900 leading-tight">
            Less paper work, more{' '}
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-orange-500 to-pink-600">
              people work!
            </span>
          </h2>
        </div>

        {/* ===== BLOCK 1 ===== */}
        <div className="grid md:grid-cols-2 gap-12 items-center mb-28">
          {/* Left visual */}
          <div className="relative">
            <div className="rounded-3xl bg-gradient-to-br from-blue-400 to-indigo-500 p-6 w-full max-w-sm mx-auto shadow-xl">
              <div className="bg-white rounded-2xl p-4 shadow-sm">
                <h3 className="font-semibold mb-3">Activity log</h3>
                <ul className="space-y-2 text-sm">
                  <li className="flex justify-between">
                    <span className="text-gray-700">Demi Wilkinson</span>
                    <span className="text-blue-600">updated bank details</span>
                  </li>
                  <li className="flex justify-between">
                    <span className="text-gray-700">Aliah Lane</span>
                    <span className="text-green-600">
                      requested casual leave
                    </span>
                  </li>
                  <li className="flex justify-between">
                    <span className="text-gray-700">Lara Steiner</span>
                    <span className="text-orange-600">
                      completed onboarding
                    </span>
                  </li>
                </ul>
              </div>
              <div className="absolute top-3 right-3 bg-white rounded-xl shadow-md px-3 py-2 text-sm">
                <p className="font-semibold text-green-600">eNPS Survey</p>
                <p className="text-gray-800 font-bold text-lg">8.5/10</p>
                <span className="text-xs text-green-500 font-semibold">
                  Promoter
                </span>
              </div>
            </div>
          </div>

          {/* Right text */}
          <div>
            <span className="uppercase text-gray-400 tracking-wider text-sm font-semibold">
              Improve Productivity
            </span>
            <h3 className="text-2xl md:text-3xl font-bold mt-2 mb-4 text-gray-900">
              Free up thousands of employee hours per year
            </h3>
            <p className="text-gray-600 text-sm leading-relaxed mb-5">
              Get rid of endless spreadsheets and binders collecting dust—or
              hours wasted on searching and emailing.
            </p>
            <ul className="space-y-2 text-sm text-gray-700">
              <li>✅ Single source for employee data</li>
              <li>✅ Ensure clarity in HR communication</li>
              <li>✅ Leave management without spreadsheets and email chains</li>
            </ul>
            <button className="mt-6 bg-orange-500 hover:bg-orange-600 text-white font-semibold text-sm px-5 py-2.5 rounded-xl shadow-md transition">
              Try HR for free →
            </button>
          </div>
        </div>

        {/* ===== BLOCK 2 ===== */}
        <div className="grid md:grid-cols-2 gap-12 items-center mb-28">
          {/* Left text */}
          <div>
            <span className="uppercase text-gray-400 tracking-wider text-sm font-semibold">
              Improve Team Efficiency
            </span>
            <h3 className="text-2xl md:text-3xl font-bold mt-2 mb-4 text-gray-900">
              Manage global teams
            </h3>
            <p className="text-gray-600 text-sm leading-relaxed mb-5">
              Simplify the challenges of managing a global team—whether it’s
              local compliance, currency conversion, onboarding, or everything
              in between.
            </p>
            <ul className="space-y-2 text-sm text-gray-700">
              <li>
                ✅ Maintain country-wise work schedules and leave policies
              </li>
              <li>✅ Take control of access, equipment and compliance</li>
              <li>✅ Manage HR and work at the same place</li>
            </ul>
            <button className="mt-6 bg-orange-500 hover:bg-orange-600 text-white font-semibold text-sm px-5 py-2.5 rounded-xl shadow-md transition">
              Try HR for free →
            </button>
          </div>

          {/* Right visual */}
          <div className="relative">
            <div className="rounded-3xl bg-gradient-to-br from-pink-400 to-rose-400 p-6 w-full max-w-sm mx-auto shadow-xl text-white">
              <div className="bg-white/90 backdrop-blur-xl rounded-2xl p-4">
                <div className="grid grid-cols-2 gap-4">
                  {[
                    { country: 'Norway', members: 10 },
                    { country: 'USA', members: 23 },
                    { country: 'India', members: 8 },
                    { country: 'South Africa', members: 12 },
                  ].map((team, i) => (
                    <div
                      key={i}
                      className="p-3 bg-white rounded-xl shadow-sm text-gray-700"
                    >
                      <p className="font-semibold">{team.country}</p>
                      <p className="text-xs text-gray-500">
                        {team.members} members
                      </p>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* ===== BLOCK 3 ===== */}
        <div className="grid md:grid-cols-2 gap-12 items-center">
          {/* Left visual */}
          <div className="relative">
            <div className="rounded-3xl bg-gradient-to-br from-orange-300 to-amber-400 p-6 w-full max-w-sm mx-auto shadow-xl">
              <div className="bg-white rounded-2xl p-4 shadow-sm">
                <div className="flex justify-between mb-3">
                  <h4 className="font-semibold text-gray-900 text-sm">
                    Employee availability
                  </h4>
                  <button className="text-xs text-blue-600 font-medium">
                    View report
                  </button>
                </div>
                <div className="h-32 flex items-end justify-between">
                  {[40, 60, 50, 70, 80, 90, 65, 85, 60, 70, 75, 80].map(
                    (v, i) => (
                      <div
                        key={i}
                        className="w-2.5 bg-gradient-to-t from-orange-400 to-pink-400 rounded-full"
                        style={{ height: `${v}%` }}
                      ></div>
                    )
                  )}
                </div>
              </div>
            </div>
          </div>

          {/* Right text */}
          <div>
            <span className="uppercase text-gray-400 tracking-wider text-sm font-semibold">
              Work Smarter
            </span>
            <h3 className="text-2xl md:text-3xl font-bold mt-2 mb-4 text-gray-900">
              Modern, intuitive and automated!
            </h3>
            <p className="text-gray-600 text-sm leading-relaxed mb-5">
              Create an HR process that is automated & everybody loves.
            </p>
            <ul className="space-y-2 text-sm text-gray-700">
              <li>✅ Simplify and streamline your HR processes</li>
              <li>✅ Automate routine tasks to save time and reduce errors</li>
              <li>✅ Empower your team with information and clarity</li>
            </ul>
            <button className="mt-6 bg-orange-500 hover:bg-orange-600 text-white font-semibold text-sm px-5 py-2.5 rounded-xl shadow-md transition">
              Try Betopia HR for free →
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

export default ProductivitySection;
