package com.effectivemobile.taskmanagementsystem.service.comment.impl;

import com.effectivemobile.taskmanagementsystem.entity.comment.Comment;
import com.effectivemobile.taskmanagementsystem.repository.comment.CommentRepository;
import com.effectivemobile.taskmanagementsystem.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getAllCommentsByTask(Long taskId, Pageable pageable) {
        return commentRepository.findAllByTaskId(taskId, pageable);
    }

    @Override
    public Comment updateComment(Long commentId, String text) {
        return commentRepository.save(new Comment());//todo доделать
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(Math.toIntExact(commentId));
    }
}
