package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.InstituteName;
import com.betopia.hrm.domain.employee.exception.instituteName.InstituteNameNotFoundException;
import com.betopia.hrm.domain.employee.repository.InstituteNameRepository;
import com.betopia.hrm.domain.employee.request.InstituteNameRequest;
import com.betopia.hrm.services.employee.InstituteNameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InstituteNameServiceImpl implements InstituteNameService {

    private final InstituteNameRepository instituteNameRepository;

    public InstituteNameServiceImpl(InstituteNameRepository instituteNameRepository) {
        this.instituteNameRepository = instituteNameRepository;
    }

    @Override
    public PaginationResponse<InstituteName> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<InstituteName> instituteNamePage = instituteNameRepository.findAll(pageable);

        List<InstituteName> instituteNames = instituteNamePage.getContent();

        PaginationResponse<InstituteName> response = new PaginationResponse<>();

        response.setData(instituteNames);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All InstituteName fetch successful");

        Links links = Links.fromPage(instituteNamePage, "/instituteNames");
        response.setLinks(links);

        Meta meta = Meta.fromPage(instituteNamePage, "/instituteNames");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<InstituteName> getAllInstituteName() {
        List<InstituteName> instituteNames = instituteNameRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        if(instituteNames.isEmpty()){
            throw new ResourceNotFoundException("InstituteName not found");
        }
        return instituteNames;
    }

    @Override
    public InstituteName store(InstituteNameRequest request) {
        InstituteName instituteName = new InstituteName();
        instituteName.setName(request.name());
        instituteNameRepository.save(instituteName);

        return instituteName;
    }

    @Override
    public InstituteName show(Long instituteNameId) {
        InstituteName instituteName = instituteNameRepository.findById(instituteNameId)
                .orElseThrow(() -> new InstituteNameNotFoundException("InstituteNameId not found with id: " + instituteNameId));

        return instituteName;
    }

    @Override
    public InstituteName update(Long instituteNameId, InstituteNameRequest request) {
        InstituteName instituteName = instituteNameRepository.findById(instituteNameId)
                .orElseThrow(() -> new InstituteNameNotFoundException("InstituteName not found with id: " + instituteNameId));

        instituteName.setName(request.name());
        instituteName.setLastModifiedDate(LocalDateTime.now());
        instituteNameRepository.save(instituteName);

        return instituteName;
    }

    @Override
    public void delete(Long instituteNameId) {
        InstituteName instituteName = instituteNameRepository.findById(instituteNameId)
                .orElseThrow(() -> new RuntimeException("InstituteName not found with id: " + instituteNameId));

        instituteNameRepository.deleteById(instituteNameId);
    }
}
