package com.betopia.hrm.domain.users.repository;

import com.betopia.hrm.domain.users.entity.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

    List<Workplace> findByWorkplaceGroupIdOrderByIdDesc(Long workplaceGroupId);

    Optional<Workplace> findFirstByNameIgnoreCase(String name);
}
