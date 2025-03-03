package com.effectivemobile.taskmanagementsystem.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;

    private OffsetDateTime createDate;

    private OffsetDateTime changeDate;

    private String text;

    private Long taskId;

    private Long userId;
}
