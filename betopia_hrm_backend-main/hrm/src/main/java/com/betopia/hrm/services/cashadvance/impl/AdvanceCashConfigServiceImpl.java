package com.betopia.hrm.services.cashadvance.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashConfig;
import com.betopia.hrm.domain.cashadvance.exception.AdvanceCashConfigNotFoundException;
import com.betopia.hrm.domain.cashadvance.mapper.AdvanceCashConfigRequestMapper;
import com.betopia.hrm.domain.cashadvance.repository.AdvanceCashConfigRepository;
import com.betopia.hrm.domain.cashadvance.request.AdvanceCashConfigRequest;
import com.betopia.hrm.domain.dto.cashadvance.AdvanceCashConfigDTO;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceSlabConfigDTO;
import com.betopia.hrm.domain.dto.cashadvance.mapper.AdvanceCashConfigMapper;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeTypeRepository;
import com.betopia.hrm.services.cashadvance.AdvanceCashConfigService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvanceCashConfigServiceImpl implements AdvanceCashConfigService {

    private final AdvanceCashConfigRepository advanceCashConfigRepository;
    private final AdvanceCashConfigRequestMapper advanceCashConfigRequestMapper;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final AdvanceCashConfigMapper advanceCashConfigMapper;


    public AdvanceCashConfigServiceImpl(AdvanceCashConfigRepository advanceCashConfigRepository, AdvanceCashConfigRequestMapper advanceCashConfigRequestMapper, EmployeeTypeRepository employeeTypeRepository, AdvanceCashConfigMapper advanceCashConfigMapper) {
        this.advanceCashConfigRepository = advanceCashConfigRepository;
        this.advanceCashConfigRequestMapper = advanceCashConfigRequestMapper;
        this.employeeTypeRepository = employeeTypeRepository;
        this.advanceCashConfigMapper = advanceCashConfigMapper;
    }

    @Override
    public PaginationResponse<AdvanceCashConfigDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<AdvanceCashConfig> advanceCashConfigPage = advanceCashConfigRepository.findAll(pageable);

        List<AdvanceCashConfig> advanceCashConfigs = advanceCashConfigPage.getContent();
        List<AdvanceCashConfigDTO> advanceCashConfigDTOs = advanceCashConfigMapper.toDTOList(advanceCashConfigs);

        PaginationResponse<AdvanceCashConfigDTO> response = new PaginationResponse<>();

        response.setData(advanceCashConfigDTOs);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All employee fetch successful");

        Links links = Links.fromPage(advanceCashConfigPage, "/advanceCashConfigs");
        response.setLinks(links);

        Meta meta = Meta.fromPage(advanceCashConfigPage, "/advanceCashConfigs");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<AdvanceCashConfigDTO> getAllAdvanceCashConfig() {
        return advanceCashConfigRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(advanceCashConfigMapper::toDTO)
                .toList();
    }

    @Override
    public AdvanceCashConfigDTO store(AdvanceCashConfigRequest request) {
        AdvanceCashConfig advanceCashConfig =advanceCashConfigRequestMapper.toEntity(request);
        advanceCashConfigRepository.save(advanceCashConfig);
        return advanceCashConfigMapper.toDTO(advanceCashConfigRepository.save(advanceCashConfig));
    }

    @Override
    public AdvanceCashConfigDTO show(Long advanceCashConfigId) {
        AdvanceCashConfig advanceCashConfig = advanceCashConfigRepository.findById(advanceCashConfigId)
                .orElseThrow(() -> new AdvanceCashConfigNotFoundException("Advance cash not found with id: " + advanceCashConfigId));

        return advanceCashConfigMapper.toDTO(advanceCashConfig);
    }

    @Override
    public AdvanceCashConfigDTO update(Long advanceCashConfigId, AdvanceCashConfigRequest request) {
        AdvanceCashConfig advanceCashConfig = advanceCashConfigRepository.findById(advanceCashConfigId)
                .orElseThrow(() -> new AdvanceCashConfigNotFoundException("AdvanceCashConfig not found with id: " + advanceCashConfigId));


        if(request.employeeTypeId()!=null) {
            EmployeeType employeeType = employeeTypeRepository.findById(request.employeeTypeId())
                    .orElseThrow(() -> new EmployeeNotFound("EmployeeTypes not found: " + request.employeeTypeId()));
            advanceCashConfig.setEmployeeType(employeeType);
        }
        else
            advanceCashConfig.setEmployeeType(null);

        advanceCashConfig.setMinimumWorkingDays(request.minimumWorkingDays());
        advanceCashConfig.setAdvanceLimitPercent(request.advanceLimitPercent());
        advanceCashConfig.setAdvanceLimitAmount(request.advanceLimitAmount());
        advanceCashConfig.setServiceChargeAmount(request.serviceChargeAmount());
        advanceCashConfig.setServiceChargePercent(request.serviceChargePercent());
        advanceCashConfig.setApprovedAmountChange(request.isApprovedAmountChange());
        advanceCashConfig.setStatus(request.status());

        return advanceCashConfigMapper.toDTO(advanceCashConfigRepository.save(advanceCashConfig));
    }

    @Override
    public void delete(Long advanceCashConfigId) {
        AdvanceCashConfig advanceCashConfig = advanceCashConfigRepository.findById(advanceCashConfigId)
                .orElseThrow(() -> new AdvanceCashConfigNotFoundException("AdvanceCashConfig not found with id: " + advanceCashConfigId));

        advanceCashConfigRepository.deleteById(advanceCashConfigId);
    }
}
