package com.betopia.hrm.services.admin;

import com.betopia.hrm.domain.admin.entity.Country;
import com.betopia.hrm.domain.admin.entity.Location;
import com.betopia.hrm.domain.admin.request.CountryRequest;
import com.betopia.hrm.domain.admin.request.LocationRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LocationService {

    PaginationResponse<Location> index(Sort.Direction direction, int page, int perPage);

    List<Location> getAllLocations();

    Location insert(LocationRequest request);

    Location show(Long locationId);

    Location update(Long locationId, LocationRequest request);

    void delete(Long countryId);
    List<Location>getAllDivisions(Integer countryId);
    List<Location>getDistrictsByDivisionId(Integer divisionId);
    List<Location>getPoliceStationsByDistrictId(Integer districtId);
    List<Location>getPostOfficeByDistrictId(Integer districtId);
}
