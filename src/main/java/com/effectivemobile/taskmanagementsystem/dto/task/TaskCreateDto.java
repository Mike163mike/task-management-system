package com.effectivemobile.taskmanagementsystem.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDto {

    @NotBlank
    @Size(max = 255)
    @Schema(description = "The task title should be less than 255 characters", maxLength = 255)
    private String title;

    @Size(max = 524_288)
    @Schema(description = "The task description should be less than 524 288 characters in length. It can be empty",
            maxLength = 524_288)
    private String description;
}
