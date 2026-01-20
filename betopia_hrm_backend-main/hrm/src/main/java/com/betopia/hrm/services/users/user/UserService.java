package com.betopia.hrm.services.users.user;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.request.UserRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {

    PaginationResponse<User> index(Sort.Direction direction, int page, int perPage, String keyword);

    List<User> getAllUsers();

    User store(UserRequest request);

    User show(Long userId);

    User update(Long userId, UserRequest request);

    void destroy(Long userId);

    GlobalResponse passwordValidation(String password);



}
