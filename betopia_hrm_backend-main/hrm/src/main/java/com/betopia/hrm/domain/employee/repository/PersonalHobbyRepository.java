package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.PersonalHobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalHobbyRepository extends JpaRepository<PersonalHobby, Long> {

}
