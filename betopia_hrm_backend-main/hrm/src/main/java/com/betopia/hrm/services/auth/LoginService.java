package com.betopia.hrm.services.auth;

import com.betopia.hrm.domain.auth.login.LoginRequest;
import com.betopia.hrm.domain.users.request.TokenRequest;
import com.betopia.hrm.domain.base.response.AuthResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginService {

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(TokenRequest request);
}
