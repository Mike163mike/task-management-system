package com.effectivemobile.taskmanagementsystem.service.task;

import com.effectivemobile.taskmanagementsystem.entity.task.Task;

public interface TaskService {

    Task createTask(Task task);

    Task updateTask(Long taskId, Task task);

    void deleteTask(Long taskId);
}
