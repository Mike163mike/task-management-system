package com.effectivemobile.taskmanagementsystem.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskUpdateDto {

    @Size(max = 255)
    @Schema(description = "The task title should be less than 255 characters", maxLength = 255)
    private String title;

    @Size(max = 524_288)
    @Schema(description = "The task description should be less than 524 288 characters in length. It can be empty",
            maxLength = 524_288)
    private String description;

    @Schema(description = "Existing status or null")
    @Pattern(regexp = "PENDING|IN_PROGRESS|FINISHED", message = "Status must be PENDING, IN_PROGRESS, or FINISHED")
    private String status;

    @Schema(description = "Existing priority or null")
    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "Priority must be LOW, MEDIUM, or HIGH")
    private String priority;

    @Schema(description = "Existing username or null")
    private String assignee;
}
