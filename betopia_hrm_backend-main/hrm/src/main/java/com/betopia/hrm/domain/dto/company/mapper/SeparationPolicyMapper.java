package com.betopia.hrm.domain.dto.company.mapper;


import com.betopia.hrm.domain.company.entity.SeparationPolicy;
import com.betopia.hrm.domain.dto.company.SeparationPolicyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeparationPolicyMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "workplace.id", target = "workplaceId")
    SeparationPolicyDTO toDTO(SeparationPolicy separationPolicy);

    List<SeparationPolicyDTO> toDTOList(List<SeparationPolicy> separationPolicies);


}
