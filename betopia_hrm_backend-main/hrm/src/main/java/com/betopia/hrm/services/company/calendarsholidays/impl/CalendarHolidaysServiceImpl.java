package com.betopia.hrm.services.company.calendarsholidays.impl;

import com.betopia.hrm.domain.company.entity.CalendarHolidays;
import com.betopia.hrm.domain.company.repository.CalendarHolidaysRepository;
import com.betopia.hrm.domain.company.request.CalendarHolidaysRequest;
import com.betopia.hrm.domain.company.request.UpdateIsHolidayRequest;
import com.betopia.hrm.services.company.calendarsholidays.CalendarHolidaysService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarHolidaysServiceImpl implements CalendarHolidaysService {

    private final CalendarHolidaysRepository calendarHolidaysRepository;

    private final JdbcTemplate jdbcTemplate;


    public CalendarHolidaysServiceImpl(CalendarHolidaysRepository calendarHolidaysRepository, JdbcTemplate jdbcTemplate) {
        this.calendarHolidaysRepository = calendarHolidaysRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CalendarHolidays> getHolidaysForCurrentYear() {
        return calendarHolidaysRepository.findHolidaysForCurrentYear();
    }

    @Override
    public List<CalendarHolidays> updateCalendars(List<CalendarHolidaysRequest> requests) {
        List<CalendarHolidays> holidays = new ArrayList<>();

        for (CalendarHolidaysRequest request: requests) {
            CalendarHolidays calendarHolidays = calendarHolidaysRepository.findById(request.id()).orElseThrow();

            calendarHolidays.setHolidayDate(request.holidayDate() != null ? request.holidayDate() : calendarHolidays.getHolidayDate());
            calendarHolidays.setIsHoliday(request.isHoliday() != null ? request.isHoliday() : calendarHolidays.getIsHoliday());
            calendarHolidays.setDescription(request.description() != null ? request.description() : calendarHolidays.getDescription());
            calendarHolidays.setWeekendType(request.weekendType() != null ? request.weekendType() : calendarHolidays.getWeekendType());

            holidays.add(calendarHolidays);
        }

        return calendarHolidaysRepository.saveAll(holidays);
    }

    @Override
    public List<CalendarHolidays> updateIsHoliday(List<UpdateIsHolidayRequest> requests) {

        List<CalendarHolidays> updateHolidays = new ArrayList<>();

        for (UpdateIsHolidayRequest request: requests) {
            CalendarHolidays holiday = calendarHolidaysRepository.findById(request.id())
                    .orElseThrow(() -> new RuntimeException("CalendarHoliday not found with id: " + request.id()));

            holiday.setIsHoliday(request.isHoliday());
            holiday.setUpdatedAt(LocalDateTime.now());

            updateHolidays.add(holiday);
        }

        return calendarHolidaysRepository.saveAll(updateHolidays);
    }

    @Override
    @Scheduled(fixedRate = 3600000)
    public void updateStatusOfHolidays() {

        LocalDate today = LocalDate.now();

//        LocalDate today = LocalDate.of(2025, 12, 31);

        // finding the last day of the year
        if(today.getMonth() == Month.DECEMBER && today.getDayOfMonth() == 31) {

            int currentYear = today.getYear();

            // Update all calendar holidays of current year to status = 0
            calendarHolidaysRepository.updateStatusByYear(currentYear, false);

            // Call database function to generate next year's calendar
            int nextYear = currentYear + 1;
            String sql = "SELECT generate_calendar(?, ?, ?, ?)";
            jdbcTemplate.queryForObject(sql, Long.class, nextYear, "Company Calendar", "HOLIDAY", true);

        }

    }

    @Override
    public boolean isHolidayOrWeekend(LocalDate date) {
        return calendarHolidaysRepository
                .findByHolidayDateAndIsHoliday(date, true)
                .isPresent();
    }
}
