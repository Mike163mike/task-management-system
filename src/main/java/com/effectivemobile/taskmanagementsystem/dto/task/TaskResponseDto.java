package com.effectivemobile.taskmanagementsystem.dto.task;

import com.effectivemobile.taskmanagementsystem.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {

    private Long id;

    private OffsetDateTime createDate;

    private OffsetDateTime changeDate;

    private String title;

    private String description;

    private String status;

    private String priority;

    private List<Comment> comments = new ArrayList<>();

    private String creator;

    private String assignee;
}
