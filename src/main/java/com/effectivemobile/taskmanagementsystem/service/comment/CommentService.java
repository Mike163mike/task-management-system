package com.effectivemobile.taskmanagementsystem.service.comment;

import com.effectivemobile.taskmanagementsystem.entity.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Comment createComment(Comment comment);

    Page<Comment> getAllCommentsByTask(Long taskId, Pageable pageable);

    Comment updateComment(Long commentId, String text);

    void deleteComment(Long commentId);
}
