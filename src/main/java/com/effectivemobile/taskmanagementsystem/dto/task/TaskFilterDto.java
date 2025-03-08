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

    @Schema(description = "The email of the assignee. Only one from two! Remember?", example = "morty_smith@gmail.com")
    private String assigneeEmail;

    @Schema(description = "The email of the creator. Only one from two! Remember?", example = "rick_sanchez@gmail.com")
    private String creatorEmail;

    @AssertTrue(message = "Exactly one of assigneeEmail or creatorEmail must be provided")
    public boolean isValid() {
        return (assigneeEmail == null) ^ (creatorEmail == null);  // Либо одно поле null, либо другое
    }
}
