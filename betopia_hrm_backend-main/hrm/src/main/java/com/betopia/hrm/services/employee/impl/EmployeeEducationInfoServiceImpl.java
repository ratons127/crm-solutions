package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeEducationInfo;
import com.betopia.hrm.domain.employee.entity.FieldStudy;
import com.betopia.hrm.domain.employee.entity.InstituteName;
import com.betopia.hrm.domain.employee.entity.QualificationLevel;
import com.betopia.hrm.domain.employee.entity.QualificationRatingMethod;
import com.betopia.hrm.domain.employee.entity.QualificationType;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.exception.employeeeducationinfo.EmployeeEducationInfoNotFoundException;
import com.betopia.hrm.domain.employee.exception.fieldStudy.FieldStudyNotFoundException;
import com.betopia.hrm.domain.employee.exception.instituteName.InstituteNameNotFoundException;
import com.betopia.hrm.domain.employee.exception.qualificationLevel.QualificationLevelNotFoundException;
import com.betopia.hrm.domain.employee.exception.qualificationRatingMethod.QualificationRatingMethodNotFound;
import com.betopia.hrm.domain.employee.exception.qualificationType.QualificationTypeNotFoundException;
import com.betopia.hrm.domain.employee.mapper.EmployeeEducationInfoMapper;
import com.betopia.hrm.domain.employee.repository.EmployeeEducationInfoRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.FieldStudyRepository;
import com.betopia.hrm.domain.employee.repository.InstituteNameRepository;
import com.betopia.hrm.domain.employee.repository.QualificationLevelRepository;
import com.betopia.hrm.domain.employee.repository.QualificationRatingMethodRepository;
import com.betopia.hrm.domain.employee.repository.QualificationTypeRepository;
import com.betopia.hrm.domain.employee.request.EmployeeEducationInfoRequest;
import com.betopia.hrm.services.employee.EmployeeEducationInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeEducationInfoServiceImpl implements EmployeeEducationInfoService {

    private final EmployeeEducationInfoRepository employeeEducationInfoRepository;
    private final EmployeeEducationInfoMapper employeeEducationInfoMapper;
    private final EmployeeRepository employeeRepository;
    private final QualificationTypeRepository qualificationTypeRepository;
    private final QualificationLevelRepository qualificationLevelRepository;
    private final InstituteNameRepository instituteNameRepository;
    private final FieldStudyRepository fieldStudyRepository;
    private final QualificationRatingMethodRepository qualificationRatingMethodRepository;

    public EmployeeEducationInfoServiceImpl(EmployeeEducationInfoRepository employeeEducationInfoRepository, EmployeeEducationInfoMapper employeeEducationInfoMapper, EmployeeRepository employeeRepository, QualificationTypeRepository qualificationTypeRepository, QualificationLevelRepository qualificationLevelRepository, InstituteNameRepository instituteNameRepository, FieldStudyRepository fieldStudyRepository, QualificationRatingMethodRepository qualificationRatingMethodRepository) {
        this.employeeEducationInfoRepository = employeeEducationInfoRepository;
        this.employeeEducationInfoMapper = employeeEducationInfoMapper;
        this.employeeRepository = employeeRepository;
        this.qualificationTypeRepository = qualificationTypeRepository;
        this.qualificationLevelRepository = qualificationLevelRepository;
        this.instituteNameRepository = instituteNameRepository;
        this.fieldStudyRepository = fieldStudyRepository;
        this.qualificationRatingMethodRepository = qualificationRatingMethodRepository;
    }


    @Override
    public PaginationResponse<EmployeeEducationInfo> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<EmployeeEducationInfo> employeeEducationInfoPage = employeeEducationInfoRepository.findAll(pageable);

        List<EmployeeEducationInfo> employeeEducationInfos = employeeEducationInfoPage.getContent();

        PaginationResponse<EmployeeEducationInfo> response = new PaginationResponse<>();

        response.setData(employeeEducationInfos);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All EmployeeEducationInfo fetch successful");

        Links links = Links.fromPage(employeeEducationInfoPage, "/employeeEducationInfos");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeeEducationInfoPage, "/employeeEducationInfos");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmployeeEducationInfo> getAllEmployeeEducationInfo() {
        return employeeEducationInfoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public EmployeeEducationInfo store(EmployeeEducationInfoRequest request) {
        EmployeeEducationInfo employeeEducationInfo =employeeEducationInfoMapper.toEntity(request);

        employeeEducationInfoRepository.save(employeeEducationInfo);
        return employeeEducationInfo;
    }

    @Override
    public EmployeeEducationInfo show(Long employeeEducationInfoId) {
        EmployeeEducationInfo employeeEducationInfo = employeeEducationInfoRepository.findById(employeeEducationInfoId)
                .orElseThrow(() -> new EmployeeEducationInfoNotFoundException("EmployeeEducationInfo not found with id: " + employeeEducationInfoId));

        return employeeEducationInfo;
    }

    @Override
    public EmployeeEducationInfo update(Long employeeEducationInfoId, EmployeeEducationInfoRequest request) {
        EmployeeEducationInfo employeeEducationInfo = employeeEducationInfoRepository.findById(employeeEducationInfoId)
                .orElseThrow(() -> new EmployeeEducationInfoNotFoundException("EmployeeEducationInfo not found with id: " + employeeEducationInfoId));

        employeeEducationInfo.setSubject(request.subject());
        employeeEducationInfo.setResult(request.result());
        employeeEducationInfo.setPassingYear(request.passingYear());

        if(request.employeeId()!=null) {
            Employee employee = employeeRepository.findById(request.employeeId())
                    .orElseThrow(() -> new EmployeeNotFound("Employee not found: " + request.employeeId()));
            employeeEducationInfo.setEmployee(employee);
        }
        else
            employeeEducationInfo.setEmployee(null);


        if(request.qualificationTypeId() != null) {
            QualificationType qualificationType = qualificationTypeRepository.findById(request.qualificationTypeId())
                    .orElseThrow(() -> new QualificationTypeNotFoundException("Qualification Type not found: " + request.qualificationTypeId()));
            employeeEducationInfo.setQualificationType(qualificationType);
        }
        else
            employeeEducationInfo.setQualificationType(null);


        if(request.qualificationLevelId() != null) {
            QualificationLevel qualificationLevel = qualificationLevelRepository.findById(request.qualificationLevelId())
                    .orElseThrow(() -> new QualificationLevelNotFoundException("Qualification Level not found: " + request.qualificationLevelId()));
            employeeEducationInfo.setQualificationLevel(qualificationLevel);
        }
        else
            employeeEducationInfo.setQualificationLevel(null);

        if(request.instituteNameId() != null) {
            InstituteName instituteName = instituteNameRepository.findById(request.instituteNameId())
                    .orElseThrow(() -> new InstituteNameNotFoundException("Institute name not found: " + request.instituteNameId()));
            employeeEducationInfo.setInstituteName(instituteName);
        }
        else
            employeeEducationInfo.setInstituteName(null);

        if(request.fieldStudyId() != null){
            FieldStudy fieldStudy = fieldStudyRepository.findById(request.fieldStudyId())
                    .orElseThrow(() -> new FieldStudyNotFoundException("Field study not found: " + request.fieldStudyId()));
            employeeEducationInfo.setFieldStudy(fieldStudy);
        }
        else
            employeeEducationInfo.setFieldStudy(null);

        if(request.qualificationRatingMethodId() != null){
            QualificationRatingMethod qualificationRatingMethod = qualificationRatingMethodRepository.findById(request.qualificationRatingMethodId())
                    .orElseThrow(() -> new QualificationRatingMethodNotFound("Qualification rating method not found: " + request.qualificationRatingMethodId()));
            employeeEducationInfo.setQualificationRatingMethod(qualificationRatingMethod);
        }

        employeeEducationInfoRepository.save(employeeEducationInfo);
        return employeeEducationInfo;
    }

    @Override
    public void delete(Long employeeEducationInfoId) {
        EmployeeEducationInfo employeeEducationInfo = employeeEducationInfoRepository.findById(employeeEducationInfoId)
                .orElseThrow(() -> new EmployeeEducationInfoNotFoundException("EmployeeEducationInfo not found with id: " + employeeEducationInfoId));

        employeeEducationInfoRepository.deleteById(employeeEducationInfoId);
    }
}
