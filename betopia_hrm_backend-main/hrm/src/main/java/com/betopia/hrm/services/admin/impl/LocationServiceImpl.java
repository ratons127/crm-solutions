package com.betopia.hrm.services.admin.impl;

import com.betopia.hrm.domain.admin.entity.Location;
import com.betopia.hrm.domain.admin.enums.LocationType;
import com.betopia.hrm.domain.admin.exceptions.CountryNotFound;
import com.betopia.hrm.domain.admin.exceptions.LocationNotFound;
import com.betopia.hrm.domain.admin.repository.LocationRepository;
import com.betopia.hrm.domain.admin.request.LocationRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.admin.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public PaginationResponse<Location> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<Location> locationPage = locationRepository.findAll(pageable);

        List<Location> locations = locationPage.getContent();

        PaginationResponse<Location> response = new PaginationResponse<>();

        response.setData(locations);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All country fetch successful");

        Links links = Links.fromPage(locationPage, "/location");
        response.setLinks(links);

        Meta meta = Meta.fromPage(locationPage, "/location");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<Location> getAllLocations() {

        return locationRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Location insert(LocationRequest request) {

        Location location = new Location();
        location.setCountryId(request.countryId());
        if(request.type().equals(LocationType.DIVISION.name()))
            location.setParentId(0);
        else
            location.setParentId(request.parentId());
        location.setName(request.name());
        location.setType(request.type());
        location.setGeoHash(request.geoHash());
        location.setStatus(true);
        locationRepository.save(location);
        return location;
    }

    @Override
    public Location show(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFound("Location not found with id: " + locationId));

        return location;
    }

    @Override
    public Location update(Long locationId, LocationRequest request) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFound("Location not found with id: " + locationId));

        location.setCountryId(request.countryId());
        if(request.type().equals(LocationType.DIVISION.name()))
            location.setParentId(0);
        else
            location.setParentId(request.parentId());
        location.setName(request.name());
        location.setType(request.type());
        location.setGeoHash(request.geoHash());
        location.setStatus(request.status());
        locationRepository.save(location);
        return location;
    }

    @Override
    public void delete(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFound("Location not found with id: " + locationId));

        locationRepository.deleteById(locationId);
    }

    @Override
    public List<Location> getAllDivisions(Integer countryId) {

        return locationRepository.findAll().stream()
                .filter(location -> location.getCountryId().equals(countryId) && location.getType().equals(LocationType.DIVISION.name()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Location> getDistrictsByDivisionId(Integer  divisionId) {

        return locationRepository.findAll().stream()
                .filter(location -> location.getParentId().equals(divisionId) && location.getType().equals(LocationType.DISTRICT.name()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Location> getPoliceStationsByDistrictId(Integer  districtId) {

        return locationRepository.findAll().stream()
                .filter(location -> location.getParentId().equals(districtId) && location.getType().equals(LocationType.POLICE_STATION.name()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Location> getPostOfficeByDistrictId(Integer  districtId) {

        return locationRepository.findAll().stream()
                .filter(location-> location.getParentId().equals(districtId) && location.getType().equals(LocationType.POST_OFFICE.name()))
                .collect(Collectors.toList());
    }
}
