package com.betopia.hrm.services.auth;

import com.betopia.hrm.domain.auth.login.ChangePasswordRequest;
import com.betopia.hrm.domain.auth.login.ForgetPasswordRequest;
import com.betopia.hrm.domain.auth.login.ResetPasswordRequestBody;
import org.springframework.security.core.Authentication;

public interface PasswordResetService {

    Object forgotPassword(ForgetPasswordRequest request);

    void resetPassword(ResetPasswordRequestBody request);

    void changePassword(ChangePasswordRequest request, Authentication authentication);
}
