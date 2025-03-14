package com.effectivemobile.taskmanagementsystem.service.task;

import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Task createTask(Task task);

    Task updateTask(Long taskId, Task updatedTask);

    void deleteTask(Long taskId);

    Page<Task> getAllTasksByCreator(String creatorEmail, Pageable pageable);

    Page<Task> getAllTasksByAssignee(String assigneeEmail, Pageable pageable);

    void validateTaskAccess(Task task);
}
