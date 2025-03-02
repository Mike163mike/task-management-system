package com.effectivemobile.taskmanagementsystem.dto.user;

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
public class UserCreationDto {

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, max = 150, message = "Username must be between 3 and 150 characters")
    @Schema(description = "The username should be between 3 and 150 characters", minLength = 3, maxLength = 150,
            example = "Morty_Smith")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    @Schema(description = "The password should be between 6 and 255 characters", minLength = 6, maxLength = 255,
            example = "Very_strong_PASSWORD_123")
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    @Size(min = 6, max = 254, message = "Email must be between 6 and 254 characters")
    @Schema(description = "The email should be between 5 and 254 characters", minLength = 6, maxLength = 254,
            example = "Morty_Smith@gmail.com")
    @Email(message = "Invalid email format")
    private String email;
}
