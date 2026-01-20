package com.betopia.hrm.domain.dto.employee.mapper;


import com.betopia.hrm.domain.dto.employee.EmployeeClearanceChecklistDTO;
import com.betopia.hrm.domain.employee.entity.EmployeeClearanceChecklist;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeClearanceChecklistMapper {

    EmployeeClearanceChecklistDTO toDTO(EmployeeClearanceChecklist employeeClearanceChecklist);

    // List mapping
    List<EmployeeClearanceChecklistDTO> toDTOList(List<EmployeeClearanceChecklist> employeeClearanceChecklists);
}
