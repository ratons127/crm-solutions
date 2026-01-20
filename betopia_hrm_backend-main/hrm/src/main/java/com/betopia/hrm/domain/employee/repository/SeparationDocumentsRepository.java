package com.betopia.hrm.domain.employee.repository;


import com.betopia.hrm.domain.employee.entity.SeparationDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeparationDocumentsRepository extends JpaRepository<SeparationDocuments, Long> {
}
