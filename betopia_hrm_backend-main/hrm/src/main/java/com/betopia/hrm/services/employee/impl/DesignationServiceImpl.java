package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.request.DesignationRequest;
import com.betopia.hrm.services.employee.DesignationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DesignationServiceImpl implements DesignationService {
    private final DesignationRepository designationRepository;

    public DesignationServiceImpl(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }


    @Override
    public PaginationResponse<Designation> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<Designation> designationPage = designationRepository.findAll(pageable);
        List<Designation> designations = designationPage.getContent();
        PaginationResponse<Designation> response = new PaginationResponse<>();
        response.setData(designations);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All designations fetch successful");
        Links links = Links.fromPage(designationPage, "/designation");
        response.setLinks(links);
        Meta meta = Meta.fromPage(designationPage, "/designation");
        response.setMeta(meta);
        return response;
    }


    @Override
    public List<Designation> getAllDesignations() {
        return designationRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }


    @Override
    public Designation store(DesignationRequest request) {
        Designation designation = new Designation();
        designation.setName(request.name());
        designation.setDescription(request.description());
        designation.setStatus(request.status());
        return designationRepository.save(designation);
    }


    @Override
    public Designation update(Long designationId, DesignationRequest request) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new RuntimeException("Designation not found with id: " + designationId));
        designation.setName(request.name() != null ? request.name() : designation.getName());
        designation.setDescription(request.description() != null ? request.description() : designation.getDescription());
        designation.setStatus(request.status() != null ? request.status() : designation.getStatus());
        designationRepository.save(designation);
        return designation;
    }


    @Override
    public Designation show(Long designationId) {
        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new RuntimeException("Designation not found with id: " + designationId));
        return designation;
    }


    @Override
    public void destroy(Long designationId) {
        designationRepository.deleteById(designationId);
    }

}
