package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeWorkExperience;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.exception.employeeworkexperience.EmployeeWorkExperienceNotFoundException;
import com.betopia.hrm.domain.employee.mapper.EmployeeWorkExperienceMapper;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeWorkExperienceRepository;
import com.betopia.hrm.domain.employee.request.EmployeeWorkExperienceRequest;
import com.betopia.hrm.services.employee.EmployeeWorkExperienceService;
import com.betopia.hrm.webapp.util.S3Service;
import com.betopia.hrm.webapp.util.UploadFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeWorkExperienceServiceImpl implements EmployeeWorkExperienceService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeWorkExperienceService.class.getName());

    private final EmployeeWorkExperienceRepository employeeWorkExperienceRepository;
    private final EmployeeWorkExperienceMapper employeeWorkExperienceMapper;
    private final UploadFiles uploadFiles;
    private final EmployeeRepository employeeRepository;


    public EmployeeWorkExperienceServiceImpl(EmployeeWorkExperienceRepository employeeWorkExperienceRepository, EmployeeWorkExperienceMapper employeeWorkExperienceMapper, UploadFiles uploadFiles, EmployeeRepository employeeRepository) {
        this.employeeWorkExperienceRepository = employeeWorkExperienceRepository;
        this.employeeWorkExperienceMapper = employeeWorkExperienceMapper;
        this.uploadFiles = uploadFiles;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public PaginationResponse<EmployeeWorkExperience> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<EmployeeWorkExperience> employeeWorkExperiencePage = employeeWorkExperienceRepository.findAll(pageable);

        List<EmployeeWorkExperience> employeeWorkExperiences = employeeWorkExperiencePage.getContent();

        PaginationResponse<EmployeeWorkExperience> response = new PaginationResponse<>();

        response.setData(employeeWorkExperiences);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All employeeWorkExperiences fetch successful");

        Links links = Links.fromPage(employeeWorkExperiencePage, "/employeeWorkExperiences");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeeWorkExperiencePage, "/employeeWorkExperiences");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmployeeWorkExperience> getAllEmployeeWorkExperience() {
        return employeeWorkExperienceRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public EmployeeWorkExperience store(EmployeeWorkExperienceRequest request) {
        EmployeeWorkExperience employeeWorkExperience =employeeWorkExperienceMapper.toEntity(request);

        employeeWorkExperienceRepository.save(employeeWorkExperience);
        return employeeWorkExperience;
    }

    @Override
    public EmployeeWorkExperience show(Long employeeWorkExperienceId) {
        EmployeeWorkExperience employeeWorkExperience = employeeWorkExperienceRepository.findById(employeeWorkExperienceId)
                .orElseThrow(() -> new EmployeeWorkExperienceNotFoundException("EmployeeWorkExperience not found with id: " + employeeWorkExperienceId));

        return employeeWorkExperience;
    }

    @Override
    public EmployeeWorkExperience update(Long employeeWorkExperienceId, EmployeeWorkExperienceRequest request) {
        EmployeeWorkExperience employeeWorkExperience = employeeWorkExperienceRepository.findById(employeeWorkExperienceId)
                .orElseThrow(() -> new EmployeeWorkExperienceNotFoundException("EmployeeWorkExperience not found with id: " + employeeWorkExperienceId));

        employeeWorkExperience.setCompanyName(request.companyName());
        employeeWorkExperience.setJobDescription(request.jobDescription());
        employeeWorkExperience.setJobTitle(request.jobTitle());
        employeeWorkExperience.setLocation(request.location());
        employeeWorkExperience.setFromDate(request.fromDate());
        employeeWorkExperience.setToDate(request.toDate());
        employeeWorkExperience.setTenure(request.tenure());
        employeeWorkExperience.setImage(request.image());
        employeeWorkExperience.setImageUrl(request.imageUrl());
        if(request.employeeId()!=null) {
            Employee employee = employeeRepository.findById(request.employeeId())
                    .orElseThrow(() -> new EmployeeNotFound("Employee not found: " + request.employeeId()));
            employeeWorkExperience.setEmployee(employee);
        }
        else
            employeeWorkExperience.setEmployee(null);

        employeeWorkExperienceRepository.save(employeeWorkExperience);
        return employeeWorkExperience;
    }

    @Override
    public void delete(Long employeeWorkExperienceId) {
        EmployeeWorkExperience employeeWorkExperience = employeeWorkExperienceRepository.findById(employeeWorkExperienceId)
                .orElseThrow(() -> new EmployeeWorkExperienceNotFoundException("EmployeeWorkExperience not found with id: " + employeeWorkExperienceId));

        employeeWorkExperienceRepository.deleteById(employeeWorkExperienceId);
    }

    @Override
    public ResponseEntity<GlobalResponse> uploadFile(Long employeeWorkExperienceId, MultipartFile file) {
        return uploadFiles.upload(file, "employeeWorkExperiences", (imageUrl) -> {
            EmployeeWorkExperience employeeWorkExperience = employeeWorkExperienceRepository.findById(employeeWorkExperienceId)
                    .orElseThrow(() -> new EmployeeWorkExperienceNotFoundException("EmployeeWorkExperience not found " + employeeWorkExperienceId));

            employeeWorkExperience.setImage("employeeWorkExperiences/" + file.getOriginalFilename());
            employeeWorkExperience.setImageUrl(imageUrl);
            return employeeWorkExperienceRepository.save(employeeWorkExperience);
        });
    }

    @Override
    public ResponseEntity<GlobalResponse> deleteFile(Long employeeWorkExperienceId) {
        return uploadFiles.delete(
                employeeWorkExperienceId,
                () -> employeeWorkExperienceRepository.findById(employeeWorkExperienceId)
                        .orElseThrow(() -> new EmployeeWorkExperienceNotFoundException("EmployeeWorkExperience not found " + employeeWorkExperienceId)),

                (entity) -> {
                    EmployeeWorkExperience employeeWorkExperience = (EmployeeWorkExperience) entity;

                    // delete from S3
                    if (employeeWorkExperience.getImage() != null) {
                        try {
                            uploadFiles.deleteFromS3(employeeWorkExperience.getImage());
                        } catch (Exception e) {
                            log.error("Failed to delete image from S3 for companyId={}, image={}",
                                    employeeWorkExperienceId, employeeWorkExperience.getImage(), e);
                        }
                    }

                    // clear DB refs
                    employeeWorkExperience.setImage(null);
                    employeeWorkExperience.setImageUrl(null);
                    employeeWorkExperienceRepository.save(employeeWorkExperience);

                    log.info("Image deleted successfully for employeeWorkExperienceId={}", employeeWorkExperienceId);
                }
        );
    }


}
