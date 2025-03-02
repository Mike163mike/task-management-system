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
        return taskMapper.toResponseDto(taskService.updateTask(taskId, taskMapper.toEntity(taskUpdateDto)));//todo вернуть 200 статус OK и DTO с ID в теле
    }

    @Override
    public void deleteTask(Long taskId) {
        taskService.deleteTask(taskId);
    }

    @Override
    public Page<TaskResponseDto> getAllTasksByActor(TaskFilterDto taskFilterDto, Pageable pageable) {
        String creatorUsername = taskFilterDto.getCreatorUsername();
        String assigneeUsername = taskFilterDto.getAssigneeUsername();

//        PageRequest pageable = PageRequest.of(taskFilterDto.getPage(), taskFilterDto.getSize(),
//                Sort.by(Sort.Direction.DESC, "createDate"));

        if (creatorUsername != null) {
            return taskMapper.toPageResponseDto(taskService.getAllTasksByCreator(creatorUsername, pageable));
        } else {
            return taskMapper.toPageResponseDto(taskService.getAllTasksByAssignee(assigneeUsername, pageable));
        }
    }
}
