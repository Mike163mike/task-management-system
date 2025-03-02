package com.effectivemobile.taskmanagementsystem.facade.task;

import com.effectivemobile.taskmanagementsystem.dto.task.TaskCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskFilterDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskFacade {

    TaskResponseDto createTask(TaskCreateDto taskCreateDto);

    TaskResponseDto updateTask(Long taskId, TaskUpdateDto taskUpdateDto);

    void deleteTask(Long taskId);

    Page<TaskResponseDto> getAllTasksByActor(TaskFilterDto taskFilterDto, Pageable pageable);
}
