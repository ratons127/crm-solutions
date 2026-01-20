package com.betopia.hrm.services.employee.noticeperiodconfig.impl;


import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.NoticePeriodConfigDTO;
import com.betopia.hrm.domain.dto.employee.mapper.NoticePeriodConfigMapper;
import com.betopia.hrm.domain.employee.entity.NoticePeriodConfig;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.repository.NoticePeriodConfigRepository;
import com.betopia.hrm.domain.employee.request.NoticePeriodConfigRequest;
import com.betopia.hrm.services.employee.noticeperiodconfig.NoticePeriodConfigService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoticePeriodConfigServiceImpl implements NoticePeriodConfigService {

    private final NoticePeriodConfigRepository noticePeriodConfigRepository;
    private final EmployeeSeparationsRepository employeeSeparationsRepository;
    private final NoticePeriodConfigMapper noticePeriodConfigMapper;

    public NoticePeriodConfigServiceImpl(NoticePeriodConfigRepository noticePeriodConfigRepository,
                                         EmployeeSeparationsRepository employeeSeparationsRepository,
                                         NoticePeriodConfigMapper  noticePeriodConfigMapper) {

        this.noticePeriodConfigRepository = noticePeriodConfigRepository;
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.noticePeriodConfigMapper = noticePeriodConfigMapper;
    }


    @Override
    public PaginationResponse<NoticePeriodConfigDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<NoticePeriodConfig> noticePage = noticePeriodConfigRepository.findAll(pageable);

        // Get content from page
        List<NoticePeriodConfig> shifts = noticePage.getContent();

        // Convert entities to DTOs using MapStruct
        List<NoticePeriodConfigDTO> noticeDTOS = noticePeriodConfigMapper.toDTOList(shifts);

        // Create pagination response
        PaginationResponse<NoticePeriodConfigDTO> response = new PaginationResponse<>();
        response.setData(noticeDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All notice period successfully");

        // Set links
        Links links = Links.fromPage(noticePage, "/notice-period");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(noticePage, "/notice-period");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<NoticePeriodConfigDTO> getAllNoticePeriodConfigs() {
        List<NoticePeriodConfig> notices = noticePeriodConfigRepository.findAll();
        return noticePeriodConfigMapper.toDTOList(notices);
    }

    @Override
    public NoticePeriodConfigDTO store(NoticePeriodConfigRequest request) {
        NoticePeriodConfig noticePeriodConfig = new NoticePeriodConfig();


        noticePeriodConfig.setEmployeeSeparation(employeeSeparationsRepository.findById(request.employeeSeparationId())
                .orElseThrow(() -> new RuntimeException("Employee Separation id not found")));

        noticePeriodConfig.setDefaultNoticeDays(request.defaultNoticeDays());
        noticePeriodConfig.setMinimumNoticeDays(request.minimumNoticeDays());
        noticePeriodConfig.setGracePeriodDays(request.gracePeriodDays());
        noticePeriodConfig.setCanWaive(request.canWaive());
        noticePeriodConfig.setCanBuyout(request.canBuyout());
        noticePeriodConfig.setStatus(request.status());

        // Save entity
        NoticePeriodConfig savedNoticePeriod = noticePeriodConfigRepository.save(noticePeriodConfig);

        // Convert Entity to DTO and return
        return noticePeriodConfigMapper.toDTO(savedNoticePeriod);
    }

    @Override
    public NoticePeriodConfigDTO show(Long id) {
        NoticePeriodConfig noticePeriodConfig = noticePeriodConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice period not found with id: " + id));
        return noticePeriodConfigMapper.toDTO(noticePeriodConfig);
    }

    @Override
    public NoticePeriodConfigDTO update(Long id, NoticePeriodConfigRequest request) {
        NoticePeriodConfig noticePeriodConfig = noticePeriodConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice period not found with id: " + id));


        noticePeriodConfig.setEmployeeSeparation(employeeSeparationsRepository.findById(request.employeeSeparationId())
                .orElseThrow(() -> new RuntimeException("Employee Separation not found")));

        noticePeriodConfig.setDefaultNoticeDays(request.defaultNoticeDays());
        noticePeriodConfig.setMinimumNoticeDays(request.minimumNoticeDays());
        noticePeriodConfig.setGracePeriodDays(request.gracePeriodDays());
        noticePeriodConfig.setCanWaive(request.canWaive());
        noticePeriodConfig.setCanBuyout(request.canBuyout());
        noticePeriodConfig.setStatus(request.status());

        NoticePeriodConfig savedNoticePeriod = noticePeriodConfigRepository.save(noticePeriodConfig);

        return noticePeriodConfigMapper.toDTO(savedNoticePeriod);
    }

    @Override
    public void destroy(Long id) {
        NoticePeriodConfig noticePeriodConfig = noticePeriodConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice period not found"));

        noticePeriodConfigRepository.delete(noticePeriodConfig);
    }
}
