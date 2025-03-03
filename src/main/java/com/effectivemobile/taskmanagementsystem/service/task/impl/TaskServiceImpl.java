package com.effectivemobile.taskmanagementsystem.service.task.impl;

import com.effectivemobile.taskmanagementsystem.entity.task.PriorityEnum;
import com.effectivemobile.taskmanagementsystem.entity.task.StatusEnum;
import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.repository.task.TaskRepository;
import com.effectivemobile.taskmanagementsystem.repository.user.UserRepository;
import com.effectivemobile.taskmanagementsystem.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Override
    public Task createTask(Task task) {
        populateTask(task);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task updateTask(Long taskId, Task updatedTask) {
        Task oldTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException("Task with ID %s not found in DB".formatted(taskId),
                        this.getClass(), "updateTask"));

        if (updatedTask.getTitle() != null) {
            oldTask.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            oldTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getStatus() != null) {
            oldTask.setStatus(updatedTask.getStatus());
        }
        if (updatedTask.getPriority() != null) {
            oldTask.setPriority(updatedTask.getPriority());
        }
        if (updatedTask.getAssignee() != null) {
            oldTask.setAssignee(updatedTask.getAssignee());
        }

        return taskRepository.save(oldTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    @Transactional//todo ?
    public Page<Task> getAllTasksByCreator(String creatorUsername, Pageable pageable) {
        return taskRepository.findAllByCreatorUsernameOrderByCreateDateDesc(creatorUsername, pageable);
    }

    @Override
    @Transactional//todo ?
    public Page<Task> getAllTasksByAssignee(String assigneeUsername, Pageable pageable) {
        return taskRepository.findAllByAssigneeUsernameOrderByCreateDateDesc(assigneeUsername, pageable);
    }

    private void populateTask(Task task) {
        task.setCreator(userRepository.findById(1).get());//todo set user from securityContextHolder
        task.setPriority(PriorityEnum.LOW);
        task.setStatus(StatusEnum.PENDING);
    }
}
