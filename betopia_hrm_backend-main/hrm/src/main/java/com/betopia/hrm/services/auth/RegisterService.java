package com.betopia.hrm.services.auth;

import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.request.UserRequest;

public interface RegisterService {

    User register(UserRequest request);
}
