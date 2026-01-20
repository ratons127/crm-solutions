package com.betopia.hrm.services.employee.promotionrequests.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.Grade;
import com.betopia.hrm.domain.employee.entity.PromotionRequests;
import com.betopia.hrm.domain.employee.exception.employmentstatus.EmploymentStatusNotFoundException;
import com.betopia.hrm.domain.employee.exception.promotionrequests.PromotionRequestsNotFoundException;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.GradeRepository;
import com.betopia.hrm.domain.employee.repository.PromotionRequestsRepository;
import com.betopia.hrm.domain.employee.request.Promotion;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.promotionrequests.PromotionRequestsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionRequestsServiceImpl implements PromotionRequestsService {

    private final PromotionRequestsRepository promotionRequestsRepository;
    private final EmployeeRepository employeeRepository;
    private final GradeRepository gradeRepository;
    private final DesignationRepository designationRepository;
    private final UserRepository userRepository;

    public PromotionRequestsServiceImpl(PromotionRequestsRepository promotionRequestsRepository,EmployeeRepository employeeRepository,
                                        GradeRepository gradeRepository, DesignationRepository designationRepository,
                                        UserRepository userRepository) {
        this.promotionRequestsRepository = promotionRequestsRepository;
        this.employeeRepository = employeeRepository;
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
        this.designationRepository = designationRepository;
    }

    @Override
    public PaginationResponse<PromotionRequests> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<PromotionRequests> promotionRequestsPage = promotionRequestsRepository.findAll(pageable);
        List<PromotionRequests> promotionRequests = promotionRequestsPage.getContent();

        PaginationResponse<PromotionRequests> response = new PaginationResponse<>();
        response.setData(promotionRequests);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Promotion Requests fetched successfully");

        Links links = Links.fromPage(promotionRequestsPage, "/promotion-request");
        response.setLinks(links);

        Meta meta = Meta.fromPage(promotionRequestsPage, "/promotion-request");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<PromotionRequests> getAll() {
        return promotionRequestsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public PromotionRequests store(Promotion request) {
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.employeeId()));
        Grade grade = gradeRepository.findById(request.newGradeId())
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + request.newGradeId()));
        Designation designation = designationRepository.findById(request.currentDesignationId())
                .orElseThrow(() -> new RuntimeException("Designation not found with id: " + request.currentDesignationId()));
        User users = userRepository.findById(request.approvedById())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.approvedById()));


        PromotionRequests promotion = new PromotionRequests();
        promotion.setEmployee(employee);
        promotion.setCurrentGrade(grade);
        promotion.setNewGrade(grade);
        promotion.setCurrentDesignation(designation);
        promotion.setNewDesignation(designation);
        promotion.setAppraisal(request.appraisalId());
        promotion.setJustification(request.justification());
        promotion.setPromotionType(request.promotionType());
        promotion.setEffectiveDate(request.effectiveDate());
        promotion.setSalaryChange(request.salaryChange());
        promotion.setApprovalStatus(request.approvalStatus());
        promotion.setStatus(request.status());

        return promotionRequestsRepository.save(promotion);
    }

    @Override
    public PromotionRequests show(Long id) {
        return promotionRequestsRepository.findById(id)
                .orElseThrow(() -> new PromotionRequestsNotFoundException("Transfer Requests not found with id: " + id));
    }

    @Override
    public PromotionRequests update(Long id, Promotion request) {
        PromotionRequests promotionRequests = promotionRequestsRepository.findById(id)
                .orElseThrow(() -> new EmploymentStatusNotFoundException("Promotion Requests not found with id: " + id));

        promotionRequests.setEmployee(request.employeeId() != null ? employeeRepository.findById(request.employeeId()).orElse(null) :
                promotionRequests.getEmployee());

        promotionRequests.setCurrentDesignation(request.currentDesignationId()!= null ? designationRepository.
                findById(request.currentDesignationId()).orElse(null) : promotionRequests.getCurrentDesignation());

        promotionRequests.setNewDesignation(request.newDesignationId()!= null ? designationRepository.findById(request.newDesignationId()).orElse
                (null) : promotionRequests.getNewDesignation());

        promotionRequests.setPromotionType(request.promotionType() != null ? request.promotionType() : promotionRequests.getPromotionType());
        promotionRequests.setEffectiveDate(request.effectiveDate() != null ? request.effectiveDate() : promotionRequests.getEffectiveDate());
        promotionRequests.setSalaryChange(request.salaryChange() != null ? request.salaryChange() : promotionRequests.getSalaryChange());
        promotionRequests.setApprovalStatus(request.approvalStatus() != null ? request.approvalStatus() : promotionRequests.getApprovalStatus());
        promotionRequests.setStatus(request.status() != null ? request.status() : promotionRequests.getStatus());
        promotionRequests.setLastModifiedDate(LocalDateTime.now());

        return promotionRequestsRepository.save(promotionRequests);
    }

    @Override
    public void destroy(Long id) {
        PromotionRequests promotionRequests = promotionRequestsRepository.findById(id)
                .orElseThrow(() -> new PromotionRequestsNotFoundException("Transfer Requests not found with id: " + id));
        promotionRequestsRepository.delete(promotionRequests);
    }
}
