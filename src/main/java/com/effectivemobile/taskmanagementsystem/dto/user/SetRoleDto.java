package com.effectivemobile.taskmanagementsystem.dto.user;

import com.effectivemobile.taskmanagementsystem.validator.ValidRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetRoleDto {

    @NotEmpty(message = "Email cannot be empty")
    @Size(min = 6, max = 254, message = "Email must be between 6 and 254 characters")
    @Schema(description = "The email should be between 5 and 254 characters", minLength = 6, maxLength = 254,
            example = "morty_smith@gmail.com")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Role cannot be empty")
    @Schema(description = "The roles can be 'ROLE_USER', 'ROLE_ASSIGNEE', 'ROLE_ADMIN'",
            example = "ROLE_ASSIGNEE")
    @ValidRole(message = "Invalid role. Allowed values are: 'ROLE_USER', 'ROLE_ASSIGNEE', 'ROLE_ADMIN'")
    private String role;
}
