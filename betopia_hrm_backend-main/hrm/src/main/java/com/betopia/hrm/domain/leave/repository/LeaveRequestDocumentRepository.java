package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.leave.entity.LeaveRequestDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestDocumentRepository extends JpaRepository<LeaveRequestDocument, Long> {
}
