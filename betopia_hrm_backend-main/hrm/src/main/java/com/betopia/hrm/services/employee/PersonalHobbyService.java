package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.PersonalHobby;
import com.betopia.hrm.domain.employee.request.PersonalHobbyRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PersonalHobbyService {

    PaginationResponse<PersonalHobby> index(Sort.Direction direction, int page, int perPage);

    List<PersonalHobby> getAllPersonalHobby();

    PersonalHobby store(PersonalHobbyRequest request);

    PersonalHobby show(Long personalHobbyId);

    PersonalHobby update(Long personalHobbyId, PersonalHobbyRequest request);

    void delete(Long personalHobbyId);
}
