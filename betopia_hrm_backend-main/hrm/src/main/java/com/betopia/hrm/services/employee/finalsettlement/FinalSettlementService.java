package com.betopia.hrm.services.employee.finalsettlement;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.FinalSettlementDTO;
import com.betopia.hrm.domain.employee.request.FinalSettlementRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FinalSettlementService {


    PaginationResponse<FinalSettlementDTO> index(Sort.Direction direction, int page, int perPage);

    List<FinalSettlementDTO> getAll();

    FinalSettlementDTO store(FinalSettlementRequest request);

    FinalSettlementDTO show(Long id);

    FinalSettlementDTO update(Long id,  FinalSettlementRequest request, List<MultipartFile> files);

    void destroy(Long id);

    FinalSettlementDTO saveSettlement(FinalSettlementRequest request, List<MultipartFile> files);

}
