package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {
}
