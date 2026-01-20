import { CalendarProvider } from './calendarContext';
import LeftSection from './section/leftSection';
import RightSection from './section/rightSection';
import Breadcrumbs from '@/components/common/Breadcrumbs';

export default function CalenderPage() {
  return (
    <CalendarProvider>
      <div className="h-auto">
        <div className="bg-white border-b border-gray-300 py-2 px-5">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h1 className="text-xl font-semibold text-gray-700 py-3">
                Company Calendar
              </h1>
              <Breadcrumbs />
            </div>
            <div>
              <LeftSection />
            </div>

            {/* <div className="flex items-center gap-2 mt-3 sm:mt-0">
              <Link href="/administration/calendar">
                <Button
                  leftSection={<IoCalendarOutline size={14} />}
                  variant="filled"
                  color="orange"
                  size="md"
                >
                  Calendar
                </Button>
              </Link>
              <Link href="/administration/calendar/groups">
                <Button
                  leftSection={<RiGroupFill size={14} />}
                  variant="outline"
                  color="black"
                  size="md"
                >
                  Group
                </Button>
              </Link>
            </div> */}
          </div>
        </div>

        {/* Content */}
        <div className="flex justify-around gap-5 mt-5">
          <div className="w-3/2">
            <div className="bg-white rounded-2xl shadow-md border border-gray-200 px-5 py-5">
              <RightSection />
            </div>
          </div>
          {/* <div className="w-1/2">
            <LeftSection />
          </div> */}
        </div>
      </div>
    </CalendarProvider>
  );
}
