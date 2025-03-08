package com.effectivemobile.taskmanagementsystem.service.comment.impl;

import com.effectivemobile.taskmanagementsystem.entity.comment.Comment;
import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import com.effectivemobile.taskmanagementsystem.entity.user.User;
import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.repository.comment.CommentRepository;
import com.effectivemobile.taskmanagementsystem.repository.task.TaskRepository;
import com.effectivemobile.taskmanagementsystem.service.comment.CommentService;
import com.effectivemobile.taskmanagementsystem.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    private final UserService userService;

    @Override
    public Comment createComment(Comment comment) {
        Long taskId = comment.getTask().getId();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException("Task with ID %s not found in DB".formatted(taskId),
                        this.getClass(), "createComment"));

        checkUserPermissionForTask(task);

        populateComment(comment);
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getAllCommentsByTask(Long taskId, Pageable pageable) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException("Task with ID %s not found in DB".formatted(taskId),
                        this.getClass(), "getAllCommentsByTask"));

        checkUserPermissionForTask(task);

        return commentRepository.findAllByTaskId(taskId, pageable);
    }

    @Override
    public Comment updateComment(Long commentId, Comment updatedComment) {
        Comment commentToUpdate = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("Comment with ID %s not found in DB".formatted(commentId),
                        this.getClass(), "updateComment"));

        checkUserPermissionForComment(commentToUpdate);

        populateComment(commentToUpdate, updatedComment);
        return commentRepository.save(commentToUpdate);
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("Comment with ID %s not found in DB".formatted(commentId),
                        this.getClass(), "deleteComment"));

        checkUserPermissionForComment(comment);

        Task task = comment.getTask();
        task.getComments().remove(comment);
        taskRepository.save(task);
        commentRepository.delete(comment);
    }

    private void checkUserPermissionForTask(Task task) {
        Long currentUserId = userService.getCurrentUserId();
        Long taskCreatorId = task.getCreator().getId();
        User currentUser = userService.getCurrentUser();

        if (!taskCreatorId.equals(currentUserId) && !userService.isAdmin(currentUser)) {
            throw new CustomException("You don't have permission for this task", this.getClass(),
                    "checkUserPermissionForTask");
        }
    }

    private void checkUserPermissionForComment(Comment comment) {
        Long currentUserId = userService.getCurrentUserId();
        Long commentHolderId = comment.getUser().getId();
        User currentUser = userService.getCurrentUser();

        if (!currentUserId.equals(commentHolderId) && !userService.isAdmin(currentUser)) {
            throw new CustomException("You don't have permission for this comment", this.getClass(),
                    "checkUserPermissionForComment");
        }
    }

    private void populateComment(Comment commentToUpdate, Comment updatedComment) {
        if (updatedComment.getText() != null) {
            commentToUpdate.setText(updatedComment.getText());
        }

        checkDuplicateCommentText(commentToUpdate);
        populateTaskAndUser(commentToUpdate, updatedComment);
    }

    private void populateComment(Comment comment) {
        checkDuplicateCommentText(comment);
        populateTaskAndUser(comment, comment);
    }

    private void checkDuplicateCommentText(Comment comment) {
        if (commentRepository.existsByTextAndIdNot(comment.getText(), comment.getId())) {
            throw new CustomException("A comment with the same text already exists", this.getClass(),
                    "checkDuplicateCommentText");
        }
    }

    private void populateTaskAndUser(Comment commentToUpdate, Comment updatedComment) {
        if (updatedComment.getTask() != null) {
            Task task = taskRepository.findById(updatedComment.getTask().getId())
                    .orElseThrow(() -> new CustomException("Task not found", this.getClass(), "populateTaskAndUser"));
            commentToUpdate.setTask(task);
        }

        User currentUser = userService.getCurrentUser();
        commentToUpdate.setUser(currentUser);
    }
}
