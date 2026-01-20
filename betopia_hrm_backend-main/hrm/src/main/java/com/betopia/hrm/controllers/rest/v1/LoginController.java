package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.auth.login.ChangePasswordRequest;
import com.betopia.hrm.domain.auth.login.LoginRequest;
import com.betopia.hrm.domain.auth.login.ForgetPasswordRequest;
import com.betopia.hrm.domain.auth.login.ResetPasswordRequestBody;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.users.request.TokenRequest;
import com.betopia.hrm.domain.base.response.AuthResponse;
import com.betopia.hrm.services.auth.LoginService;
import com.betopia.hrm.services.auth.PasswordResetService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1")
@Tag(
        name = "Authentication",
        description = "APIs for user authentication and session management. "
                + "Includes operations for login, token refresh, and logout."
)

public class LoginController {

    private final LoginService loginService;

    private final PasswordResetService passwordResetService;


    public LoginController(LoginService loginService, PasswordResetService passwordResetService) {
        this.loginService = loginService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/auth/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user with provided credentials (e.g., email/username and password). "
                    + "If the credentials are valid, returns an access token and a refresh token for session management. "
                    + "Use the access token to access protected resources."
    )
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(loginService.login(request));
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token. "
                    + "This endpoint allows clients to maintain an active session without requiring the user to log in again. "
                    + "If the refresh token is invalid or expired, a 401 Unauthorized response will be returned."
    )
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(loginService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "User logout",
            description = "Logs out the currently authenticated user by invalidating the access token and refresh token. "
                    + "After logout, the user must log in again to obtain a new access token."
    )
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logout successful. Remove token on client side.");
    }

    @PostMapping("/auth/forgot-password")
    @Operation(
            summary = "Forgot Password",
            description = "Generate a reset token and send reset password link to user email"
    )
    public ResponseEntity<GlobalResponse> forgotPassword(
            @Valid @RequestBody ForgetPasswordRequest request
    ) {
        try {
            passwordResetService.forgotPassword(request);
            return ResponseBuilder.ok(
                    HttpStatus.OK, // no data
                    "Reset password link sent successfully to your email"
            );
        } catch (RuntimeException ex) {
            return ResponseBuilder.error(
                    ex.getMessage(),
                    HttpStatus.valueOf(400)
            );
        }
    }


    @PostMapping("/auth/reset-password")
    @Operation(summary = "Reset Password", description = "Reset user password using token")
    public ResponseEntity<GlobalResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequestBody request
    ) {
        passwordResetService.resetPassword(request);

        return ResponseBuilder.ok(null, "Password reset successfully");
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change Password", description = "Change password for the logged-in user")
    public ResponseEntity<GlobalResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication // Spring Security will inject current user
    ) {
        passwordResetService.changePassword(request, authentication);
        return ResponseBuilder.ok(null, "Password changed successfully");
    }

}
