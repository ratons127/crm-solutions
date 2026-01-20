package com.betopia.hrm.services.employee.separationdocuments;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.SeparationDocumentsDTO;
import com.betopia.hrm.domain.employee.entity.SeparationDocuments;
import com.betopia.hrm.domain.employee.request.FinalSettlementRequest;
import com.betopia.hrm.domain.employee.request.SeparationDocumentsRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SeparationDocumentsService {

    PaginationResponse<SeparationDocumentsDTO> index(Sort.Direction direction, int page, int perPage);

    List<SeparationDocumentsDTO> getAll();

    SeparationDocumentsDTO show(Long id);

    SeparationDocumentsDTO update(Long id,  SeparationDocumentsRequest request, List<MultipartFile> files);

    void destroy(Long id);

    SeparationDocumentsDTO saveSeparation(SeparationDocumentsRequest request, List<MultipartFile> files);

}
