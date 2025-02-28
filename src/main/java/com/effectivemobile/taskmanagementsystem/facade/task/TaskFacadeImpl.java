package com.effectivemobile.taskmanagementsystem.facade.task;

import com.effectivemobile.taskmanagementsystem.dto.task.TaskCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskUpdateDto;
import com.effectivemobile.taskmanagementsystem.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskFacadeImpl implements TaskFacade {

    private final TaskService taskService;

    @Override
    public ResponseEntity<TaskResponseDto> createTask(TaskCreateDto taskCreateDto) {
        return null; //todo вернуть 201 статус CREATED и DTO с ID в теле
    }

    @Override
    public ResponseEntity<TaskResponseDto> updateTask(Long taskId, TaskUpdateDto taskUpdateDto) {
        return null;//todo вернуть 200 статус OK и DTO с ID в теле
    }

    @Override
    public ResponseEntity<Void> deleteTask(Long taskId) {
        return ResponseEntity.noContent().build();//todo вернуть 200 статус OK
    }
}
