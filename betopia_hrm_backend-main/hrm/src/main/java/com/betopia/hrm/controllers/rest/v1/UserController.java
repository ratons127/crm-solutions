package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.request.UserRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.users.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/users")
@Tag(name = "User Management -> users", description = "Operations related to managing user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "1. Get all users with pagination", description = "Retrieve all users with pagination")
    //@PreAuthorize("hasAuthority('user-list')")
    public ResponseEntity<PaginationResponse<User>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) String keyword

    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<User> paginationResponse = userService.index(direction, page, perPage,keyword);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all users", description = "Retrieve all users without pagination")
    //@PreAuthorize("hasAuthority('user-list')")
    public ResponseEntity<GlobalResponse> getAllUsers()
    {
        List<User> users = userService.getAllUsers();

        GlobalResponse response = GlobalResponse.success(
                users,
                "All users fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "3. Store user", description = "Creating a new user into the system")
//    @PreAuthorize("hasAuthority('user-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody UserRequest request)
    {
        System.out.println("Request: " + request);

        User createdUser = userService.store(request);

        GlobalResponse response = GlobalResponse.success(
                createdUser,
                "Store successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Gey by id single user", description = "A single user retrieve from database by id")
    //@PreAuthorize("hasAuthority('user-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        User user = userService.show(id);

        GlobalResponse response = GlobalResponse.success(
                user,
                "User fetched successfully",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "5. Update user", description = "Update single user into the system")
    //@PreAuthorize("hasAuthority('user-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody UserRequest request)
    {
        User updatedUser = userService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                updatedUser,
                "Updated successfully",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete user", description = "Remove a single user into the system")
    //@PreAuthorize("hasAuthority('user-delete')")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id)
    {
        userService.destroy(id);

        GlobalResponse response = GlobalResponse.success(
                null,
                "User deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/passwordValidate/{password}")
    @Operation(summary = "1. Validate user password during registration", description = "Retrieve all password policy")
    public ResponseEntity<GlobalResponse> passwordValidate(@PathVariable("password") String  password)
    {
        GlobalResponse response = userService.passwordValidation(password);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<User>> search(@RequestParam(required = false) String keyword) {
//        List<User> users = userService.search(keyword);
//        return ResponseEntity.ok(users);
//    }
}
