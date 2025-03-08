package com.effectivemobile.taskmanagementsystem.controller.task;

import com.effectivemobile.taskmanagementsystem.constant.AppConstant;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskFilterDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskUpdateDto;
import com.effectivemobile.taskmanagementsystem.facade.task.TaskFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(APP_PREFIX + "/task")
@AllArgsConstructor
@Validated
@SecurityRequirement(name = AppConstant.AUTHORIZATION)
public class TaskController {

    private final TaskFacade taskFacade;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ASSIGNEE', 'ROLE_ADMIN')")
    @Operation(
            summary = "Create new task",
            description = """
                    A new task is created by users with role 'ASSIGNEE' and 'ADMIN'. The new task created with status
                    'PENDING' and priority 'LOW'. It got creator as its creator-field.
                    """
    )
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid TaskCreateDto taskCreateDto) {
        return ResponseEntity.status(CREATED).body(taskFacade.createTask(taskCreateDto));
    }

    @GetMapping("/tasks")
    @PreAuthorize("hasAnyRole('ROLE_ASSIGNEE', 'ROLE_ADMIN')")
    @Operation(
            summary = "Get page of tasks",
            description = """
                    Get all tasks by the creator's email or the assignee's email. Returns a paginated list of
                    tasks. In the query, you should set only one of these email - the creator's or the assignee's -
                    and not both. Also, you should specify the page parameters - by default, start with the 0th page and
                    20 items per page.
                    """
    )
    public ResponseEntity<Page<TaskResponseDto>> getAllTasksByActor(@Valid @ModelAttribute TaskFilterDto taskFilterDto,
                                                                    @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(taskFacade.getAllTasksByActor(taskFilterDto, pageable));
    }

    @PatchMapping("/update/{taskId}")
    @PreAuthorize("hasAnyRole('ROLE_ASSIGNEE', 'ROLE_ADMIN')")
    @Operation(
            summary = "Update task",
            description = """
                    Update task by its ID can users with role 'ASSIGNEE' and 'ADMIN'. User with role 'ASSIGNEE' can
                    update only self tasks. 'ADMIN' - any.
                    """
    )
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long taskId,
                                                      @RequestBody @Valid TaskUpdateDto taskUpdateDto) {
        return ResponseEntity.ok(taskFacade.updateTask(taskId, taskUpdateDto));
    }

    @DeleteMapping("/delete/{taskId}")
    @PreAuthorize("hasAnyRole('ROLE_ASSIGNEE', 'ROLE_ADMIN')")
    @Operation(
            summary = "Delete task",
            description = """
                    Delete task by its ID can users with role 'ASSIGNEE' and 'ADMIN'. User with role 'ASSIGNEE' can
                    delete only self tasks. 'ADMIN' - any.
                    """
    )
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskFacade.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
