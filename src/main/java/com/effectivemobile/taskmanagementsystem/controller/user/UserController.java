package com.effectivemobile.taskmanagementsystem.controller.user;

import com.effectivemobile.taskmanagementsystem.dto.user.UserCreationDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserResponseDto;
import com.effectivemobile.taskmanagementsystem.facade.user.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(APP_PREFIX + "/user")
@AllArgsConstructor
//@SecurityRequirement(name = CoreConstants.Security.AUTHORIZATION)
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/create")
//    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Create new user",
            description = """
                    Create new user
                    """
    )
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreationDto userCreationDto) {
        return ResponseEntity.status(CREATED).body(userFacade.createUser(userCreationDto));
    }

    @GetMapping("/users")
    @Operation(
            summary = "Get page of users",
            description = """
                    Get all users. Returns a paginated list of users. You should specify the page parameters in the query
                    parameters, by default it start with the 0th page and 20 items per page.
                    """
    )
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userFacade.getAllUsers(pageable));
    }

    @PatchMapping("/update/{userId}")
    @Operation(
            summary = "Update user",
            description = """
                    Update user by its ID can users with role 'ASSIGNEE' and 'ADMIN'. User with role 'ASSIGNEE' can
                    update only self account. 'ADMIN' - any.
                    """
    )
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,
                                                      @RequestBody @Valid UserCreationDto userCreationDto) {
        return ResponseEntity.ok(userFacade.updateUser(userId, userCreationDto));
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(
            summary = "Delete user",
            description = """
                    Delete self account by its ID can users with any role: 'USER', 'ASSIGNEE' and 'ADMIN'. User with
                    roles 'USER' and 'ASSIGNEE' can delete only self account. 'ADMIN' - any.
                    """
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userFacade.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
