package com.betopia.hrm.services.employee.exitinterview.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitInterviewDTO;
import com.betopia.hrm.domain.dto.employee.mapper.ExitInterviewMapper;
import com.betopia.hrm.domain.employee.entity.ExitInterview;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.repository.ExitInterviewRepository;
import com.betopia.hrm.domain.employee.request.ExitInterviewRequest;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.exitinterview.ExitInterviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExitInterviewServiceImpl implements ExitInterviewService {

    private final ExitInterviewRepository exitInterviewRepository;
    private final EmployeeSeparationsRepository employeeSeparationsRepository;
    private final UserRepository userRepository;
    private final ExitInterviewMapper exitInterviewMapper;

    public ExitInterviewServiceImpl(ExitInterviewRepository exitInterviewRepository,
                                    EmployeeSeparationsRepository employeeSeparationsRepository,
                                    UserRepository userRepository, ExitInterviewMapper exitInterviewMapper){
        this.exitInterviewRepository = exitInterviewRepository;
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.userRepository = userRepository;
        this.exitInterviewMapper = exitInterviewMapper;
    }

    @Override
    public PaginationResponse<ExitInterviewDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<ExitInterview> interviewPage = exitInterviewRepository.findAll(pageable);

        // Get content from page
        List<ExitInterview>  exitInterviews = interviewPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<ExitInterviewDTO> exitInterviewsDTOS = exitInterviewMapper.toDTOList(exitInterviews);

        // Create pagination response
        PaginationResponse<ExitInterviewDTO> response = new PaginationResponse<>();
        response.setData(exitInterviewsDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All exit interview successfully");

        // Set links
        Links links = Links.fromPage(interviewPage, "/exit-interview");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(interviewPage, "/exit-interview");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ExitInterviewDTO> getAll() {
        List<ExitInterview> accessRevocationLogs = exitInterviewRepository.findAll();
        return exitInterviewMapper.toDTOList(accessRevocationLogs);
    }

    @Override
    public ExitInterviewDTO store(ExitInterviewRequest request) {

        ExitInterview exitInterview = new ExitInterview();

        exitInterview.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found with id: " + request.separationId())));

        exitInterview.setInterviewer(userRepository.findById(request.interviewerId())
                .orElseThrow(() -> new RuntimeException("Interviewer not found with id: " + request.interviewerId())));


        exitInterview.setInterviewMode(request.interviewMode());
        exitInterview.setScheduledDate(request.scheduledDate());
        exitInterview.setCompletedDate(request.completedDate());
        exitInterview.setOverallSatisfactionRating(request.overallSatisfactionRating());
        exitInterview.setWorkEnvironmentRating(request.workEnvironmentRating());
        exitInterview.setManagementRating(request.managementRating());
        exitInterview.setGrowthOpportunityRating(request.growthOpportunityRating());
        exitInterview.setCompensationRating(request.compensationRating());
        exitInterview.setPrimaryReasonLeaving(request.primaryReasonLeaving());
        exitInterview.setWouldRecommend(request.wouldRecommend());
        exitInterview.setWouldRejoin(request.wouldRejoin());
        exitInterview.setAdditionalComments(request.additionalComments());
        exitInterview.setExitStatus(request.exitStatus());
        exitInterview.setConfidentialityLevel(request.confidentialityLevel());
        // Convert interviewData Map to JSON string
        exitInterview.setInterviewData(request.interviewData());


        // Save entity
        ExitInterview saved = exitInterviewRepository.save(exitInterview);

        // Convert entity to DTO
        return exitInterviewMapper.toDTO(saved);
    }

    @Override
    public ExitInterviewDTO show(Long id) {
        ExitInterview exitInterview = exitInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit interview not found with id: " + id));
        return exitInterviewMapper.toDTO(exitInterview);
    }

    @Override
    public ExitInterviewDTO update(Long id, ExitInterviewRequest request) {
        ExitInterview exitInterview = exitInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exit Interview not found with id: " + id));

        exitInterview.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found with id: " + request.separationId())));

        exitInterview.setInterviewer(userRepository.findById(request.interviewerId())
                .orElseThrow(() -> new RuntimeException("Interviewer not found with id: " + request.interviewerId())));


        exitInterview.setInterviewMode(request.interviewMode());
        exitInterview.setScheduledDate(request.scheduledDate());
        exitInterview.setCompletedDate(request.completedDate());
        exitInterview.setOverallSatisfactionRating(request.overallSatisfactionRating());
        exitInterview.setWorkEnvironmentRating(request.workEnvironmentRating());
        exitInterview.setManagementRating(request.managementRating());
        exitInterview.setGrowthOpportunityRating(request.growthOpportunityRating());
        exitInterview.setCompensationRating(request.compensationRating());
        exitInterview.setPrimaryReasonLeaving(request.primaryReasonLeaving());
        exitInterview.setWouldRecommend(request.wouldRecommend());
        exitInterview.setWouldRejoin(request.wouldRejoin());
        exitInterview.setAdditionalComments(request.additionalComments());
        exitInterview.setExitStatus(request.exitStatus());
        exitInterview.setConfidentialityLevel(request.confidentialityLevel());
        exitInterview.setInterviewData(request.interviewData());


        // Save entity
        ExitInterview saved = exitInterviewRepository.save(exitInterview);

        // Convert entity to DTO
        return exitInterviewMapper.toDTO(saved);
    }

    @Override
    public void destroy(Long id) {
        ExitInterview exitInterview = exitInterviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit interview not found"));

        exitInterviewRepository.delete(exitInterview);
    }
}
