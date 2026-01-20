package com.betopia.hrm.domain.dto.employee.mapper;


import com.betopia.hrm.domain.dto.employee.EmployeeDocumentDTO;
import com.betopia.hrm.domain.employee.entity.EmployeeDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeDocumentMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "documentType.id", target = "documentTypeId")

    EmployeeDocumentDTO toDTO(EmployeeDocument employeeDocument);
    List<EmployeeDocumentDTO> toDTOList(List<EmployeeDocument> employeeDocuments);

}
