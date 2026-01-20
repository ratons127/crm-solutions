package com.betopia.hrm.services.employee.employmentstatus.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmploymentStatus;
import com.betopia.hrm.domain.employee.exception.employmentstatus.EmploymentStatusNotFoundException;
import com.betopia.hrm.domain.employee.repository.EmploymentStatusRepository;
import com.betopia.hrm.domain.employee.request.EmploymentStatusRequest;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.services.employee.employmentstatus.EmploymentStatusService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmploymentStatusServiceImpl implements EmploymentStatusService {

    private final EmploymentStatusRepository employmentStatusRepository;
    private final CompanyRepository companyRepository;

    public EmploymentStatusServiceImpl(EmploymentStatusRepository employmentStatusRepository,CompanyRepository companyRepository) {
        this.employmentStatusRepository = employmentStatusRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public PaginationResponse<EmploymentStatus> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<EmploymentStatus> employeeStatusPage = employmentStatusRepository.findAll(pageable);
        List<EmploymentStatus> employeeStatus = employeeStatusPage.getContent();

        PaginationResponse<EmploymentStatus> response = new PaginationResponse<>();
        response.setData(employeeStatus);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Employment statuses fetched successfully");

        Links links = Links.fromPage(employeeStatusPage, "/employment-status");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeeStatusPage, "/employment-status");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmploymentStatus> getAllEmploymentStatuses() {
        return employmentStatusRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public EmploymentStatus getEmploymentStatusById(Long id) {
        return employmentStatusRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFound("Employment status not found with id: " + id));
    }

    @Override
    public EmploymentStatus store(EmploymentStatusRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyNotFound("Company not found with id: " + request.companyId()));

        EmploymentStatus employmentStatus = new EmploymentStatus();
        employmentStatus.setCompany(company);
        employmentStatus.setStatusCode(request.statusCode());
        employmentStatus.setStatusName(request.statusName());
        employmentStatus.setDescription(request.description());
        employmentStatus.setIsSystem(request.isSystem() != null ? request.isSystem() : false);
        employmentStatus.setStatus(request.status() != null ? request.status() : true);

        return employmentStatusRepository.save(employmentStatus);
    }

    @Override
    public EmploymentStatus updateEmploymentStatus(Long id, EmploymentStatusRequest request) {
        EmploymentStatus employmentStatus = employmentStatusRepository.findById(id)
                .orElseThrow(() -> new EmploymentStatusNotFoundException("Employment status not found with id: " + id));

        employmentStatus.setCompany(request.companyId() != null ? companyRepository.findById(request.companyId()).orElse(null) :
                employmentStatus.getCompany());

        employmentStatus.setStatusCode(request.statusCode() != null ? request.statusCode() : employmentStatus.getStatusCode());
        employmentStatus.setStatusName(request.statusName() != null ? request.statusName() : employmentStatus.getStatusName());
        employmentStatus.setDescription(request.description() != null ? request.description() : employmentStatus.getDescription());
        employmentStatus.setIsSystem(request.isSystem() != null ? request.isSystem() : employmentStatus.getIsSystem());
        employmentStatus.setStatus(request.status() != null ? request.status() : employmentStatus.getStatus());

        employmentStatus.setLastModifiedDate(LocalDateTime.now());

        return employmentStatusRepository.save(employmentStatus);
    }

    @Override
    public void deleteEmploymentStatus(Long id) {
        EmploymentStatus employmentStatus = employmentStatusRepository.findById(id)
                .orElseThrow(() -> new EmploymentStatusNotFoundException("Employment status not found with id: " + id));
        employmentStatusRepository.delete(employmentStatus);
    }
}
