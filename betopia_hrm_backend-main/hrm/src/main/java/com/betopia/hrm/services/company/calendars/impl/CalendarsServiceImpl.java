package com.betopia.hrm.services.company.calendars.impl;


import com.betopia.hrm.domain.company.entity.Calendars;
import com.betopia.hrm.domain.company.repository.CalendarsRepository;
import com.betopia.hrm.domain.company.request.CalendarsRequest;
import com.betopia.hrm.services.company.calendars.CalendarsService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CalendarsServiceImpl implements CalendarsService {

    private final CalendarsRepository calendarsRepository;

    public CalendarsServiceImpl(CalendarsRepository calendarsRepository) {
        this.calendarsRepository = calendarsRepository;
    }

    @Override
    public List<Calendars> getAllCalendars() {
        return calendarsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Calendars getCalendarsById(Long id) {
        return calendarsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendars not found with id: " + id));
    }

    @Override
    public Calendars store(CalendarsRequest request) {
        Calendars calendars = new Calendars();
        calendars.setName(request.name());
        calendars.setDescription(request.description());
        calendars.setType(request.type());
        calendars.setYear(request.year());
        calendars.setIsDefault(request.isDefault());
        calendars.setCreatedAt(request.createdAt());
        calendars.setUpdatedAt(request.updatedAt());
        return calendarsRepository.save(calendars);
    }

    @Override
    public Calendars updateCalendars(Long id, CalendarsRequest request) {
        Calendars calendars = calendarsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendars not found with id: " + id));

        calendars.setName(request.name() != null ? request.name() : calendars.getName());
        calendars.setDescription(request.description() != null ? request.description() : calendars.getDescription());

        calendars.setType(request.type() != null ? request.type() : calendars.getType());
        calendars.setYear(request.year() != null ? request.year() : calendars.getYear());
        calendars.setIsDefault(request.isDefault() != null ? request.isDefault() : calendars.getIsDefault());
        calendars.setUpdatedAt(request.updatedAt() != null ? request.updatedAt() : calendars.getUpdatedAt());
        return calendarsRepository.save(calendars);
    }

    @Override
    public void deleteCalendars(Long id) {
        Calendars calendars = calendarsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendars not found with id: " + id));

        calendars.setDeletedAt(LocalDateTime.now());

        calendarsRepository.delete(calendars);
    }
}
