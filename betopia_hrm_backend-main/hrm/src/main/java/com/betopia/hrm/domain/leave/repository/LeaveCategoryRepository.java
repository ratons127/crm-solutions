package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.leave.entity.LeaveCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveCategoryRepository extends JpaRepository<LeaveCategory, Long> {

    Page<LeaveCategory> findByParentIsNull(Pageable pageable);

    List<LeaveCategory> findByStatus(Boolean status);
}
