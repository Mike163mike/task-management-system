package com.effectivemobile.taskmanagementsystem.facade.comment.impl;

import com.effectivemobile.taskmanagementsystem.dto.comment.CommentCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.comment.CommentUpdateDto;
import com.effectivemobile.taskmanagementsystem.facade.comment.CommentFacade;
import com.effectivemobile.taskmanagementsystem.mapper.comment.CommentMapper;
import com.effectivemobile.taskmanagementsystem.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentFacadeImpl implements CommentFacade {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @Override
    public CommentResponseDto createComment(CommentCreateDto commentCreateDto) {
        return commentMapper.toCommentResponseDto(commentService.createComment(commentMapper.toEntity(commentCreateDto)));
    }

    @Override
    public Page<CommentResponseDto> getAllCommentsByTask(Long taskId, Pageable pageable) {
        return commentMapper.toPageResponseDto(commentService.getAllCommentsByTask(taskId, pageable));
    }

    @Override
    public CommentResponseDto updateComment(Long commentId, CommentUpdateDto commentUpdateDto) {
        return commentMapper.toCommentResponseDto(commentService.updateComment(commentId, commentUpdateDto.getText()));
    }

    @Override
    public void deleteComment(Long commentId) {
        commentService.deleteComment(commentId);
    }
}
