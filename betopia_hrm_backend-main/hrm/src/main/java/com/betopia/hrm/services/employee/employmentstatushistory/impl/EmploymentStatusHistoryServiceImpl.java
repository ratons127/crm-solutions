package com.betopia.hrm.services.employee.employmentstatushistory.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmploymentStatus;
import com.betopia.hrm.domain.employee.entity.EmploymentStatusHistory;
import com.betopia.hrm.domain.employee.exception.employmentstatus.EmploymentStatusNotFoundException;
import com.betopia.hrm.domain.employee.exception.employmentstatushistory.EmploymentStatusHistoryNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.EmploymentStatusHistoryRepository;
import com.betopia.hrm.domain.employee.repository.EmploymentStatusRepository;
import com.betopia.hrm.domain.employee.request.EmploymentStatusHistoryRequest;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.services.employee.employmentstatushistory.EmploymentStatusHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmploymentStatusHistoryServiceImpl implements EmploymentStatusHistoryService {

    private final EmploymentStatusHistoryRepository employmentStatusHistoryRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final EmploymentStatusRepository employmentStatusRepository ;

    public EmploymentStatusHistoryServiceImpl(EmploymentStatusHistoryRepository employmentStatusHistoryRepository, CompanyRepository companyRepository,
                                              EmployeeRepository employeeRepository, EmploymentStatusRepository employmentStatusRepository)
            {
        this.employmentStatusHistoryRepository = employmentStatusHistoryRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.employmentStatusRepository = employmentStatusRepository;
    }

    @Override
    public PaginationResponse<EmploymentStatusHistory> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<EmploymentStatusHistory> employeeStatusHistoryPage = employmentStatusHistoryRepository.findAll(pageable);
        List<EmploymentStatusHistory> employeeStatusHistory = employeeStatusHistoryPage.getContent();

        PaginationResponse<EmploymentStatusHistory> response = new PaginationResponse<>();
        response.setData(employeeStatusHistory);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Employment status history fetched successfully");

        Links links = Links.fromPage(employeeStatusHistoryPage, "/leave-types");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeeStatusHistoryPage, "/leave-types");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmploymentStatusHistory> getAllEmploymentStatusHistories() {
        return employmentStatusHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public EmploymentStatusHistory getEmploymentStatusHistoryById(Long id) {
        return employmentStatusHistoryRepository.findById(id)
                .orElseThrow(() -> new EmploymentStatusHistoryNotFound("Employment status history not found with id: " + id));
    }

    @Override
    public EmploymentStatusHistory store(EmploymentStatusHistoryRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyNotFound("Company not found with id: " + request.companyId()));

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.employeeId()));

        EmploymentStatus employmentStatus = employmentStatusRepository.findById(request.statusId())
                .orElseThrow(() -> new EmploymentStatusNotFoundException("Employment status history not found with id: " + request.statusId()));

        EmploymentStatusHistory employmentStatusHistory = new EmploymentStatusHistory();
        employmentStatusHistory.setCompany(company);
        employmentStatusHistory.setEmployee(employee);
        employmentStatusHistory.setStatus(employmentStatus);
        employmentStatusHistory.setEffectiveFrom(request.effectiveFrom());
        employmentStatusHistory.setEffectiveTo(request.effectiveTo());
        employmentStatusHistory.setReasonCode(request.reasonCode());
        employmentStatusHistory.setRemarks(request.remarks());

        return employmentStatusHistoryRepository.save(employmentStatusHistory);
    }

    @Override
    public EmploymentStatusHistory updateEmploymentStatusHistory(Long id, EmploymentStatusHistoryRequest request) {
        EmploymentStatusHistory employmentStatusHistory = employmentStatusHistoryRepository.findById(id)
                .orElseThrow(() -> new EmploymentStatusHistoryNotFound("Employment status history not found with id: " + id));

        employmentStatusHistory.setCompany(request.companyId() != null ? companyRepository.findById(request.companyId()).orElse(null) :
                employmentStatusHistory.getCompany());

        employmentStatusHistory.setEmployee(request.employeeId() != null ? employeeRepository.findById(request.employeeId()).orElse(null) :
                employmentStatusHistory.getEmployee());

        employmentStatusHistory.setStatus(request.statusId() != null ? employmentStatusRepository.findById(request.statusId()).orElse(null) :
                employmentStatusHistory.getStatus());

        employmentStatusHistory.setEffectiveFrom(request.effectiveFrom() != null ? request.effectiveFrom() : employmentStatusHistory.getEffectiveFrom());
        employmentStatusHistory.setEffectiveTo(request.effectiveTo() != null ? request.effectiveTo() : employmentStatusHistory.getEffectiveTo());
        employmentStatusHistory.setReasonCode(request.reasonCode() != null ? request.reasonCode() : employmentStatusHistory.getReasonCode());
        employmentStatusHistory.setRemarks(request.remarks() != null ? request.remarks() : employmentStatusHistory.getRemarks());
        employmentStatusHistory.setLastModifiedDate(LocalDateTime.now());

        return employmentStatusHistoryRepository.save(employmentStatusHistory);
    }

    @Override
    public void deleteEmploymentStatusHistory(Long id) {
        EmploymentStatusHistory employmentStatusHistory = employmentStatusHistoryRepository.findById(id)
                .orElseThrow(() -> new EmploymentStatusHistoryNotFound("Employment status history not found with id: " + id));
        employmentStatusHistoryRepository.delete(employmentStatusHistory);
    }
}
