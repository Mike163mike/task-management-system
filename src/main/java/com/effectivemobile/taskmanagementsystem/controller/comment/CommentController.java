package com.effectivemobile.taskmanagementsystem.controller.comment;

import com.effectivemobile.taskmanagementsystem.dto.comment.CommentCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.comment.CommentUpdateDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskFilterDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskUpdateDto;
import com.effectivemobile.taskmanagementsystem.facade.comment.CommentFacade;
import com.effectivemobile.taskmanagementsystem.facade.task.TaskFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(APP_PREFIX + "/comment")
@AllArgsConstructor
@Validated
//@SecurityRequirement(name = CoreConstants.Security.AUTHORIZATION)
public class CommentController {

    private final CommentFacade commentFacade;

    @PostMapping("/create")
//    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Create new comment",
            description = """
                    A new comment can be created by users with role 'ASSIGNEE' and 'ADMIN'
                    """
    )
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentCreateDto commentCreateDto) {
        return ResponseEntity.status(CREATED).body(commentFacade.createComment(commentCreateDto));
    }

    @GetMapping("/comments/{taskId}")
    @Operation(
            summary = "Get page of comments",
            description = """
                    Get all comments related to a specific task by its ID. The endpoint returns a paginated list of
                    comments. You can specify the page parameters, with the default being to start on the 0th page with
                    20 items per page.
                    """
    )
    public ResponseEntity<Page<CommentResponseDto>> getAllCommentsByTask(@PathVariable Long taskId,
                                                                         @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(commentFacade.getAllCommentsByTask(taskId, pageable));
    }

    @PatchMapping("/update/{commentId}")
    @Operation(
            summary = "Update comment",
            description = """
                    Update comment by its ID can users with role 'ASSIGNEE' and 'ADMIN'. User with role 'ASSIGNEE' can
                    update only self comments. 'ADMIN' - any.
                    """
    )
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                            @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        return ResponseEntity.ok(commentFacade.updateComment(commentId, commentUpdateDto));
    }

    @DeleteMapping("/delete/{commentId}")
    @Operation(
            summary = "Delete comment",
            description = """
                    Delete comment by its ID can users with role 'ASSIGNEE' and 'ADMIN'. User with role 'ASSIGNEE' can
                    delete only self commit. 'ADMIN' - any.
                    """
    )
    public ResponseEntity<Void> deleteCommit(@PathVariable Long commentId) {
        commentFacade.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
