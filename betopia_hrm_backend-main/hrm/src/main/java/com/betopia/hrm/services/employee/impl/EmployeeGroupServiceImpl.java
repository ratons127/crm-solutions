package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeGroup;
import com.betopia.hrm.domain.employee.repository.EmployeeGroupRepository;
import com.betopia.hrm.domain.employee.request.EmployeeGroupRequest;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.exception.workplace.WorkplaceNotFound;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.services.employee.EmployeeGroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeGroupServiceImpl implements EmployeeGroupService {

    private final EmployeeGroupRepository employeeGroupRepository;
    private final CompanyRepository companyRepository;
    private final WorkplaceRepository workplaceRepository;

    public EmployeeGroupServiceImpl(EmployeeGroupRepository employeeGroupRepository,
                                    CompanyRepository companyRepository,
                                    WorkplaceRepository workplaceRepository)
    {
        this.employeeGroupRepository = employeeGroupRepository;
        this.companyRepository = companyRepository;
        this.workplaceRepository = workplaceRepository;
    }

    @Override
    public PaginationResponse<EmployeeGroup> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<EmployeeGroup> employeeGroupPage = employeeGroupRepository.findAll(pageable);
        List<EmployeeGroup> employeeGroups = employeeGroupPage.getContent();
        PaginationResponse<EmployeeGroup> response = new PaginationResponse<>();

        response.setData(employeeGroups);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All employee groups fetch successful");

        Links links = Links.fromPage(employeeGroupPage, "/employee-group");
        response.setLinks(links);
        Meta meta = Meta.fromPage(employeeGroupPage, "/employee-group");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmployeeGroup> getAllEmployeeGroups() {
        return employeeGroupRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public EmployeeGroup show(Long employeeGroupId) {
        EmployeeGroup employeeGroup = employeeGroupRepository.findById(employeeGroupId)
                .orElseThrow(() -> new RuntimeException("Employee Group not found with id: " + employeeGroupId));

        return employeeGroup;
    }

    @Override
    public EmployeeGroup store(EmployeeGroupRequest request) {

        EmployeeGroup employeeGroup = new EmployeeGroup();

        employeeGroup.setName(request.name());
        employeeGroup.setCode(request.code());
        employeeGroup.setDescription(request.description());
        employeeGroup.setStatus(request.status());

        if (request.companyId() != null) {
            employeeGroup.setCompany(
                    companyRepository.findById(request.companyId())
                            .orElseThrow(() -> new CompanyNotFound("Company not found with id: " + request.companyId()))
            );
        } else {
            employeeGroup.setCompany(null);
        }

        if (request.workplaceId() != null) {
            employeeGroup.setWorkplace(
                    workplaceRepository.findById(request.workplaceId())
                            .orElseThrow(() -> new WorkplaceNotFound("Workplace not found with id: " + request.workplaceId()))
            );
        } else {
            employeeGroup.setWorkplace(null);
        }

        return employeeGroupRepository.save(employeeGroup);
    }

    @Override
    public EmployeeGroup update(Long employeeGroupId, EmployeeGroupRequest request) {
        EmployeeGroup employeeGroup = employeeGroupRepository.findById(employeeGroupId)
                .orElseThrow(() -> new RuntimeException("Employee group not found with id: " + employeeGroupId));
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyNotFound("Company not found"));
        Workplace workplace = workplaceRepository.findById(request.workplaceId())
                .orElseThrow(() -> new CompanyNotFound("workplace not found"));

        employeeGroup.setCompany(company != null ? company : employeeGroup.getCompany());
        employeeGroup.setWorkplace(workplace != null ? workplace : employeeGroup.getWorkplace());
        employeeGroup.setName(request.name() != null ? request.name() : employeeGroup.getName());
        employeeGroup.setCode(request.code() != null ? request.code() : employeeGroup.getCode());
        employeeGroup.setDescription(request.description() != null ? request.description() : employeeGroup.getDescription());
        employeeGroup.setStatus(request.status() != null ? request.status() : employeeGroup.getStatus());

        return employeeGroupRepository.save(employeeGroup);
    }

    @Override
    public void destroy(Long employeeGroupId) {
        employeeGroupRepository.deleteById(employeeGroupId);
    }

}
