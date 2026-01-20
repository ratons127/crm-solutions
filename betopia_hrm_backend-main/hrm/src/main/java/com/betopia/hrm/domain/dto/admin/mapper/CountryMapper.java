package com.betopia.hrm.domain.dto.admin.mapper;

import com.betopia.hrm.domain.admin.entity.Country;
import com.betopia.hrm.domain.dto.admin.CountryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CountryMapper {

    CountryDTO toDTO(Country country);

    List<CountryDTO> toDTOList(List<Country> countries);
}
