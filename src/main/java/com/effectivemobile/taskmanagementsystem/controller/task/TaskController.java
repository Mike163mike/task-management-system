package com.effectivemobile.taskmanagementsystem.controller.task;

import com.effectivemobile.taskmanagementsystem.dto.task.TaskCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskUpdateDto;
import com.effectivemobile.taskmanagementsystem.facade.task.TaskFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.effectivemobile.taskmanagementsystem.constant.AppConstant.APP_PREFIX;

@RestController
@RequestMapping(APP_PREFIX + "/task")
@AllArgsConstructor
//@SecurityRequirement(name = CoreConstants.Security.AUTHORIZATION)
public class TaskController {

    private final TaskFacade taskFacade;

    @PostMapping("/create")
//    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Create new task",
            description = """
                    Create new task
                    """
    )
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid TaskCreateDto taskCreateDto) {
//        return ResponseEntity.ok("All are working");
        return taskFacade.createTask(taskCreateDto);
    }

    @PatchMapping("/update/{taskId}")
    @Operation(
            summary = "Update task",
            description = """
                    Update task by its ID
                    """
    )
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long taskId,
                                                      @RequestBody @Valid TaskUpdateDto taskUpdateDto) {
        return taskFacade.updateTask(taskId, taskUpdateDto);
    }

    @DeleteMapping("/delete/{taskId}")
    @Operation(
            summary = "Delete task",
            description = """
                    Delete task by its ID
                    """
    )
    public ResponseEntity<Void>  deleteTask(@PathVariable Long taskId) {
        return taskFacade.deleteTask(taskId);
    }
}
