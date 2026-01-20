package com.betopia.hrm.domain.employee.mapper;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeEducationInfo;
import com.betopia.hrm.domain.employee.entity.FieldStudy;
import com.betopia.hrm.domain.employee.entity.InstituteName;
import com.betopia.hrm.domain.employee.entity.QualificationLevel;
import com.betopia.hrm.domain.employee.entity.QualificationRatingMethod;
import com.betopia.hrm.domain.employee.entity.QualificationType;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.exception.fieldStudy.FieldStudyNotFoundException;
import com.betopia.hrm.domain.employee.exception.instituteName.InstituteNameNotFoundException;
import com.betopia.hrm.domain.employee.exception.qualificationLevel.QualificationLevelNotFoundException;
import com.betopia.hrm.domain.employee.exception.qualificationRatingMethod.QualificationRatingMethodNotFound;
import com.betopia.hrm.domain.employee.exception.qualificationType.QualificationTypeNotFoundException;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.FieldStudyRepository;
import com.betopia.hrm.domain.employee.repository.InstituteNameRepository;
import com.betopia.hrm.domain.employee.repository.QualificationLevelRepository;
import com.betopia.hrm.domain.employee.repository.QualificationRatingMethodRepository;
import com.betopia.hrm.domain.employee.repository.QualificationTypeRepository;
import com.betopia.hrm.domain.employee.request.EmployeeEducationInfoRequest;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEducationInfoMapper {

    private final EmployeeRepository employeeRepository;
    private final QualificationTypeRepository qualificationTypeRepository;
    private final QualificationLevelRepository qualificationLevelRepository;
    private final InstituteNameRepository instituteNameRepository;
    private final FieldStudyRepository fieldStudyRepository;
    private final QualificationRatingMethodRepository qualificationRatingMethodRepository;

    public EmployeeEducationInfoMapper(EmployeeRepository employeeRepository, QualificationTypeRepository qualificationTypeRepository, QualificationLevelRepository qualificationLevelRepository, InstituteNameRepository instituteNameRepository, FieldStudyRepository fieldStudyRepository, QualificationRatingMethodRepository qualificationRatingMethodRepository) {
        this.employeeRepository = employeeRepository;
        this.qualificationTypeRepository = qualificationTypeRepository;
        this.qualificationLevelRepository = qualificationLevelRepository;
        this.instituteNameRepository = instituteNameRepository;
        this.fieldStudyRepository = fieldStudyRepository;
        this.qualificationRatingMethodRepository = qualificationRatingMethodRepository;
    }


    /**
     * Map CreateRequest DTO -> Entity
     */
    public EmployeeEducationInfo toEntity(EmployeeEducationInfoRequest request) {
        EmployeeEducationInfo employeeEducationInfo = new EmployeeEducationInfo();
        entityDto(employeeEducationInfo, request);
        return employeeEducationInfo;
    }

    /**
     * Update Entity fields from CreateRequest
     */
    public void entityDto(EmployeeEducationInfo employeeEducationInfo, EmployeeEducationInfoRequest request) {
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

    }
}

