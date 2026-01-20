package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.PersonalHobby;
import com.betopia.hrm.domain.employee.exception.personalHobby.PersonalHobbyNotFoundException;
import com.betopia.hrm.domain.employee.repository.PersonalHobbyRepository;
import com.betopia.hrm.domain.employee.request.PersonalHobbyRequest;
import com.betopia.hrm.services.employee.PersonalHobbyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PersonalHobbyServiceImpl implements PersonalHobbyService {

    private final PersonalHobbyRepository personalHobbyRepository;

    public PersonalHobbyServiceImpl(PersonalHobbyRepository personalHobbyRepository) {
        this.personalHobbyRepository = personalHobbyRepository;
    }

    @Override
    public PaginationResponse<PersonalHobby> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<PersonalHobby> personalHobbyPage = personalHobbyRepository.findAll(pageable);

        List<PersonalHobby> personalHobbies = personalHobbyPage.getContent();

        PaginationResponse<PersonalHobby> response = new PaginationResponse<>();

        response.setData(personalHobbies);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All PersonalHobby fetch successful");

        Links links = Links.fromPage(personalHobbyPage, "/personalHobbies");
        response.setLinks(links);

        Meta meta = Meta.fromPage(personalHobbyPage, "/personalHobbies");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<PersonalHobby> getAllPersonalHobby() {
       List<PersonalHobby> personalHobbies = personalHobbyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
       if (personalHobbies.isEmpty()) {
           throw new ResourceNotFoundException("No personal hobbies found");
       }
        return personalHobbies;
    }

    @Override
    public PersonalHobby store(PersonalHobbyRequest request) {
        PersonalHobby personalHobby=new PersonalHobby();
        personalHobby.setName(request.name());
        personalHobbyRepository.save(personalHobby);
        return personalHobby;
    }

    @Override
    public PersonalHobby show(Long personalHobbyId) {
        PersonalHobby personalHobby=personalHobbyRepository.findById(personalHobbyId)
                .orElseThrow(()-> new PersonalHobbyNotFoundException("No personal hobby found with id: " + personalHobbyId));

        return personalHobby;
    }

    @Override
    public PersonalHobby update(Long personalHobbyId, PersonalHobbyRequest request) {
        PersonalHobby personalHobby= personalHobbyRepository.findById(personalHobbyId)
                .orElseThrow(()-> new  PersonalHobbyNotFoundException("No personal hobby found with id: " + personalHobbyId));

        personalHobby.setName(request.name());
        personalHobby.setLastModifiedDate(LocalDateTime.now());
        personalHobbyRepository.save(personalHobby);
        return personalHobby;
    }

    @Override
    public void delete(Long personalHobbyId) {
        PersonalHobby personalHobby= personalHobbyRepository.findById(personalHobbyId)
                .orElseThrow(()-> new  RuntimeException("No personal hobby found with id: " + personalHobbyId));
        personalHobbyRepository.deleteById(personalHobbyId);
    }
}
