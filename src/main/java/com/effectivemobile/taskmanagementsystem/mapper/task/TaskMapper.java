package com.effectivemobile.taskmanagementsystem.mapper.task;

import com.effectivemobile.taskmanagementsystem.dto.task.TaskCreateDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.task.TaskUpdateDto;
import com.effectivemobile.taskmanagementsystem.entity.task.PriorityEnum;
import com.effectivemobile.taskmanagementsystem.entity.task.StatusEnum;
import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task toEntity(TaskCreateDto taskCreateDto);

    @Mapping(target = "assignee.username", source = "assignee")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "mapPriority")
    Task toEntity(TaskUpdateDto taskUpdateDto);

    @Mapping(target = "creator", source = "creator.username")
    @Mapping(target = "assignee", source = "assignee.username")
    TaskResponseDto toResponseDto(Task task);

    default Page<TaskResponseDto> toPageResponseDto(Page<Task> tasks) {
        return tasks.map(this::toResponseDto);
    }

    @Named("mapStatus")
    static StatusEnum mapStatus(String status) {
        return status != null ? StatusEnum.valueOf(status) : null;
    }

    @Named("mapPriority")
    static PriorityEnum mapPriority(String priority) {
        return priority != null ? PriorityEnum.valueOf(priority) : null;
    }
}
