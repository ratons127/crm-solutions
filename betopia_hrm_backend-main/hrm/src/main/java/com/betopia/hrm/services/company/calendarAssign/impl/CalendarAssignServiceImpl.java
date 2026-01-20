package com.betopia.hrm.services.company.calendarAssign.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.*;
import com.betopia.hrm.domain.company.repository.*;
import com.betopia.hrm.domain.company.request.CalenderAssignRequest;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.services.company.calendarAssign.CalendarAssignService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarAssignServiceImpl implements CalendarAssignService {

    private final CalendarAssignRepository calendarAssignRepository;

    private final CalendarsRepository calendarsRepository;

    private final CompanyRepository companyRepository;

    private final BusinessUnitRepository businessUnitRepository;

    private final WorkplaceGroupRepository workplaceGroupRepository;

    private final WorkplaceRepository workplaceRepository;

    private final DepartmentRepository departmentRepository;

    private final TeamRepository teamRepository;

    public CalendarAssignServiceImpl(CalendarAssignRepository calendarAssignRepository, CalendarsRepository calendarsRepository, CompanyRepository companyRepository, BusinessUnitRepository businessUnitRepository, WorkplaceGroupRepository workplaceGroupRepository, WorkplaceRepository workplaceRepository, DepartmentRepository departmentRepository, TeamRepository teamRepository) {
        this.calendarAssignRepository = calendarAssignRepository;
        this.calendarsRepository = calendarsRepository;
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.workplaceGroupRepository = workplaceGroupRepository;
        this.workplaceRepository = workplaceRepository;
        this.departmentRepository = departmentRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public PaginationResponse<CalendarAssign> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<CalendarAssign> calendarAssignPage = calendarAssignRepository.findAll(pageable);

        List<CalendarAssign> calendarAssigns = calendarAssignPage.getContent();

        PaginationResponse<CalendarAssign> response = new PaginationResponse<>();

        response.setData(calendarAssigns);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All calendar assign fetch successful");

        Links links = Links.fromPage(calendarAssignPage, "/calendar-assigns");
        response.setLinks(links);

        Meta meta = Meta.fromPage(calendarAssignPage, "/calendar-assigns");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<CalendarAssign> getAllCalendarAssigns() {
        return calendarAssignRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public CalendarAssign store(CalenderAssignRequest request) {
        CalendarAssign calendarAssign = new CalendarAssign();

        Calendars calendars = calendarsRepository.findById(request.calenderId()).orElseThrow(() -> new RuntimeException("Calendar not found with id: " + request.calenderId()));
        calendarAssign.setCalendar(calendars);

        Company company = companyRepository.findById(request.companyId()).orElseThrow(() -> new RuntimeException("Company not found with id: " + request.companyId()));
        calendarAssign.setCompany(company);

        BusinessUnit businessUnit = businessUnitRepository.findById(request.businessUnitId()).orElseThrow(() -> new RuntimeException("Business Unit not found with id: " + request.businessUnitId()));
        calendarAssign.setBusinessUnit(businessUnit);

        WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(request.workPlaceGroupId()).orElseThrow(() -> new RuntimeException("Workplace Group not found with id: " + request.workPlaceGroupId()));
        calendarAssign.setWorkplaceGroup(workplaceGroup);

        Workplace workplace = workplaceRepository.findById(request.workPlaceId()).orElseThrow(() -> new RuntimeException("Workplace not found with id: " + request.workPlaceId()));
        calendarAssign.setWorkplace(workplace);

        Department department = departmentRepository.findById(request.departmentId()).orElseThrow(() -> new RuntimeException("Department not found with id: " + request.departmentId()));
        calendarAssign.setDepartment(department);

        Team team = teamRepository.findById(request.teamId()).orElseThrow(() -> new RuntimeException("Team not found with id: " + request.teamId()));
        calendarAssign.setTeam(team);

        calendarAssign.setDescription(request.description());
        calendarAssign.setStatus(request.status());

        calendarAssign = calendarAssignRepository.save(calendarAssign);

        return calendarAssign;
    }

    @Override
    public CalendarAssign show(Long calendarAssignId) {
        CalendarAssign calendarAssign = calendarAssignRepository.findById(calendarAssignId).orElseThrow(() -> new RuntimeException("Calendar Assign not found with id: " + calendarAssignId));
        return calendarAssign;
    }

    @Override
    public CalendarAssign update(Long calendarAssignId, CalenderAssignRequest request) {
        CalendarAssign calendarAssign = calendarAssignRepository.findById(calendarAssignId)
                .orElseThrow(() -> new RuntimeException("Calendar Assign not found with id: " + calendarAssignId));

        Calendars calendars = calendarsRepository.findById(request.calenderId())
                .orElseThrow(() -> new RuntimeException("Calendar not found with id: " + request.calenderId()));
        calendarAssign.setCalendar(calendars != null ? calendars : calendarAssign.getCalendar());

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + request.companyId()));
        calendarAssign.setCompany(company != null ? company : calendarAssign.getCompany());

        BusinessUnit businessUnit = businessUnitRepository.findById(request.businessUnitId())
                .orElseThrow(() -> new RuntimeException("Business Unit not found with id: " + request.businessUnitId()));
        calendarAssign.setBusinessUnit(businessUnit != null ? businessUnit : calendarAssign.getBusinessUnit());

        WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(request.workPlaceGroupId())
                .orElseThrow(() -> new RuntimeException("Workplace Group not found with id: " + request.workPlaceGroupId()));
        calendarAssign.setWorkplaceGroup(workplaceGroup != null ? workplaceGroup : calendarAssign.getWorkplaceGroup());

        Workplace workplace = workplaceRepository.findById(request.workPlaceId())
                .orElseThrow(() -> new RuntimeException("Workplace not found with id: " + request.workPlaceId()));
        calendarAssign.setWorkplace(workplace != null ? workplace : calendarAssign.getWorkplace());

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.departmentId()));
        calendarAssign.setDepartment(department != null ? department : calendarAssign.getDepartment());

        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + request.teamId()));
        calendarAssign.setTeam(team != null ? team : calendarAssign.getTeam());

        calendarAssign.setDescription(request.description() != null ? request.description() : calendarAssign.getDescription());
        calendarAssign.setStatus(request.status() != null ? request.status() : calendarAssign.getStatus());

        calendarAssign = calendarAssignRepository.save(calendarAssign);

        return calendarAssign;
    }

    @Override
    public void destroy(Long calendarAssignId) {
        CalendarAssign calendarAssign = calendarAssignRepository.findById(calendarAssignId).orElseThrow(() -> new RuntimeException("Calendar Assign not found with id: " + calendarAssignId));
        calendarAssignRepository.delete(calendarAssign);
    }
}
