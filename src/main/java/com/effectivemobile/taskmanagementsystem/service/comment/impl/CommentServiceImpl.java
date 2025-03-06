package com.effectivemobile.taskmanagementsystem.service.comment.impl;

import com.effectivemobile.taskmanagementsystem.entity.comment.Comment;
import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import com.effectivemobile.taskmanagementsystem.entity.user.User;
import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.repository.comment.CommentRepository;
import com.effectivemobile.taskmanagementsystem.repository.task.TaskRepository;
import com.effectivemobile.taskmanagementsystem.repository.user.UserRepository;
import com.effectivemobile.taskmanagementsystem.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    @Override
    public Comment createComment(Comment comment) {
        populateComment(comment);
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getAllCommentsByTask(Long taskId, Pageable pageable) {
        return commentRepository.findAllByTaskId(taskId, pageable);
    }

    public Comment updateComment(Long commentId, Comment updatedComment) {
        Comment commentToUpdate = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("A comment with the ID %s not found",
                        this.getClass(), "updateComment"));

        populateComment(commentToUpdate, updatedComment);

        return commentRepository.save(commentToUpdate);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("A comment with the ID %s not found", this.getClass(),
                        "deleteComment"));

        Task task = comment.getTask();
        task.getComments().remove(comment);
        taskRepository.save(task);

        commentRepository.delete(comment);
    }


    private void populateComment(Comment commentToUpdate, Comment updatedComment) {
        if (updatedComment.getText() != null) {
            commentToUpdate.setText(updatedComment.getText());
        }

        if (commentRepository.existsByTextAndIdNot(commentToUpdate.getText(), commentToUpdate.getId())) {
            throw new CustomException("A comment with the same text already exists", this.getClass(), "populateComment");
        }

        populateTaskAndUser(commentToUpdate, updatedComment);
    }

    private void populateComment(Comment comment) {
        if (commentRepository.existsByText(comment.getText())) {
            throw new CustomException("A comment with the same text already exists", this.getClass(), "populateComment");
        }

        populateTaskAndUser(comment, comment);
    }

    private void populateTaskAndUser(Comment commentToUpdate, Comment updatedComment) {
        if (updatedComment.getTask() != null) {
            Task task = taskRepository.findById(updatedComment.getTask().getId())
                    .orElseThrow(() -> new CustomException("Task not found", this.getClass(), "populateComment"));
            commentToUpdate.setTask(task);
        }

        User user = userRepository.findById(1L) // TODO: заменить на реального пользователя
                .orElseThrow(() -> new CustomException("User not found", this.getClass(), "populateComment"));
        commentToUpdate.setUser(user);
    }
}
