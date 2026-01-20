package com.betopia.hrm.services.attendance.shiftCategory.impl;

import com.betopia.hrm.domain.attendance.entity.ShiftCategory;
import com.betopia.hrm.domain.attendance.exception.ShiftCategoryNotFoundException;
import com.betopia.hrm.domain.attendance.repository.ShiftCategoryRepository;
import com.betopia.hrm.domain.attendance.request.ShiftCategoryRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveCategory;
import com.betopia.hrm.services.attendance.shiftCategory.ShiftCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftCategoryServiceImpl implements ShiftCategoryService {

    private final ShiftCategoryRepository shiftCategoryRepository;

    public ShiftCategoryServiceImpl(ShiftCategoryRepository shiftCategoryRepository) {
        this.shiftCategoryRepository = shiftCategoryRepository;
    }

    @Override
    public PaginationResponse<ShiftCategory> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<ShiftCategory> shiftCategoryPage = shiftCategoryRepository.findAll(pageable);

        List<ShiftCategory> shiftCategories = shiftCategoryPage.getContent();

        PaginationResponse<ShiftCategory> response = new PaginationResponse<>();
        response.setData(shiftCategories);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All shift categories fetched successfully");

        Links links = Links.fromPage(shiftCategoryPage, "/shift-categories");
        response.setLinks(links);

        Meta meta = Meta.fromPage(shiftCategoryPage, "/shift-categories");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ShiftCategory> getAll() {
        return shiftCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public ShiftCategory store(ShiftCategoryRequest request) {
        ShiftCategory shiftCategory = new ShiftCategory();

        shiftCategory.setName(request.name());
        shiftCategory.setType(request.type());
        shiftCategory.setDescription(request.description());
        shiftCategory.setStatus(request.status() != null ? request.status() : true);

        shiftCategory = shiftCategoryRepository.save(shiftCategory);
        return shiftCategory;
    }

    @Override
    public ShiftCategory show(Long aLong) {
        ShiftCategory shiftCategory = shiftCategoryRepository.findById(aLong).orElseThrow(() -> new ShiftCategoryNotFoundException("Shift category not found with id: " + aLong));
        return shiftCategory;
    }

    @Override
    public ShiftCategory update(Long aLong, ShiftCategoryRequest request) {
        ShiftCategory shiftCategory = shiftCategoryRepository.findById(aLong).orElseThrow(() -> new ShiftCategoryNotFoundException("Shift category not found with id: " + aLong));

        shiftCategory.setName(request.name() != null ? request.name() : shiftCategory.getName());
        shiftCategory.setType(request.type() != null ? request.type() : shiftCategory.getType());
        shiftCategory.setDescription(request.description() != null ? request.description() : shiftCategory.getDescription());
        shiftCategory.setStatus(request.status() != null ? request.status() : shiftCategory.getStatus());

        shiftCategory = shiftCategoryRepository.save(shiftCategory);
        return shiftCategory;
    }

    @Override
    public void destroy(Long aLong) {
        ShiftCategory shiftCategory = shiftCategoryRepository.findById(aLong).orElseThrow(() ->
                new ShiftCategoryNotFoundException("Shift category not found with id: " + aLong));
        shiftCategoryRepository.delete(shiftCategory);
    }
}
