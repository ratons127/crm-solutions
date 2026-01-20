package com.betopia.hrm.services.base;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BaseService<T, R, ID> {

    /**
     * Get paginated list with sorting
     *
     * @param direction Sort direction (ASC/DESC)
     * @param page Page number (0-based)
     * @param perPage Items per page
     * @return Paginated response
     */
    PaginationResponse<T> index(Sort.Direction direction, int page, int perPage);

    /**
     * Get all records without pagination
     *
     * @return List of all entities
     */
    List<T> getAll();

    /**
     * Create new record
     *
     * @param request Request DTO
     * @return Created entity
     */
    T store(R request);

    /**
     * Get single record by ID with additional details
     *
     * @param id Entity ID
     * @return Map containing entity and related data
     */
    T show(ID id);

    /**
     * Update existing record
     *
     * @param id Entity ID
     * @param request Request DTO
     * @return Updated entity
     */
    T update(ID id, R request);

    /**
     * Delete record (soft or hard delete)
     *
     * @param id Entity ID
     */
    void destroy(ID id);
}
