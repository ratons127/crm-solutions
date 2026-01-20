package com.betopia.hrm.services.company.team.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.exception.DepartmentNotFoundException;
import com.betopia.hrm.domain.company.exception.TeamDeleteNotAllowedException;
import com.betopia.hrm.domain.company.exception.TeamNotFoundException;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.company.repository.TeamRepository;
import com.betopia.hrm.domain.company.request.TeamRequest;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.users.entity.Permission;
import com.betopia.hrm.services.company.team.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    public TeamServiceImpl(TeamRepository teamRepository, DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.teamRepository = teamRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public PaginationResponse<Team> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<Team> teamPage = teamRepository.findAll(pageable);

        List<Team> teams = teamPage.getContent();

        PaginationResponse<Team> response = new PaginationResponse<>();

        response.setData(teams);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All teams fetch successful");

        Links links = Links.fromPage(teamPage, "/teams");
        response.setLinks(links);

        Meta meta = Meta.fromPage(teamPage, "/teams");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<Team> getAllTeams() {

        return teamRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Team store(TeamRequest request) {
        Team team = new Team();

        Department department = departmentRepository.findById(request.departmentId()).orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.departmentId()));

        team.setDepartment(department);
        team.setName(request.name());
        team.setCode(request.code());
        team.setDescription(request.description());
        team.setStatus(request.status());

        team = teamRepository.save(team);

        return team;
    }

    @Override
    public Team edit(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException("Team not found"));

        return team;
    }

    @Override
    public Team update(Long teamId, TeamRequest request) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException("Team not found"));

        Department department = departmentRepository.findById(request.departmentId()).orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.departmentId()));
        team.setDepartment(department != null ? department : team.getDepartment());
        team.setName(request.name() != null ? request.name() : team.getName());
        team.setCode(request.code() != null ? request.code() : team.getCode());
        team.setDescription(request.description() != null ? request.description() : team.getDescription());
        team.setStatus(request.status() != null ? request.status() : team.getStatus());

        team = teamRepository.save(team);

        return team;
    }

    @Override
    public void destroy(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException("Team not found"));

//        boolean hasEmployees = employeeRepository.existsByTeam(team);
//
//        if (hasEmployees) {
//            throw new TeamDeleteNotAllowedException(
//                    "Team with id " + teamId + " cannot be deleted because it has assigned employees.");
//        }

        teamRepository.delete(team);
    }
}
