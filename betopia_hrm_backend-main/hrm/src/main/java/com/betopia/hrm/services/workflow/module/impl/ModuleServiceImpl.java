package com.betopia.hrm.services.workflow.module.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.ModuleDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.ModuleMapper;
import com.betopia.hrm.domain.workflow.entity.Module;
import com.betopia.hrm.domain.workflow.exceptions.ModuleNotFound;
import com.betopia.hrm.domain.workflow.repository.ModuleRepository;
import com.betopia.hrm.domain.workflow.request.ModuleRequest;
import com.betopia.hrm.services.workflow.module.ModuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Service
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository repository;
    private final ModuleMapper mapper;

    public ModuleServiceImpl(ModuleRepository repository,
                             ModuleMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PaginationResponse<ModuleDTO> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<Module> modulePage = repository.findAll(pageable);

        PaginationResponse<ModuleDTO> response = new PaginationResponse<>();
        response.setData(modulePage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All module fetched successfully");
        response.setLinks(Links.fromPage(modulePage, "/modules"));
        response.setMeta(Meta.fromPage(modulePage, "/modules"));

        return response;
    }

    @Override
    public List<ModuleDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public ModuleDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new ModuleNotFound("Module not found with id: " + id)));
    }

    @Override
    public ModuleDTO store(ModuleRequest request) {
        Module module = new Module();
        module.setModuleName(request.moduleName());
        module.setModuleCode(request.moduleCode());

        return mapper.toDTO(repository.save(module));
    }

    @Override
    public ModuleDTO update(Long id, ModuleRequest request) {

        Module module = repository.findById(id)
                .orElseThrow(() -> new ModuleNotFound("Module not found with id:" + id));

        updateIfNotNull(request.moduleName(), module::setModuleName);
        updateIfNotNull(request.moduleCode(), module::setModuleCode);
        updateIfNotNull(request.status(), module::setStatus);

        module.setLastModifiedDate(LocalDateTime.now());

        return mapper.toDTO(repository.save(module));
    }

    @Override
    public void delete(Long id) {

        Module module = repository.findById(id)
                .orElseThrow(() -> new ModuleNotFound("Module not found with id: + id"));

        repository.delete(module);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
