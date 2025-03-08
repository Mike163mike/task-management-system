package com.effectivemobile.taskmanagementsystem.facade.task.impl;

import com.effectivemobile.taskmanagementsystem.dto.task.TaskCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskFilterDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskUpdateDto;
import com.effectivemobile.taskmanagementsystem.facade.task.TaskFacade;
import com.effectivemobile.taskmanagementsystem.mapper.task.TaskMapper;
import com.effectivemobile.taskmanagementsystem.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskFacadeImpl implements TaskFacade {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @Override
    public TaskResponseDto createTask(TaskCreateDto taskCreateDto) {
        return taskMapper.toResponseDto(taskService.createTask(taskMapper.toEntity(taskCreateDto)));
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, TaskUpdateDto taskUpdateDto) {
        return taskMapper.toResponseDto(taskService.updateTask(taskId, taskMapper.toEntity(taskUpdateDto)));
    }

    @Override
    public void deleteTask(Long taskId) {
        taskService.deleteTask(taskId);
    }

    @Override
    public Page<TaskResponseDto> getAllTasksByActor(TaskFilterDto taskFilterDto, Pageable pageable) {
        String creatorEmail = taskFilterDto.getCreatorEmail();
        String assigneeEmail = taskFilterDto.getAssigneeEmail();

        if (creatorEmail != null) {
            return taskMapper.toPageResponseDto(taskService.getAllTasksByCreator(creatorEmail, pageable));
        } else {
            return taskMapper.toPageResponseDto(taskService.getAllTasksByAssignee(assigneeEmail, pageable));
        }
    }
}
