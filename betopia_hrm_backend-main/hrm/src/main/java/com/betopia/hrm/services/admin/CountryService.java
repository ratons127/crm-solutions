package com.betopia.hrm.services.admin;

import com.betopia.hrm.domain.admin.entity.Country;
import com.betopia.hrm.domain.admin.request.CountryRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.users.entity.PasswordPolicy;
import com.betopia.hrm.domain.users.request.PasswordPolicyRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CountryService {

    PaginationResponse<Country> index(Sort.Direction direction, int page, int perPage);

    List<Country> getAllCountries();

    Country insert(CountryRequest request);

    Country show(Long countryId);

    Country update(Long countryId, CountryRequest request);

    void delete(Long countryId);
}
