package com.betopia.hrm.services.users.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.request.CompanyRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.time.ZoneId;
import java.util.Currency;
import java.util.List;

public interface CompanyService {

    PaginationResponse<Company> index(Sort.Direction direction, int page, int perPage);

    List<Company> getAllCompanies();

    Company store(CompanyRequest request);

    Company show(Long companyId);

    Company update(Long companyId, CompanyRequest request);

    void destroy(Long companyId);

    ResponseEntity<GlobalResponse> uploadCompanyImage(Long companyId, MultipartFile file);

    ResponseEntity<GlobalResponse> deleteCompanyImage(Long companyId);

    List<Currency> getAllCurrencies();

    List<ZoneId> getAllTimeZone();

}
