package com.betopia.hrm.domain.admin.repository;

import com.betopia.hrm.domain.admin.entity.Country;
import com.betopia.hrm.domain.admin.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository  extends JpaRepository<Location, Long> {

}
