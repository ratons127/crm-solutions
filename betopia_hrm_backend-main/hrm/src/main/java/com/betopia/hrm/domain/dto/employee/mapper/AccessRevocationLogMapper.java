package com.betopia.hrm.domain.dto.employee.mapper;


import com.betopia.hrm.domain.dto.employee.AccessRevocationLogDTO;
import com.betopia.hrm.domain.employee.entity.AccessRevocationLog;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccessRevocationLogMapper {

    AccessRevocationLogDTO toDTO(AccessRevocationLog accessRevocationLog);
    List<AccessRevocationLogDTO> toDTOList(List<AccessRevocationLog> accessRevocationLogs);
}
