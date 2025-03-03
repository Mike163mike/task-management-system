package com.effectivemobile.taskmanagementsystem.mapper.comment;

import com.effectivemobile.taskmanagementsystem.dto.comment.CommentCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.effectivemobile.taskmanagementsystem.entity.comment.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    Comment toEntity(CommentCreateDto commentCreateDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "taskId", source = "task.id")
    CommentResponseDto toCommentResponseDto(Comment comment);

    default Page<CommentResponseDto> toPageResponseDto(Page<Comment> comments) {
        return comments.map(this::toCommentResponseDto);
    }
}
