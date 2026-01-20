package com.betopia.hrm.domain.cashadvance.mapper;

import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashConfig;
import com.betopia.hrm.domain.cashadvance.request.AdvanceCashConfigRequest;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class AdvanceCashConfigRequestMapper {
    private final EmployeeTypeRepository employeeTypeRepository;

    public AdvanceCashConfigRequestMapper(EmployeeTypeRepository employeeTypeRepository) {
        this.employeeTypeRepository = employeeTypeRepository;
    }


    /**
     * Map CreateRequest DTO -> Entity
     */
    public AdvanceCashConfig toEntity(AdvanceCashConfigRequest request) {
        AdvanceCashConfig advanceCashConfig = new AdvanceCashConfig();
        entityDto(advanceCashConfig, request);
        return advanceCashConfig;
    }

    /**
     * Update Entity fields from CreateRequest
     */
    public void entityDto(AdvanceCashConfig advanceCashConfig, AdvanceCashConfigRequest request) {
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
    }
}
