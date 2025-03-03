package com.effectivemobile.taskmanagementsystem.facade.comment;

import com.effectivemobile.taskmanagementsystem.dto.comment.CommentCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.comment.CommentUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentFacade {

    CommentResponseDto createComment(CommentCreateDto commentCreateDto);

    Page<CommentResponseDto> getAllCommentsByTask(Long taskId, Pageable pageable);

    CommentResponseDto updateComment(Long commentId, CommentUpdateDto commentUpdateDto);

    void deleteComment(Long commentId);
}
