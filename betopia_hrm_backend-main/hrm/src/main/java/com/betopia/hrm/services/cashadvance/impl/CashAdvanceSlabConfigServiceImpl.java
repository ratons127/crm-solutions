package com.betopia.hrm.services.cashadvance.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfig;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfigDetails;
import com.betopia.hrm.domain.cashadvance.exception.CashAdvanceSlabConfigNotFoundException;
import com.betopia.hrm.domain.cashadvance.repository.CashAdvanceSlabConfigRepository;
import com.betopia.hrm.domain.cashadvance.request.CashAdvanceSlabConfigRequest;
import com.betopia.hrm.domain.cashadvance.request.UpdateCashAdvanceSlabConfigDetailRequest;
import com.betopia.hrm.domain.cashadvance.request.UpdateCashAdvanceSlabConfigRequest;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.company.exception.BusinessUnitNotFound;
import com.betopia.hrm.domain.company.exception.WorkplaceGroupNotFound;
import com.betopia.hrm.domain.company.repository.BusinessUnitRepository;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.company.repository.TeamRepository;
import com.betopia.hrm.domain.company.repository.WorkplaceGroupRepository;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceSlabConfigDTO;
import com.betopia.hrm.domain.dto.cashadvance.mapper.CashAdvanceSlabConfigMapper;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeTypeRepository;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.exception.workplace.WorkplaceNotFound;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.services.cashadvance.CashAdvanceSlabConfigService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CashAdvanceSlabConfigServiceImpl implements CashAdvanceSlabConfigService {

    private final CashAdvanceSlabConfigRepository cashAdvanceSlabConfigRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final DesignationRepository designationRepository;
    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;
    private final BusinessUnitRepository businessUnitRepository;
    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceGroupRepository workplaceGroupRepository;
    private final TeamRepository teamRepository;
    private final CashAdvanceSlabConfigMapper  cashAdvanceSlabConfigMapper;

    public CashAdvanceSlabConfigServiceImpl(CashAdvanceSlabConfigRepository cashAdvanceSlabConfigRepository, EmployeeTypeRepository employeeTypeRepository, DesignationRepository designationRepository, DepartmentRepository departmentRepository, CompanyRepository companyRepository, BusinessUnitRepository businessUnitRepository, WorkplaceRepository workplaceRepository, WorkplaceGroupRepository workplaceGroupRepository, TeamRepository teamRepository, CashAdvanceSlabConfigMapper cashAdvanceSlabConfigMapper) {
        this.cashAdvanceSlabConfigRepository = cashAdvanceSlabConfigRepository;
        this.employeeTypeRepository = employeeTypeRepository;
        this.designationRepository = designationRepository;
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.workplaceRepository = workplaceRepository;
        this.workplaceGroupRepository = workplaceGroupRepository;
        this.teamRepository = teamRepository;
        this.cashAdvanceSlabConfigMapper = cashAdvanceSlabConfigMapper;
    }

    @Override
    public PaginationResponse<CashAdvanceSlabConfigDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<CashAdvanceSlabConfig> cashAdvanceSlabConfigPage = cashAdvanceSlabConfigRepository.findAll(pageable);


        List<CashAdvanceSlabConfig> cashAdvanceSlabConfigs = cashAdvanceSlabConfigPage.getContent();

        List<CashAdvanceSlabConfigDTO> cashAdvanceSlabConfigDTOs = cashAdvanceSlabConfigMapper.toDTOList(cashAdvanceSlabConfigs);

        PaginationResponse<CashAdvanceSlabConfigDTO> response = new PaginationResponse<>();

        response.setData(cashAdvanceSlabConfigDTOs);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All CashAdvanceSlabConfig fetch successful");

        Links links = Links.fromPage(cashAdvanceSlabConfigPage, "/cashAdvanceSlabConfigs");
        response.setLinks(links);

        Meta meta = Meta.fromPage(cashAdvanceSlabConfigPage, "/cashAdvanceSlabConfigs");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<CashAdvanceSlabConfigDTO> getAllCashAdvanceSlabConfig() {
        return cashAdvanceSlabConfigRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(cashAdvanceSlabConfigMapper::toDTO)
                .toList();
    }

    @Override
    public CashAdvanceSlabConfigDTO store(CashAdvanceSlabConfigRequest request) {

        CashAdvanceSlabConfig cashAdvanceSlabConfig = new CashAdvanceSlabConfig();
        if(request.employeeTypeId()!=null) {
            EmployeeType employeeType = employeeTypeRepository.findById(request.employeeTypeId())
                    .orElseThrow(() -> new EmployeeNotFound("EmployeeTypes not found: " + request.employeeTypeId()));
            cashAdvanceSlabConfig.setEmployeeType(employeeType);
        }
        else
            cashAdvanceSlabConfig.setEmployeeType(null);

        if(request.companyId() != null) {
            Company company = companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new CompanyNotFound("Company not found: " + request.companyId()));
            cashAdvanceSlabConfig.setCompany(company);
        }
        else
            cashAdvanceSlabConfig.setCompany(null);

        if(request.businessUnitId() != null) {
            BusinessUnit businessUnit = businessUnitRepository.findById(request.businessUnitId())
                    .orElseThrow(() -> new BusinessUnitNotFound("BusinessUnit not found: " + request.businessUnitId()));
            cashAdvanceSlabConfig.setBusinessUnit(businessUnit);
        }
        else
            cashAdvanceSlabConfig.setBusinessUnit(null);

        if(request.workplaceGroupId() != null) {
            WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(request.workplaceGroupId())
                    .orElseThrow(() -> new WorkplaceGroupNotFound("WorkplaceGroup not found: " + request.workplaceGroupId()));
            cashAdvanceSlabConfig.setWorkplaceGroup(workplaceGroup);
        }
        else
            cashAdvanceSlabConfig.setWorkplaceGroup(null);

        if(request.workplaceId() != null) {
            Workplace workplace = workplaceRepository.findById(request.workplaceId())
                    .orElseThrow(() -> new WorkplaceNotFound("Workplace not found: " + request.workplaceId()));
            cashAdvanceSlabConfig.setWorkplace(workplace);
        }
        else
            cashAdvanceSlabConfig.setWorkplace(null);

        cashAdvanceSlabConfig.setApprovedAmountChange(request.isApprovedAmountChange());
        cashAdvanceSlabConfig.setStatus(request.status());
        cashAdvanceSlabConfig.setRemarks(request.remark());
        cashAdvanceSlabConfig.setAdvanceRequestDay(request.advanceRequestDay());
        cashAdvanceSlabConfig.setEffectiveToDate(request.effectiveTo());
        cashAdvanceSlabConfig.setEffectiveFromDate(request.effectiveFrom());
        cashAdvanceSlabConfig.setServiceChargeType(request.serviceChargeType());
        cashAdvanceSlabConfig.setServiceChargeAmount(request.serviceChargeAmount());
        cashAdvanceSlabConfig.setAdvancePercent(request.advancePercent());
        cashAdvanceSlabConfig.setSetupName(request.setupName());

        request.cashAdvanceSlabConfigDetails().stream().forEach(details->{
            CashAdvanceSlabConfigDetails cashAdvanceSlabConfigDetails = new CashAdvanceSlabConfigDetails();
            cashAdvanceSlabConfigDetails.setServiceChargeType(details.serviceChargeType());
            cashAdvanceSlabConfigDetails.setServiceChargeAmount(details.serviceChargeAmount());
            cashAdvanceSlabConfigDetails.setFromAmount(details.fromAmount());
            cashAdvanceSlabConfigDetails.setToAmount(details.toAmount());
            cashAdvanceSlabConfig.addDetail(cashAdvanceSlabConfigDetails);
        });

        return cashAdvanceSlabConfigMapper.toDTO(cashAdvanceSlabConfigRepository.save(cashAdvanceSlabConfig));
    }

    @Override
    public CashAdvanceSlabConfigDTO show(Long cashAdvanceSlabConfigId) {
        CashAdvanceSlabConfig cashAdvanceSlabConfig = cashAdvanceSlabConfigRepository.findById(cashAdvanceSlabConfigId)
                .orElseThrow(() -> new CashAdvanceSlabConfigNotFoundException("CashAdvanceSlabConfig not found with id: " + cashAdvanceSlabConfigId));

        return cashAdvanceSlabConfigMapper.toDTO(cashAdvanceSlabConfig);
    }

    @Override
    public CashAdvanceSlabConfigDTO update(Long cashAdvanceSlabConfigId, UpdateCashAdvanceSlabConfigRequest request) {
        CashAdvanceSlabConfig cashAdvanceSlabConfig = cashAdvanceSlabConfigRepository.findById(cashAdvanceSlabConfigId)
                .orElseThrow(() -> new CashAdvanceSlabConfigNotFoundException("CashAdvanceSlabConfig not found with id: " + cashAdvanceSlabConfigId));


        if(request.employeeTypeId()!=null) {
            EmployeeType employeeType = employeeTypeRepository.findById(request.employeeTypeId())
                    .orElseThrow(() -> new EmployeeNotFound("EmployeeTypes not found: " + request.employeeTypeId()));
            cashAdvanceSlabConfig.setEmployeeType(employeeType);
        }
        else
            cashAdvanceSlabConfig.setEmployeeType(null);

        if(request.companyId() != null) {
            Company company = companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new CompanyNotFound("Company not found: " + request.companyId()));
            cashAdvanceSlabConfig.setCompany(company);
        }
        else
            cashAdvanceSlabConfig.setCompany(null);

        if(request.businessUnitId() != null) {
            BusinessUnit businessUnit = businessUnitRepository.findById(request.businessUnitId())
                    .orElseThrow(() -> new BusinessUnitNotFound("BusinessUnit not found: " + request.businessUnitId()));
            cashAdvanceSlabConfig.setBusinessUnit(businessUnit);
        }
        else
            cashAdvanceSlabConfig.setBusinessUnit(null);

        if(request.workplaceGroupId() != null) {
            WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(request.workplaceGroupId())
                    .orElseThrow(() -> new WorkplaceGroupNotFound("WorkplaceGroup not found: " + request.workplaceGroupId()));
            cashAdvanceSlabConfig.setWorkplaceGroup(workplaceGroup);
        }
        else
            cashAdvanceSlabConfig.setWorkplaceGroup(null);

        if(request.workplaceId() != null) {
            Workplace workplace = workplaceRepository.findById(request.workplaceId())
                    .orElseThrow(() -> new WorkplaceNotFound("Workplace not found: " + request.workplaceId()));
            cashAdvanceSlabConfig.setWorkplace(workplace);
        }
        else
            cashAdvanceSlabConfig.setWorkplace(null);


        cashAdvanceSlabConfig.setApprovedAmountChange(request.isApprovedAmountChange());
        cashAdvanceSlabConfig.setStatus(request.status());
        cashAdvanceSlabConfig.setRemarks(request.remark());
        cashAdvanceSlabConfig.setAdvanceRequestDay(request.advanceRequestDay());
        cashAdvanceSlabConfig.setEffectiveToDate(request.effectiveTo());
        cashAdvanceSlabConfig.setEffectiveFromDate(request.effectiveFrom());
        cashAdvanceSlabConfig.setServiceChargeType(request.serviceChargeType());
        cashAdvanceSlabConfig.setServiceChargeAmount(request.serviceChargeAmount());
        cashAdvanceSlabConfig.setAdvancePercent(request.advancePercent());
        cashAdvanceSlabConfig.setSetupName(request.setupName());

        // 2) index existing children by id
        Map<Long, CashAdvanceSlabConfigDetails> existingById = cashAdvanceSlabConfig.getCashAdvanceSlabConfigDetails()
                .stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(CashAdvanceSlabConfigDetails::getId, Function.identity()));


        Set<Long> keepIds = new HashSet<>();
        if (request.cashAdvanceSlabConfigDetails() != null) {
            for (UpdateCashAdvanceSlabConfigDetailRequest dto : request.cashAdvanceSlabConfigDetails()) {
                if (dto.id()!= null) {
                    CashAdvanceSlabConfigDetails d = existingById.get(dto.id());
                    if (d == null) {
                        throw new IllegalArgumentException("CashAdvanceSlabConfigDetails not found with id: " + dto.id());
                    }
                    d.setServiceChargeType(dto.serviceChargeType());
                    d.setServiceChargeAmount(dto.serviceChargeAmount());
                    d.setFromAmount(dto.fromAmount());
                    d.setToAmount(dto.toAmount());
                    keepIds.add(d.getId());
                } else {
                    CashAdvanceSlabConfigDetails d = new CashAdvanceSlabConfigDetails();
                    d.setServiceChargeType(dto.serviceChargeType());
                    d.setServiceChargeAmount(dto.serviceChargeAmount());
                    d.setFromAmount(dto.fromAmount());
                    d.setToAmount(dto.toAmount());
                    cashAdvanceSlabConfig.addDetail(d);
                }
            }

            cashAdvanceSlabConfig.getCashAdvanceSlabConfigDetails()
                    .removeIf(d -> d.getId() != null && !keepIds.contains(d.getId()));
        }
        return cashAdvanceSlabConfigMapper.toDTO(cashAdvanceSlabConfigRepository.save(cashAdvanceSlabConfig));
    }

    @Override
    public void delete(Long cashAdvanceSlabConfigId) {

        CashAdvanceSlabConfig cashAdvanceSlabConfig= cashAdvanceSlabConfigRepository.findById(cashAdvanceSlabConfigId)
                .orElseThrow(()->new CashAdvanceSlabConfigNotFoundException("CashAdvanceSlabConfig not found: " + cashAdvanceSlabConfigId));

        cashAdvanceSlabConfigRepository.delete(cashAdvanceSlabConfig);
    }
}
