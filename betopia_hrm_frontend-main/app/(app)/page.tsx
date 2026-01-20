
'use client';

import { Loader, Text } from '@mantine/core';
import { FaUserTie } from 'react-icons/fa';
import { IoCalendarNumberOutline, IoCalendarOutline } from 'react-icons/io5';
import { LuAlarmClockMinus } from 'react-icons/lu';
import { DonutChart } from '@mantine/charts';
import { useEffect, useState } from 'react';
import dayjs from 'dayjs';
import duration from 'dayjs/plugin/duration';
import relativeTime from 'dayjs/plugin/relativeTime';

import CheckInCard from '../components/common/CheckInCard';
import LeaveBalanceCard from '../components/common/LeaveBalanceCard';
import WorkCard from '../components/common/WorkCard';
import AttendanceCalendar, {
  AttendanceMap,
  AttendanceStatus
} from '../components/common/calendar/AttendanceCalendar';

import { useGetAttendanceStatusQuery } from '@/lib/features/calendar/attendanceStatusAPI'; 
import { useGetAttendanceSummaryByEmployeeQuery } from '@/services/api/attendance/attendanceInOutAPI';
import { getEmployeeById, getSupervisorName } from '@/lib/features/employees/employeeAPI';
import { useGetEmployeeBalanceListQuery } from '@/lib/features/leave/balanceEmployee/balanceEmployeeAPI';
import { useAppSelector } from '@/lib/hooks';

dayjs.extend(duration);
dayjs.extend(relativeTime);

