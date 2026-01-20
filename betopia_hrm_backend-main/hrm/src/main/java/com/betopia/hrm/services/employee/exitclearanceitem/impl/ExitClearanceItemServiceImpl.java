package com.betopia.hrm.services.employee.exitclearanceitem.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitClearanceItemDTO;
import com.betopia.hrm.domain.dto.employee.mapper.ExitClearanceItemMapper;
import com.betopia.hrm.domain.employee.entity.ExitClearanceItem;
import com.betopia.hrm.domain.employee.repository.ExitClearanceItemRepository;
import com.betopia.hrm.domain.employee.repository.ExitClearanceTemplateRepository;
import com.betopia.hrm.domain.employee.request.ExitClearanceItemRequest;
import com.betopia.hrm.services.employee.exitclearanceitem.ExitClearanceItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExitClearanceItemServiceImpl implements ExitClearanceItemService {

    private final ExitClearanceItemRepository exitClearanceItemRepository;
    private final ExitClearanceTemplateRepository exitClearanceTemplateRepository;
    private final ExitClearanceItemMapper exitClearanceItemMapper;

    public ExitClearanceItemServiceImpl(ExitClearanceItemRepository exitClearanceItemRepository,
                                        ExitClearanceTemplateRepository exitClearanceTemplateRepository,
                                        ExitClearanceItemMapper exitClearanceItemMapper) {
        this.exitClearanceItemRepository = exitClearanceItemRepository;
        this.exitClearanceTemplateRepository = exitClearanceTemplateRepository;
        this.exitClearanceItemMapper = exitClearanceItemMapper;
    }

    @Override
    public PaginationResponse<ExitClearanceItemDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<ExitClearanceItem> itemPage = exitClearanceItemRepository.findAll(pageable);

        // Get content from page
        List<ExitClearanceItem> exitClearanceItems = itemPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<ExitClearanceItemDTO> itemDTOS = exitClearanceItemMapper.toDTOList(exitClearanceItems);

        // Create pagination response
        PaginationResponse<ExitClearanceItemDTO> response = new PaginationResponse<>();
        response.setData(itemDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All exit clearance items successfully");

        // Set links
        Links links = Links.fromPage(itemPage, "/exit-item");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(itemPage, "/exit-item");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ExitClearanceItemDTO> getAll() {
        List<ExitClearanceItem> exitClearanceItems = exitClearanceItemRepository.findAll();
        return exitClearanceItemMapper.toDTOList(exitClearanceItems);
    }

    @Override
    public ExitClearanceItemDTO store(ExitClearanceItemRequest request) {
        ExitClearanceItem exitClearanceItem = new ExitClearanceItem();

        exitClearanceItem.setTemplate(exitClearanceTemplateRepository.findById(request.templateId())
                .orElseThrow(() -> new RuntimeException("Clearance template id not found")));

        exitClearanceItem.setDepartment(request.department());
        exitClearanceItem.setItemName(request.itemName());
        exitClearanceItem.setItemDescription(request.itemDescription());
        exitClearanceItem.setIsMandatory(request.isMandatory());
        exitClearanceItem.setAssigneeRole(request.assigneeRole());
        exitClearanceItem.setSequenceOrder(request.sequenceOrder());
        exitClearanceItem.setStatus(request.status());

        // Save entity
        ExitClearanceItem savedexitClearanceItem= exitClearanceItemRepository.save(exitClearanceItem);

        // Convert Entity to DTO and return
        return exitClearanceItemMapper.toDTO(savedexitClearanceItem);
    }

    @Override
    public ExitClearanceItemDTO show(Long id) {
        ExitClearanceItem exitClearanceItem = exitClearanceItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance item not found with id: " + id));
        return exitClearanceItemMapper.toDTO(exitClearanceItem);
    }

    @Override
    public ExitClearanceItemDTO update(Long id, ExitClearanceItemRequest request) {
        ExitClearanceItem exitClearanceItem = exitClearanceItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance item not found with id: " + id));


        exitClearanceItem.setTemplate(exitClearanceTemplateRepository.findById(request.templateId())
                .orElseThrow(() -> new RuntimeException("exit clearance template not found")));

        exitClearanceItem.setDepartment(request.department());
        exitClearanceItem.setItemName(request.itemName());
        exitClearanceItem.setItemDescription(request.itemDescription());
        exitClearanceItem.setIsMandatory(request.isMandatory());
        exitClearanceItem.setAssigneeRole(request.assigneeRole());
        exitClearanceItem.setSequenceOrder(request.sequenceOrder());
        exitClearanceItem.setStatus(request.status());

        ExitClearanceItem updateexitClearanceItem = exitClearanceItemRepository.save(exitClearanceItem);

        return exitClearanceItemMapper.toDTO(updateexitClearanceItem);
    }

    @Override
    public void destroy(Long id) {
        ExitClearanceItem exitClearanceItem = exitClearanceItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance item not found"));

        exitClearanceItemRepository.delete(exitClearanceItem);
    }
}
