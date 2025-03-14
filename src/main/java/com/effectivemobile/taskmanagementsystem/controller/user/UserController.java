package com.effectivemobile.taskmanagementsystem.controller.user;

import com.effectivemobile.taskmanagementsystem.constant.AppConstant;
import com.effectivemobile.taskmanagementsystem.dto.user.UserCreationDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserUpdateDto;
import com.effectivemobile.taskmanagementsystem.facade.user.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(APP_PREFIX + "/user")
@AllArgsConstructor
@SecurityRequirement(name = AppConstant.AUTHORIZATION)
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/create")
    @Operation(
            summary = "Create new user",
            description = """
                    Everybody can create new user for yourself.
                    """
    )
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreationDto userCreationDto) {
        return ResponseEntity.status(CREATED).body(userFacade.createUser(userCreationDto));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(
            summary = "Get page of users",
            description = """
                    Get all users. Returns a paginated list of users. You should specify the page parameters in the query
                    parameters, by default it start with the 0th page and 20 items per page. Get all users can users with
                    role 'ROLE_ADMIN' only.
                    """
    )
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userFacade.getAllUsers(pageable));
    }

    @PatchMapping("/update/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ASSIGNEE', 'ROLE_ADMIN')")
    @Operation(
            summary = "Update user",
            description = """
                    Update user by its ID can users with role 'ROLE_ASSIGNEE' and 'ROLE_ADMIN'. User with role
                    'ROLE_ASSIGNEE' can update only self account. 'ROLE_ADMIN' - any.
                    """
    )
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,
                                                      @RequestBody @Valid UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userFacade.updateUser(userId, userUpdateDto));
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ASSIGNEE', 'ROLE_ADMIN')")
    @Operation(
            summary = "Delete user",
            description = """
                    Delete self account by its ID can users with any role: 'ROLE_USER', 'ROLE_ASSIGNEE' and 'ROLE_ADMIN'.
                    User with roles 'ROLE_USER' and 'ROLE_ASSIGNEE' can delete only self account. 'ROLE_ADMIN' - any.
                    """
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userFacade.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
