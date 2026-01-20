package com.betopia.hrm.services.company.bankbranch;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.BankBranch;
import com.betopia.hrm.domain.company.request.BankBranchRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BankBranchService {

    PaginationResponse<BankBranch> index(Sort.Direction direction, int page, int perPage);

    List<BankBranch> getAllBankBranches();

    BankBranch store(BankBranchRequest request);

    BankBranch show(Long bankBranchId);

    BankBranch update(Long bankBranchId, BankBranchRequest request);

    void destroy(Long bankBranchId);
}