export default function Dashboard() {
  const { user } = useAppSelector((s) => s.auth);
  const employeeId = user?.employeeId || null;

   // employee info
  const [employee, setEmployee] = useState<any>(null);

  useEffect(() => {
    if (employeeId) {
      getEmployeeById(employeeId)
        .then((data) => {
          setEmployee(data);
        })
        .catch((err) => console.error('Failed to load employee:', err));
    }
  }, [employeeId]);

  /* ================================
     🔹 Fetch attendance summary (Check-in/out)
  ================================ */
  const {
    data: summaryData,
    isLoading: summaryLoading,
    isError: summaryError,
  } = useGetAttendanceSummaryByEmployeeQuery({
    employeeId: employeeId as number,
    limit: 8,
  });

  // 🔹 Format time in BD timezone
  const formatTimeOnly = (dateTime: string) => {
    if (!dateTime) return '--';
    const date = new Date(dateTime);
    if (!isNaN(date.getTime())) {
      return date.toLocaleTimeString('en-BD', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true,
        timeZone: 'Asia/Dhaka',
      });
    }
    const match = dateTime.match(/\b\d{1,2}:\d{2}\s?(?:AM|PM)\b/i);
    return match ? match[0] : '--';
  };

  const checkInData =
    summaryData?.map((d) => ({
      date: d.workDate,
      checkIn: d.inTime ? formatTimeOnly(d.inTime) : '--',
      checkOut: d.outTime ? formatTimeOnly(d.outTime) : '--',
      duration: d.totalWorkDuration || '--',
    })) ?? [];

  /* ================================
     🔹 Live working duration (Today)
  ================================ */
  const [todayDuration, setTodayDuration] = useState('--');

  useEffect(() => {
    if (!checkInData || checkInData.length === 0) return;

    const today = new Date().toISOString().split('T')[0];

    const parseReadableDate = (str: string): string | null => {
      try {
        const [monthName, day, year] = str.replace(',', '').split(' ');
        const months = {
          January: '01',
          February: '02',
          March: '03',
          April: '04',
          May: '05',
          June: '06',
          July: '07',
          August: '08',
          September: '09',
          October: '10',
          November: '11',
          December: '12',
        };
        const mm = months[monthName as keyof typeof months];
        const dd = day.padStart(2, '0');
        return `${year}-${mm}-${dd}`;
      } catch {
        return null;
      }
    };

    const todayData = checkInData.find((d) => {
      const normalized = parseReadableDate(d.date);
      return normalized === today;
    });

    if (
      todayData &&
      todayData.checkIn &&
      todayData.checkIn !== '--' &&
      (!todayData.checkOut || todayData.checkOut === '--')
    ) {
      const parseCheckInToDate = (timeStr: string): Date | null => {
        try {
          const [time, modifier] = timeStr.split(' ');
          let [hours, minutes] = time.split(':').map(Number);
          if (modifier?.toUpperCase() === 'PM' && hours !== 12) hours += 12;
          if (modifier?.toUpperCase() === 'AM' && hours === 12) hours = 0;
          const now = new Date();
          return new Date(
            now.getFullYear(),
            now.getMonth(),
            now.getDate(),
            hours,
            minutes,
            0,
            0
          );
        } catch {
          return null;
        }
      };

      const checkInTime = parseCheckInToDate(todayData.checkIn);
      if (!checkInTime) return;

      const updateDuration = () => {
        const now = new Date();
        const diffMs = now.getTime() - checkInTime.getTime();

        if (diffMs > 0) {
          const hrs = Math.floor(diffMs / (1000 * 60 * 60));
          const mins = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60));
          setTodayDuration(`${hrs} hr ${mins} min`);
        } else {
          setTodayDuration('0 hr 0 min');
        }
      };

      updateDuration();
      const timer = setInterval(updateDuration, 60 * 1000);
      return () => clearInterval(timer);
    } else {
      setTodayDuration(todayData?.duration ?? '--');
    }
    return undefined
  }, [checkInData]);

  /* ================================
     🔹 Supervisor Info
  ================================ */
  const [supervisor, setSupervisor] = useState<string | null>(null);
  const [employeeSerialId, setEmployeeSerialId] = useState<string | null>(null);
  const [supervisorImage, setSupervisorImage] = useState<string | null>(null);
  const [supervisorLoading, setSupervisorLoading] = useState(true);
  const [supervisorError, setSupervisorError] = useState<string | null>(null);

  useEffect(() => {
    const fetchSupervisor = async () => {
      if (!user) return;
      try {
        const info = await getSupervisorName();
        if (info) {
          setSupervisor(
            [info.firstName, info.lastName].filter(Boolean).join(' ').trim()
          );
          setEmployeeSerialId(info?.employeeSerialId);
          setSupervisorImage(info?.imageUrl ?? '')
        } else {
          setSupervisor(null);
        }
      } catch (err: any) {
        setSupervisorError(
          err.response?.data?.message || 'Failed to fetch supervisor name'
        );
      } finally {
        setSupervisorLoading(false);
      }
    };
    fetchSupervisor();
  }, [user]);

  /* ================================
     🔹 Employee Leave Balance (LIVE)
  ================================ */
  const currentYear = new Date().getFullYear();
  const getLeaveColor = (name: string) => {
    if (name.toLowerCase().includes('sick')) return '#f87171';
    if (name.toLowerCase().includes('casual')) return '#60a5fa';
    if (name.toLowerCase().includes('earn')) return '#34d399';
    return '#F69348';
  };

  const {
    data: leaveBalanceRes,
    isLoading: leaveLoading,
    isError: leaveError,
  } = useGetEmployeeBalanceListQuery(
    { employeeId: employeeId as number, year: currentYear },
    { skip: !employeeId }
  );

  const leaveBalanceList = Array.isArray(leaveBalanceRes?.data)
    ? leaveBalanceRes.data
    : [];

  /* ================================
     🔹 Attendance Calendar (LIVE)
  ================================ */
  const [dataMap, setDataMap] = useState<AttendanceMap>({});
  const {
    data: attendanceStatusRes,
    isLoading: calendarLoading,
    isError: calendarError,
  } = useGetAttendanceStatusQuery();

  useEffect(() => {
  if (attendanceStatusRes?.data) {
    const mapped: AttendanceMap = {};

    attendanceStatusRes.data.forEach((item) => {
      const normalized = item.status.toUpperCase();

      // map backend → frontend values
      const mapStatus = (status: string): AttendanceStatus => {
        switch (status) {
          case 'PRESENT':
            return 'present';
          case 'LATE':
            return 'late';
          case 'EARLY_LEAVE':
            return 'early_leave';
          case 'HALF_DAY':
            return 'half_day';
          case 'ABSENT':
            return 'absent';
          case 'LEAVE':
            return 'leave';
          default:
            return 'off';
        }
      };

      mapped[item.statusDate] = mapStatus(normalized);
    });

    setDataMap(mapped);
  }
}, [attendanceStatusRes]);


  /* ================================
     🔹 Length of Service
  ================================ */
  let lengthOfService = '--';
  if (employee?.dateOfJoining) {
    const joinDate = dayjs(employee.dateOfJoining);
    const now = dayjs();
    const totalMonths = now.diff(joinDate, 'month');
    const years = Math.floor(totalMonths / 12);
    const months = totalMonths % 12;
    const days = now.diff(joinDate.add(totalMonths, 'month'), 'day');
    if (years > 0) {
      lengthOfService = `${years} year${years > 1 ? 's' : ''} ${months} month${
        months !== 1 ? 's' : ''
      } ${days} day${days !== 1 ? 's' : ''}`;
    } else if (months > 0) {
      lengthOfService = `${months} month${months !== 1 ? 's' : ''} ${days} day${
        days !== 1 ? 's' : ''
      }`;
    } else {
      lengthOfService = `${days} day${days !== 1 ? 's' : ''}`;
    }
  }

  /* ================================
  🔹 Static Demo Data
  ================================ */
  // const chartData = [
  //   { month: 'January', Smartphones: 1200, Laptops: 900, Tablets: 200 },
  //   { month: 'February', Smartphones: 900, Laptops: 600, Tablets: 300 },
  //   { month: 'March', Smartphones: 1300, Laptops: 800, Tablets: 400 },
  // ];

  // const LineChartData = [
  //   { date: 'Mar 22', Apples: 110 },
  //   { date: 'Mar 23', Apples: 60 },
  //   { date: 'Mar 24', Apples: 80 },
  //   { date: 'Mar 25', Apples: 70 },
  //   { date: 'Mar 26', Apples: 50 },
  //   { date: 'Mar 27', Apples: 40 },
  //   { date: 'Mar 28', Apples: 120 },
  //   { date: 'Mar 29', Apples: 80 },
  // ];
  // const applications = [
  //   {
  //     subject: 'Application subject goes here',
  //     description:
  //       'Lorem ipsum dolor sit amet consectetur. Rius enim phasellus...',
  //     status: 'Pending' as const,
  //     date: '2 days ago',
  //   },
  //   {
  //     subject: 'Application subject goes here',
  //     description:
  //       'Lorem ipsum dolor sit amet consectetur. Rius enim phasellus...',
  //     status: 'In review' as const,
  //     date: '2 Sep, 25',
  //   },
  //   {
  //     subject: 'Application subject goes here',
  //     description:
  //       'Lorem ipsum dolor sit amet consectetur. Rius enim phasellus...',
  //     status: 'Approved' as const,
  //     date: '6 Sep, 25',
  //   },
  // ];

  // const payslips = [
  //   {
  //     amount: '20,000 TK',
  //     bank: 'SCB bank',
  //     date: '05 Sep, 2025 at 08:30 PM',
  //     status: 'Received' as const,
  //   },
  //   {
  //     amount: '20,000 TK',
  //     bank: 'Meghna bank',
  //     date: '05 Sep, 2025 at 08:30 PM',
  //     status: 'Received' as const,
  //   },
  // ];

  /* ================================
     🔹 Render
  ================================ */
  return (
    <div className="py-2">
      {/* Top Work Cards */}
      <div
        className={`grid grid-cols-1 md:grid-cols-2 ${
          user?.roles?.name === 'SuperAdmin'
            ? 'lg:grid-cols-4'
            : 'lg:grid-cols-5'
        } gap-4`}
      >
        <WorkCard
          icon={<LuAlarmClockMinus className="h-5 w-5" />}
          label="Today's working period"
          value={todayDuration}
          iconBg="bg-primary"
        />

        <WorkCard
          icon={<IoCalendarNumberOutline className="h-5 w-5" />}
          label="Joining date"
          value={
            employee?.dateOfJoining
              ? dayjs(employee.dateOfJoining).format('DD MMM, YYYY')
              : '--'
          }
          iconBg="bg-primary"
        />

        <WorkCard
          icon={<IoCalendarOutline className="h-5 w-5" />}
          label="Length of service"
          value={lengthOfService}
          iconBg="bg-primary"
        />

        {user?.roles?.name !== 'SuperAdmin' && (
          <WorkCard
            imageUrl={supervisorImage || ''}
            icon={<FaUserTie className="h-5 w-5" />}
            label="Supervisor"
            value={
              supervisorLoading
                ? 'Loading...'
                : supervisorError
                ? 'N/A'
                : supervisor || 'Not Assigned'
            }
            title={employeeSerialId ? `ID: ${employeeSerialId}` : ''}
            bgGradient="bg-gradient-to-r from-orange-300 to-orange-400"
            iconBg='bg-blue-400'
            imageSize={40}
            imageRounded={true}
          />
        )}
      </div>
      {/* Charts */}
      {/* <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mt-4">
        <div className="bg-bg-primary shadow-md rounded-xl p-4">
          <h1 className="text-lg font-semibold pb-2">Sales</h1>
          <BarChart
            h={280}
            data={chartData}
            dataKey="month"
            unit="$"
            series={[
              { name: 'Smartphones', color: 'violet.6' },
              { name: 'Laptops', color: 'blue.6' },
              { name: 'Tablets', color: 'teal.6' },
            ]}
          />
        </div>

        <div className="bg-bg-primary shadow-md rounded-xl p-4">
          <h1 className="text-lg font-semibold pb-2">Sales Growth</h1>
          <LineChart
            h={280}
            data={LineChartData}
            dataKey="date"
            series={[{ name: 'Apples', color: 'indigo.6' }]}
            curveType="linear"
            connectNulls
          />
        </div>
      </div> */}

      {/* Calendar + Attendance + Leave + Chart */}
      <div className="grid grid-cols-1 lg:grid-cols-[4fr_2.8fr_2.7fr_2.4fr] gap-4 mt-4">
        {/* Attendance Calendar */}
        {user?.roles?.name === 'Employee' && (
          <div className="bg-bg-primary shadow-md rounded-xl p-4">
            {calendarLoading ? (
              <Loader />
            ) : calendarError ? (
              <Text color="red">Failed to load attendance calendar.</Text>
            ) : (
              <AttendanceCalendar
                initialYear={new Date().getFullYear()}
                initialMonth={new Date().getMonth()}
                data={dataMap}
                onMonthChange={() => {}}
                onDayClick={(iso, status) =>
                  console.log('Clicked:', iso, 'Status:', status)
                }
              />
            )}
          </div>
        )}

        {/* Live Attendance Summary */}
        {user?.roles?.name !== 'SuperAdmin' && (
          <div>
            {summaryLoading ? (
              <Loader />
            ) : summaryError ? (
              <Text color="red">Failed to load attendance data.</Text>
            ) : (
              <CheckInCard title="Check In" records={checkInData} />
            )}
          </div>
        )}

        {/* Leave Balance */}
        <div className="bg-bg-primary shadow-md rounded-xl p-4">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-800">
              Leave Balance
            </h2>
          </div>
          {leaveLoading ? (
            <Loader />
          ) : leaveError ? (
            <Text color="red">Failed to load leave balance.</Text>
          ) : leaveBalanceList.length === 0 ? (
            <Text color="dimmed" size="sm">
              No leave balance data available.
            </Text>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              {leaveBalanceList.map((item) => (
                <LeaveBalanceCard
                  key={item.id}
                  title={item.leaveType?.name || 'Unknown Leave'}
                  taken={item.usedDays ?? 0}
                  remaining={item.balance ?? 0}
                  total={item.entitledDays ?? 0}
                  color={getLeaveColor(item.leaveType?.name || '')}
                />
              ))}
            </div>
          )}
        </div>

        {/* Donut Chart */}
        {user?.roles?.name === 'SuperAdmin' && (
          <div className="bg-bg-primary shadow-md rounded-xl p-4">
            <h1 className="text-lg font-semibold pb-2">
              Monthly Income Growth  - Coming Soon
            </h1>
            <div className="flex flex-col items-center justify-center w-full">
              <div className="py-4">
                <DonutChart
                  size={199}
                  withLabelsLine
                  labelsType="percent"
                  withLabels
                  data={[
                    { name: 'USA', value: 400, color: 'indigo.6' },
                    { name: 'India', value: 300, color: 'yellow.6' },
                    { name: 'Japan', value: 100, color: 'teal.6' },
                    { name: 'Other', value: 200, color: 'gray.6' },
                  ]}
                />
              </div>
            </div>
          </div>
        )}
      </div>
      {/* Applications + Payslips */}
       {/* <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mt-4">
         <div className="bg-bg-primary shadow-md rounded-xl p-4">
          <h2 className="text-lg font-semibold text-gray-800 mb-4">
           My Applications
           </h2>
           <div className="max-h-[400px] overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-gray-300 scrollbar-track-gray-100">
             <div className="divide-y divide-gray-200">
              {applications.map((app, idx) => (
                 <ApplicationCard key={idx} index={idx + 1} {...app} />
               ))}
             </div>
           </div>
         </div>

         <div className="bg-bg-primary shadow-md rounded-xl p-4">
           <div className="flex items-center justify-between mb-4">
             <h2 className="text-lg font-semibold text-gray-800">My Payslips</h2>
             <button className="text-sm text-gray-600 hover:text-gray-900">
               See all
             </button>
           </div>
           <div className="divide-y divide-gray-200">
             {payslips.map((p, idx) => (
               <PayslipCard key={idx} {...p} />
             ))}
           </div>
         </div>
       </div>  */}
    </div>
  );
}
