package com.betopia.hrm.services.cashadvance.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfig;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfigDetails;
import com.betopia.hrm.domain.cashadvance.enums.CashAdvanceStatus;
import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashRequest;
import com.betopia.hrm.domain.cashadvance.enums.CashCalculationStatus;
import com.betopia.hrm.domain.cashadvance.enums.ServiceChargeType;
import com.betopia.hrm.domain.cashadvance.exception.AdvanceCashRequestNotFoundException;
import com.betopia.hrm.domain.cashadvance.exception.CashAdvanceSlabConfigNotFoundException;
import com.betopia.hrm.domain.cashadvance.mapper.AdvanceCashRequestMapper;
import com.betopia.hrm.domain.cashadvance.model.AdvanceRequestDTO;
import com.betopia.hrm.domain.cashadvance.repository.AdvanceCashRequestRepository;
import com.betopia.hrm.domain.cashadvance.repository.CashAdvanceSlabConfigRepository;
import com.betopia.hrm.domain.cashadvance.request.AdvanceCashRequestRequest;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.exception.DepartmentNotFoundException;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.dto.cashadvance.AdvanceCashRequestDTO;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceSlabConfigDTO;
import com.betopia.hrm.domain.dto.cashadvance.mapper.AdvanceCashRequestMappers;
import com.betopia.hrm.domain.dto.cashadvance.mapper.CashAdvanceSlabConfigMapper;
import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.dto.employee.mapper.EmployeeDTOMapper;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.mapper.EmployeeMapper;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeTypeRepository;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.services.cashadvance.AdvanceCashRequestService;
import com.betopia.hrm.services.cashadvance.CashAdvanceApprovalService;
import com.betopia.hrm.webapp.util.AuthUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AdvanceCashRequestServiceImpl implements AdvanceCashRequestService {

    private final AdvanceCashRequestRepository advanceCashRequestRepository;
    private final AdvanceCashRequestMapper advanceCashRequestMapper;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final AdvanceCashRequestMappers advanceCashRequestMappers;
    private final CashAdvanceSlabConfigRepository cashAdvanceSlabConfigRepository;
    private final EmployeeDTOMapper employeeDTOMapper;
    private final CashAdvanceSlabConfigMapper cashAdvanceSlabConfigMapper;
    private final CashAdvanceApprovalService cashAdvanceApprovalService;

    public AdvanceCashRequestServiceImpl(AdvanceCashRequestRepository advanceCashRequestRepository, AdvanceCashRequestMapper advanceCashRequestMapper, EmployeeTypeRepository employeeTypeRepository, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, DesignationRepository designationRepository, AdvanceCashRequestMappers advanceCashRequestMappers, CashAdvanceSlabConfigRepository cashAdvanceSlabConfigRepository, EmployeeMapper employeeMapper, EmployeeDTOMapper employeeDTOMapper, CashAdvanceSlabConfigMapper cashAdvanceSlabConfigMapper, CashAdvanceApprovalService cashAdvanceApprovalService) {
        this.advanceCashRequestRepository = advanceCashRequestRepository;
        this.advanceCashRequestMapper = advanceCashRequestMapper;
        this.employeeTypeRepository = employeeTypeRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
        this.advanceCashRequestMappers = advanceCashRequestMappers;
        this.cashAdvanceSlabConfigRepository = cashAdvanceSlabConfigRepository;
        this.employeeDTOMapper = employeeDTOMapper;
        this.cashAdvanceSlabConfigMapper = cashAdvanceSlabConfigMapper;
        this.cashAdvanceApprovalService = cashAdvanceApprovalService;
    }

    @Override
    public PaginationResponse<AdvanceCashRequestDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<AdvanceCashRequest> advanceCashRequestPage = advanceCashRequestRepository.findAll(pageable);

        List<AdvanceCashRequest> advanceCashRequests = advanceCashRequestPage.getContent();
        List<AdvanceCashRequestDTO> advanceCashRequestDTOs = advanceCashRequestMappers.toDTOList(advanceCashRequests);

        PaginationResponse<AdvanceCashRequestDTO> response = new PaginationResponse<>();

        response.setData(advanceCashRequestDTOs);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All AdvanceCashRequest fetch successful");

        Links links = Links.fromPage(advanceCashRequestPage, "/advanceCashRequests");
        response.setLinks(links);

        Meta meta = Meta.fromPage(advanceCashRequestPage, "/advanceCashRequests");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<AdvanceCashRequestDTO> getAllAdvanceCashRequest() {
        return advanceCashRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(advanceCashRequestMappers::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public AdvanceCashRequestDTO store(AdvanceCashRequestRequest request) {
        CashAdvanceSlabConfig cashAdvanceSlabConfigs=null;
        Employee employee=null;
        LocalDate requestDate = request.requestDate().atZone(ZoneId.systemDefault()).toLocalDate();

        if(AuthUtils.getCurrentUsername().isEmpty())
                throw new UsernameNotFoundException("Username not found");

            employee=employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                    .stream().filter(e->e.getEmail().equals(AuthUtils.getCurrentUsername())).findFirst()
                    .orElseThrow(()->new EmployeeNotFound("Employee not found"));

            EmployeeDTO employeeDTO=employeeDTOMapper.toDTO(employee);

            cashAdvanceSlabConfigs = searchCashAdvanceSlabConfig(employeeDTO.getWorkplace() == null? null:employeeDTO.getWorkplace().getId(),
                    employeeDTO.getWorkplaceGroup() == null? null:employeeDTO.getWorkplaceGroup().getId(),
                    employeeDTO.getCompany() == null? null:employeeDTO.getCompany().getId(),
                    employeeDTO.getEmployeeTypeId(), requestDate,true);

            if(cashAdvanceSlabConfigs.getCashAdvanceSlabConfigDetails().isEmpty())
                throw new CashAdvanceSlabConfigNotFoundException("No cash advance slab config found against employee:"+ employee.getEmployeeSerialId());


            if(LocalDate.now().getDayOfMonth()>cashAdvanceSlabConfigs.getAdvanceRequestDay())
                throw new CashAdvanceSlabConfigNotFoundException("Not applicable because advance request day is configured:"+ cashAdvanceSlabConfigs.getAdvanceRequestDay());

        AdvanceCashRequest advanceCashRequest =advanceCashRequestMapper.toEntity(request);
        advanceCashRequest.setStatus(CashAdvanceStatus.PENDING.name());
        advanceCashRequest.setRequestId(advanceCashRequestRepository.nextCashAdvanceRequestSerial());
        advanceCashRequest.setEmployeeId(employee.getEmployeeSerialId());
        advanceCashRequest.setEmployeeTypeId(employee.getEmployeeType().getId());
        advanceCashRequest.setDepartmentId(employee.getDepartment().getId());
        advanceCashRequest.setDesignationId(employee.getDesignation().getId());
        advanceCashRequest.setEmployeeName(employee.getFirstName());

        AdvanceCashRequest savedAdvanceCashRequest = advanceCashRequestRepository.save(advanceCashRequest);
        cashAdvanceApprovalService.createInitialApproval(savedAdvanceCashRequest);
        return advanceCashRequestMappers.toDTO(savedAdvanceCashRequest);
    }

    @Override
    public AdvanceCashRequestDTO show(Long advanceCashRequestId) {
        AdvanceCashRequest advanceCashRequest = advanceCashRequestRepository.findById(advanceCashRequestId)
                .orElseThrow(() -> new AdvanceCashRequestNotFoundException("AdvanceCashRequest not found with id: " + advanceCashRequestId));

        return advanceCashRequestMappers.toDTO(advanceCashRequest);
    }

    @Override
    public AdvanceCashRequestDTO update(Long advanceCashRequestId, AdvanceCashRequestRequest request) {
        AdvanceCashRequest advanceCashRequest = advanceCashRequestRepository.findById(advanceCashRequestId)
                .orElseThrow(() -> new AdvanceCashRequestNotFoundException("AdvanceCashRequest not found with id: " + advanceCashRequestId));

        advanceCashRequest.setRequestDate(request.requestDate());
        advanceCashRequest.setRequestedAmount(request.requestedAmount());
        advanceCashRequest.setServiceChargeAmount(request.serviceChargeAmount());
        advanceCashRequest.setReason(request.reason());
        advanceCashRequestRepository.save(advanceCashRequest);
        return advanceCashRequestMappers.toDTO(advanceCashRequestRepository.save(advanceCashRequest));
    }

    @Override
    public void delete(Long advanceCashRequestId) {

        AdvanceCashRequest advanceCashRequest = advanceCashRequestRepository.findById(advanceCashRequestId)
                .orElseThrow(() -> new AdvanceCashRequestNotFoundException("AdvanceCashRequest not found with id: " + advanceCashRequestId));

        advanceCashRequestRepository.deleteById(advanceCashRequestId);
    }

    @Override
    public AdvanceRequestDTO getAdvanceCashRequestByEmployee(BigDecimal requestedAmount) {
        CashAdvanceSlabConfig cashAdvanceSlabConfigs=null;
        CashAdvanceSlabConfigDTO cashAdvanceSlabConfigDTO=null;
        AdvanceRequestDTO advanceRequestDTO=new AdvanceRequestDTO();
        Employee employee=null;
        BigDecimal serviceCharge=BigDecimal.ZERO;
        BigDecimal calculatedServiceChargeAmount=BigDecimal.ZERO;
        LocalDate requestDate = LocalDate.now();

        if(!AuthUtils.getCurrentUsername().isEmpty()){
            employee=employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                    .stream().filter(e->e.getEmail().equals(AuthUtils.getCurrentUsername())).findFirst()
                    .orElseThrow(()->new EmployeeNotFound("Employee not found"));
            EmployeeDTO employeeDTO=employeeDTOMapper.toDTO(employee);

            cashAdvanceSlabConfigs = searchCashAdvanceSlabConfig(employeeDTO.getWorkplace() == null? null:employeeDTO.getWorkplace().getId(),
                    employeeDTO.getWorkplaceGroup() == null? null:employeeDTO.getWorkplaceGroup().getId(),
                    employeeDTO.getCompany() == null? null:employeeDTO.getCompany().getId(),
                    employeeDTO.getEmployeeTypeId(), requestDate,true);

            if(cashAdvanceSlabConfigs.getCashAdvanceSlabConfigDetails().isEmpty())
                   throw new CashAdvanceSlabConfigNotFoundException("No cash advance slab config found against employee:"+ employee.getEmployeeSerialId());

            else{
                if(ServiceChargeType.RANGE.name().equals(cashAdvanceSlabConfigs.getServiceChargeType())){
                    CashAdvanceSlabConfigDetails cashAdvanceSlabConfigDetails = cashAdvanceSlabConfigs.getCashAdvanceSlabConfigDetails().stream()
                                    .filter(p -> requestedAmount.compareTo(p.getFromAmount()) >= 0 && requestedAmount.compareTo(p.getToAmount()) <= 0)
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("No matching slab found for requested amount: " + requestedAmount));

                    if(ServiceChargeType.FIXED.name().equals(cashAdvanceSlabConfigDetails.getServiceChargeType())) {
                        calculatedServiceChargeAmount = new BigDecimal(String.valueOf(cashAdvanceSlabConfigDetails.getServiceChargeAmount()));
                        advanceRequestDTO.setServiceCharge(calculatedServiceChargeAmount);
                        double convertedAdvanceAmt = cashAdvanceSlabConfigs.getAdvancePercent().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double convertedGrossAmt = employee.getGrossSalary().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double consolidatedAdvanceAmt = convertedGrossAmt*convertedAdvanceAmt/100;
                        advanceRequestDTO.setApplicableaAdvanceAmount(new BigDecimal(String.valueOf(consolidatedAdvanceAmt)));
                        advanceRequestDTO.setDeductedAmount(new BigDecimal(String.valueOf(consolidatedAdvanceAmt)).add(calculatedServiceChargeAmount));
                        advanceRequestDTO.setRequestedAmount(requestedAmount);
                    }
                    else{
                        double convertedRequestedAmt= requestedAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double convertedServiceChargeAmt = cashAdvanceSlabConfigDetails.getServiceChargeAmount().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double consolidatedServiceChargeAmt = convertedRequestedAmt*convertedServiceChargeAmt/100;
                        calculatedServiceChargeAmount = new BigDecimal(String.valueOf(consolidatedServiceChargeAmt));
                        advanceRequestDTO.setServiceCharge(calculatedServiceChargeAmount);

                        double convertedAdvanceAmt = cashAdvanceSlabConfigs.getAdvancePercent().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double convertedGrossAmt = employee.getGrossSalary().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double consolidatedAdvanceAmt = convertedGrossAmt*convertedAdvanceAmt/100;
                        advanceRequestDTO.setApplicableaAdvanceAmount(new BigDecimal(String.valueOf(consolidatedAdvanceAmt)));
                        advanceRequestDTO.setDeductedAmount(new BigDecimal(String.valueOf(consolidatedAdvanceAmt)).add(calculatedServiceChargeAmount));
                        advanceRequestDTO.setRequestedAmount(requestedAmount);
                    }
                }
                else{
                    if(cashAdvanceSlabConfigs.getServiceChargeType().equals(ServiceChargeType.PERCENTAGE.name())){
                        double convertedAdvanceAmt = cashAdvanceSlabConfigs.getAdvancePercent().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double convertedGrossAmt = employee.getGrossSalary().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double consolidatedAdvanceAmt = convertedGrossAmt*convertedAdvanceAmt/100;
                        advanceRequestDTO.setApplicableaAdvanceAmount(new BigDecimal(String.valueOf(consolidatedAdvanceAmt)));
                        double convertedServiceChargeAmt = cashAdvanceSlabConfigs.getServiceChargeAmount().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double consolidatedServiceChargeAmt = requestedAmount.setScale(2, RoundingMode.HALF_UP).doubleValue()*convertedServiceChargeAmt/100;
                        advanceRequestDTO.setServiceCharge(new BigDecimal(String.valueOf(consolidatedServiceChargeAmt)));
                        advanceRequestDTO.setDeductedAmount(new BigDecimal(String.valueOf(consolidatedAdvanceAmt)).add(new BigDecimal(String.valueOf(consolidatedServiceChargeAmt))));
                        advanceRequestDTO.setRequestedAmount(requestedAmount);
                    }
                    else
                    {
                        double convertedAdvanceAmt = cashAdvanceSlabConfigs.getAdvancePercent().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double convertedGrossAmt = employee.getGrossSalary().setScale(2, RoundingMode.HALF_UP).doubleValue();
                        double consolidatedAdvanceAmt = convertedGrossAmt*convertedAdvanceAmt/100;
                        advanceRequestDTO.setApplicableaAdvanceAmount(new BigDecimal(String.valueOf(consolidatedAdvanceAmt)));
                        serviceCharge=cashAdvanceSlabConfigs.getServiceChargeAmount();
                        advanceRequestDTO.setServiceCharge(serviceCharge);
                        advanceRequestDTO.setDeductedAmount(new BigDecimal(String.valueOf(consolidatedAdvanceAmt)).add(serviceCharge));
                        advanceRequestDTO.setRequestedAmount(requestedAmount);
                    }

                }

            }

        }
        return advanceRequestDTO;
    }

    private CashAdvanceSlabConfig searchCashAdvanceSlabConfig(Long workplaceId, Long workplaceGroupId, Long companyId, Long employeeTypeId,
                                                                              LocalDate requestDate,Boolean status
    ) {
        Specification<CashAdvanceSlabConfig> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (workplaceId != null)
                predicates.add(cb.equal(root.get("workplace").get("id"), workplaceId));

            if (workplaceGroupId != null)
                predicates.add(cb.equal(root.get("workplaceGroup").get("id"), workplaceGroupId));

            if (companyId != null)
                predicates.add(cb.equal(root.get("company").get("id"), companyId));


            if (employeeTypeId != null)
                predicates.add(cb.equal(root.get("employeeType").get("id"), employeeTypeId));


            if (status != null)
                predicates.add(cb.equal(root.get("status"), status));

            if (requestDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("effectiveFromDate"), requestDate));
                predicates.add(cb.greaterThanOrEqualTo(root.get("effectiveToDate"), requestDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<CashAdvanceSlabConfig> cashAdvanceSlabConfigs = cashAdvanceSlabConfigRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "effectiveFromDate"));
        return cashAdvanceSlabConfigs.stream().findFirst().orElseThrow(()-> new CashAdvanceSlabConfigNotFoundException("CashAdvanceSlabConfig not found against date"+requestDate));
    }


}
