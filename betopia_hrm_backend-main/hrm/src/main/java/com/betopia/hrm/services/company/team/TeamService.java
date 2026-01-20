package com.betopia.hrm.services.company.team;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.request.TeamRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TeamService {

    PaginationResponse<Team> index(Sort.Direction direction, int page, int perPage);

    List<Team> getAllTeams();

    Team store(TeamRequest request);

    Team edit(Long teamId);

    Team update(Long teamId, TeamRequest request);

    void destroy(Long teamId);
}
