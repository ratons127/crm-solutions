package com.betopia.hrm.services.company.bank;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Bank;
import com.betopia.hrm.domain.company.request.BankRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BankService {

    PaginationResponse<Bank> index(Sort.Direction direction, int page, int perPage);

    List<Bank> getAllBanks();

    Bank store(BankRequest request);

    Bank show(Long bankId);

    Bank update(Long bankId, BankRequest request);

    void destroy(Long bankId);
}
