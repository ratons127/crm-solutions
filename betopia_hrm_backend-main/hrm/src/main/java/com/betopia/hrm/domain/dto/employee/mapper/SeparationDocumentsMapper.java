package com.betopia.hrm.domain.dto.employee.mapper;

import com.betopia.hrm.domain.dto.employee.SeparationDocumentsDTO;
import com.betopia.hrm.domain.employee.entity.SeparationDocuments;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeparationDocumentsMapper {
    SeparationDocumentsDTO toDTO(SeparationDocuments separationDocument);

    // List mapping
    List<SeparationDocumentsDTO> toDTOList(List<SeparationDocuments> separationDocuments);

}
