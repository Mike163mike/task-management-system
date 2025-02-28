package com.effectivemobile.taskmanagementsystem.service.task.impl;

import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import com.effectivemobile.taskmanagementsystem.repository.task.TaskRepository;
import com.effectivemobile.taskmanagementsystem.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(Task task) {

        return null;
    }

    @Override
    public Task updateTask(Long taskId, Task task) {
        return null;
    }

    @Override
    public void deleteTask(Long taskId) {

    }
}
