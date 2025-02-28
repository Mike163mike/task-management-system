package com.effectivemobile.taskmanagementsystem.facade.task;

import com.effectivemobile.taskmanagementsystem.dto.task.TaskCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskUpdateDto;
import org.springframework.http.ResponseEntity;

public interface TaskFacade {
    ResponseEntity<TaskResponseDto> createTask(TaskCreateDto taskCreateDto);

    ResponseEntity<TaskResponseDto> updateTask(Long taskId, TaskUpdateDto taskUpdateDto);

    ResponseEntity<Void> deleteTask(Long taskId);
}
