package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.request.UserRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.services.auth.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Tag(
        name = "User Registration",
        description = "APIs for managing new user registration. "
                + "Allows clients to create user accounts by providing necessary information such as name, email, and password."
)
public class RegisterController {

    private final RegisterService registerService;


    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user account in the system with the provided details. "
                    + "The request body must include required fields such as name, email, and password. "
                    + "If the registration is successful, returns the newly created user information. "
                    + "If the email is already taken or validation fails, an appropriate error response will be returned."
    )
    public ResponseEntity<GlobalResponse> register(@Valid @RequestBody UserRequest request)
    {
        User user = registerService.register(request);

        GlobalResponse response = GlobalResponse.success(
                user,
                "Register successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }
}
