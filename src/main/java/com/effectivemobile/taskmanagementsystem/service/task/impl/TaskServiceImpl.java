package com.effectivemobile.taskmanagementsystem.service.task.impl;

import com.effectivemobile.taskmanagementsystem.entity.task.PriorityEnum;
import com.effectivemobile.taskmanagementsystem.entity.task.StatusEnum;
import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import com.effectivemobile.taskmanagementsystem.entity.user.User;
import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.repository.task.TaskRepository;
import com.effectivemobile.taskmanagementsystem.repository.user.UserRepository;
import com.effectivemobile.taskmanagementsystem.service.task.TaskService;
import com.effectivemobile.taskmanagementsystem.service.user.UserService;
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

    private final UserService userService;

    @Override
    public Task createTask(Task task) {
        initializeNewTask(task);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task updateTask(Long taskId, Task updatedTask) {
        Task oldTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException("Task with ID %s not found in DB" .formatted(taskId),
                        this.getClass(), "updateTask"));

        validateTaskAccess(oldTask);

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
            if (updatedTask.getAssignee().getEmail() != null) {
                if (oldTask.getAssignee() == null ||
                        !oldTask.getAssignee().getEmail().equals(updatedTask.getAssignee().getEmail())) {

                    User assignee = userRepository.findByEmail(updatedTask.getAssignee().getEmail())
                            .orElseThrow(() -> new CustomException("User with email %s not found in DB"
                                    .formatted(updatedTask.getAssignee().getEmail()), this.getClass(), "updateTask"));
                    oldTask.setAssignee(assignee);
                }
            } else {
                oldTask.setAssignee(null);
            }
        }
        return oldTask;
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException("Task with ID %s not found in DB" .formatted(taskId),
                        this.getClass(), "deleteTask"));

        validateTaskAccess(task);

        taskRepository.deleteById(taskId);
    }

    @Override
    public Page<Task> getAllTasksByCreator(String creatorEmail, Pageable pageable) {
        return taskRepository.findAllByCreatorEmailOrderByCreateDateDesc(creatorEmail, pageable);
    }

    @Override
    public Page<Task> getAllTasksByAssignee(String assigneeEmail, Pageable pageable) {
        return taskRepository.findAllByAssigneeEmailOrderByCreateDateDesc(assigneeEmail, pageable);
    }

    public void validateTaskAccess(Task task) {
        Long currentUserId = userService.getCurrentUserId();
        Long taskCreatorId = task.getCreator().getId();
        User currentUser = userService.getCurrentUser();

        if (!taskCreatorId.equals(currentUserId) && !userService.isAdmin(currentUser)) {
            throw new CustomException("You don't have permission for this task", this.getClass(),
                    "validateTaskAccess");
        }
    }

    private void initializeNewTask(Task task) {
        task.setCreator(userService.getCurrentUser());
        task.setPriority(PriorityEnum.LOW);
        task.setStatus(StatusEnum.PENDING);
    }
}
