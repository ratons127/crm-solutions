package com.betopia.hrm.domain.dto.admin.mapper;

import com.betopia.hrm.domain.admin.entity.Location;
import com.betopia.hrm.domain.dto.admin.LocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    LocationDTO toDTO(Location location);

    List<LocationDTO> toDTOList(List<Location> locations);
}
