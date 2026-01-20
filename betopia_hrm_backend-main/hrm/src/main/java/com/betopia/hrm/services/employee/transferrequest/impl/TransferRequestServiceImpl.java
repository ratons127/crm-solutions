package com.betopia.hrm.services.employee.transferrequest.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.exception.DepartmentNotFoundException;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.TransferRequests;
import com.betopia.hrm.domain.employee.exception.employmentstatus.EmploymentStatusNotFoundException;
import com.betopia.hrm.domain.employee.exception.transferrequest.TransferRequestNotFoundException;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.TransferRequestsRepository;
import com.betopia.hrm.domain.employee.request.Transfer;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.exception.workplace.WorkplaceNotFound;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.services.employee.transferrequest.TransferRequestService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferRequestServiceImpl implements TransferRequestService {

    private final TransferRequestsRepository transferRequestsRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkplaceRepository workplaceRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final UserRepository userRepository;

    public TransferRequestServiceImpl(TransferRequestsRepository transferRequestsRepository, CompanyRepository companyRepository,
                                      EmployeeRepository employeeRepository, WorkplaceRepository workplaceRepository,
                                      DepartmentRepository departmentRepository, DesignationRepository designationRepository,
                                      UserRepository userRepository) {
        this.transferRequestsRepository = transferRequestsRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.workplaceRepository = workplaceRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaginationResponse<TransferRequests> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<TransferRequests> transferRequestsPage = transferRequestsRepository.findAll(pageable);
        List<TransferRequests> transferRequests = transferRequestsPage.getContent();

        PaginationResponse<TransferRequests> response = new PaginationResponse<>();
        response.setData(transferRequests);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Transfer Requests fetched successfully");

        Links links = Links.fromPage(transferRequestsPage, "/transfer-request");
        response.setLinks(links);

        Meta meta = Meta.fromPage(transferRequestsPage, "/transfer-request");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<TransferRequests> getAll() {
        return transferRequestsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    @Override
    public TransferRequests store(Transfer request) {
        Company company = companyRepository.findById(request.fromCompanyId())
                .orElseThrow(() -> new CompanyNotFound("Company not found with id: " + request.fromCompanyId()));

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.employeeId()));

        Workplace workplace = workplaceRepository.findById(request.fromWorkplaceId())
                .orElseThrow(() -> new WorkplaceNotFound("Workplace not found with id: " + request.fromWorkplaceId()));

        Department department = departmentRepository.findById(request.fromDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.fromDepartmentId()));

        Designation designation = designationRepository.findById(request.fromDesignationId())
                .orElseThrow(() -> new RuntimeException("Designation not found with id: " + request.fromDesignationId()));

        User users = userRepository.findById(request.approvedById())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.approvedById()));

        TransferRequests transfer = new TransferRequests();
        transfer.setEmployee(employee);
        transfer.setRequestType(request.requestType());
        transfer.setFromCompanyId(company);
        transfer.setToCompanyId(company);
        transfer.setFromWorkplaceId(workplace);
        transfer.setToWorkplaceId(workplace);
        transfer.setFromDepartmentId(department);
        transfer.setToDepartmentId(department);
        transfer.setFromDesignationId(designation);
        transfer.setToDesignationId(designation);
        transfer.setReason(request.reason());
        transfer.setEffectiveDate(request.effectiveDate());
        transfer.setApprovalStatus(request.approvalStatus());
        transfer.setApprovedBy(users);
        transfer.setStatus(request.status());
        return transferRequestsRepository.save(transfer);
    }

    @Override
    public TransferRequests show(Long id) {
        return transferRequestsRepository.findById(id)
                .orElseThrow(() -> new TransferRequestNotFoundException("Transfer Requests not found with id: " + id));
    }

    @Override
    public TransferRequests update(Long id, Transfer request) {
        TransferRequests transfer = transferRequestsRepository.findById(id)
                .orElseThrow(() -> new EmploymentStatusNotFoundException("Transfer Requests not found with id: " + id));

        transfer.setFromCompanyId(request.fromCompanyId() != null ? companyRepository.findById(request.fromCompanyId()).orElse(null) :
                transfer.getFromCompanyId());
        transfer.setToCompanyId(request.toCompanyId() != null ? companyRepository.findById(request.toCompanyId()).orElse(null) :
                transfer.getToCompanyId());
        transfer.setEmployee(request.employeeId() != null ? employeeRepository.findById(request.employeeId()).orElse(null) :
                transfer.getEmployee());
        transfer.setFromWorkplaceId(request.fromWorkplaceId() != null ? workplaceRepository.findById(request.fromWorkplaceId()).orElse
                (null) : transfer.getFromWorkplaceId());
        transfer.setToWorkplaceId(request.toWorkplaceId() != null ? workplaceRepository.findById(request.toWorkplaceId()).orElse(null) :
                transfer.getToWorkplaceId());
        transfer.setFromDepartmentId(request.fromDepartmentId() != null ? departmentRepository.findById(request.fromDepartmentId()).orElse
                (null) : transfer.getFromDepartmentId());
        transfer.setToDepartmentId(request.toDepartmentId()!= null ? departmentRepository.findById(request.toDepartmentId()).orElse
                (null) : transfer.getToDepartmentId());
        transfer.setFromDesignationId(request.fromDesignationId()!= null ? designationRepository.findById(request.fromDesignationId()).orElse
                (null) : transfer.getFromDesignationId());
        transfer.setToDesignationId(request.toDesignationId()!= null ? designationRepository.findById(request.toDesignationId()).orElse
                (null) : transfer.getToDesignationId());

        transfer.setApprovedBy(request.approvedById()!= null ? userRepository.findById(request.approvedById()).orElse
                (null) : transfer.getApprovedBy());

        transfer.setReason(request.reason() != null ? request.reason() : transfer.getReason());
        transfer.setEffectiveDate(request.effectiveDate() != null ? request.effectiveDate() : transfer.getEffectiveDate());
        transfer.setApprovalStatus(request.approvalStatus() != null ? request.approvalStatus() : transfer.getApprovalStatus());
        transfer.setStatus(request.status() != null ? request.status() : transfer.getStatus());
        transfer.setLastModifiedDate(LocalDateTime.now());

        return transferRequestsRepository.save(transfer);
    }

    @Override
    public void destroy(Long id) {
        TransferRequests transfer = transferRequestsRepository.findById(id)
                .orElseThrow(() -> new TransferRequestNotFoundException("Transfer Requests not found with id: " + id));
        transferRequestsRepository.delete(transfer);
    }
}
