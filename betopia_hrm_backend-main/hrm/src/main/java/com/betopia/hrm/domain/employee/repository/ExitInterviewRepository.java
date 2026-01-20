package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.ExitInterview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitInterviewRepository extends JpaRepository<ExitInterview, Long> {


}
