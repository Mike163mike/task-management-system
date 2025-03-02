package com.effectivemobile.taskmanagementsystem.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class TaskFilterDto {

    @Schema(description = "The username of the assignee. Only one from two! Remember?", example = "Morty_Smith")
    private String assigneeUsername;

    @Schema(description = "The username of the creator. Only one from two! Remember?", example = "Rick_Sanchez")
    private String creatorUsername;

//    @Min(0)
//    @Schema(description = "Page number (0-based)", example = "0")
//    private Integer page = 0;
//
//    @Min(1)
//    @Max(100)
//    @Schema(description = "Page size", example = "10")
//    private Integer size = 10;

    @AssertTrue(message = "Exactly one of assigneeUsername or creatorUsername must be provided")
    public boolean isValid() {
        return (assigneeUsername == null) ^ (creatorUsername == null);  // Либо одно поле null, либо другое
    }
}
