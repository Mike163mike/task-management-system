package com.effectivemobile.taskmanagementsystem.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {

    @NotBlank
    @Size(min = 1, max = 524_288)
    @Schema(description = "The field 'text' should be between 1 and 524 288 characters in length. It can't be empty",
            minLength = 1, maxLength = 524_288, example = "This is a comment text.")
    private String text;

    @NotNull(message = "Task ID can't be null")
    @Schema(description = "ID of the task this comment belongs to", example = "12345")
    private Long taskId;
}
