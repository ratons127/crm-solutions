package com.betopia.hrm.services.admin.impl;

import com.betopia.hrm.domain.admin.entity.Country;
import com.betopia.hrm.domain.admin.exceptions.CountryNotFound;
import com.betopia.hrm.domain.admin.repository.CountryRepository;
import com.betopia.hrm.domain.admin.request.CountryRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.admin.CountryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public PaginationResponse<Country> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<Country> countryPage = countryRepository.findAll(pageable);

        List<Country> countries = countryPage.getContent();

        PaginationResponse<Country> response = new PaginationResponse<>();

        response.setData(countries);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All country fetch successful");

        Links links = Links.fromPage(countryPage, "/country");
        response.setLinks(links);

        Meta meta = Meta.fromPage(countryPage, "/country");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<Country> getAllCountries() {

        return countryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Country insert(CountryRequest request) {

        Country country = new Country();
        country.setCode(request.code());
        country.setName(request.name());
        country.setRegion(request.region());
        countryRepository.save(country);
        return country;
    }

    @Override
    public Country show(Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new CountryNotFound("Country not found with id: " + countryId));

        return country;
    }

    @Override
    public Country update(Long countryId, CountryRequest request) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new CountryNotFound("Country not found with id: " + countryId));

        country.setCode(request.code());
        country.setName(request.name());
        country.setRegion(request.region());
        countryRepository.save(country);
        return country;
    }

    @Override
    public void delete(Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new CountryNotFound("Country not found with id: " + countryId));

        countryRepository.deleteById(countryId);
    }
}
