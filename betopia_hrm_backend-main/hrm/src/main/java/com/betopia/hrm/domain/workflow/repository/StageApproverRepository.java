package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.StageApprover;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageApproverRepository extends JpaRepository<StageApprover, Long> {

    StageApprover findByStageIdAndStatusTrueOrderBySequenceOrderAsc(Long stageId);

}
