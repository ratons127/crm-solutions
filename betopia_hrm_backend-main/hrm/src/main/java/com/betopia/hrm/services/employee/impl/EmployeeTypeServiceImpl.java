package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.exception.employeeTypes.EmployeeTypesNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeTypeRepository;
import com.betopia.hrm.domain.employee.request.EmployeeTypeRequest;
import com.betopia.hrm.services.employee.EmployeeTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeTypeServiceImpl implements EmployeeTypeService {

    private final EmployeeTypeRepository employeeTypeRepository;

    public EmployeeTypeServiceImpl(EmployeeTypeRepository employeeTypeRepository) {
        this.employeeTypeRepository = employeeTypeRepository;
    }

    @Override
    public PaginationResponse<EmployeeType> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<EmployeeType> employeeTypesPage = employeeTypeRepository.findAll(pageable);

        List<EmployeeType> employeeTypes = employeeTypesPage.getContent();

        PaginationResponse<EmployeeType> response = new PaginationResponse<>();

        response.setData(employeeTypes);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All employee Types fetch successful");

        Links links = Links.fromPage(employeeTypesPage, "/employeeTypes");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeeTypesPage, "/employeeTypes");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmployeeType> getAllEmployeeTypes() {

        return employeeTypeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public EmployeeType insert(EmployeeTypeRequest request) {

        EmployeeType employeeType = new EmployeeType();
        employeeType.setName(request.name());
        employeeType.setDescription(request.description());
        employeeType.setStatus(request.status());
        employeeTypeRepository.save(employeeType);

        return employeeType;
    }

    @Override
    public EmployeeType show(Long employeeTypesId) {
        EmployeeType employeeType = employeeTypeRepository.findById(employeeTypesId)
                .orElseThrow(() -> new EmployeeTypesNotFound("Employee Types not found with id: " + employeeTypesId));

        return employeeType;
    }

    @Override
    public EmployeeType update(Long employeeTypesId, EmployeeTypeRequest request) {
        EmployeeType employeeType = employeeTypeRepository.findById(employeeTypesId)
                .orElseThrow(() -> new EmployeeTypesNotFound("Employee types not found with id: " + employeeTypesId));

        employeeType.setName(request.name());
        employeeType.setDescription(request.description());
        employeeType.setStatus(request.status());
        employeeTypeRepository.save(employeeType);
        return employeeType;
    }

    @Override
    public void delete(Long employeeTypesId) {
        EmployeeType employeeType = employeeTypeRepository.findById(employeeTypesId)
                .orElseThrow(() -> new EmployeeTypesNotFound("Employee types not found with id: " + employeeTypesId));

        employeeTypeRepository.deleteById(employeeTypesId);
    }
}
