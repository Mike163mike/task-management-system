package com.effectivemobile.taskmanagementsystem.mapper.user;

import com.effectivemobile.taskmanagementsystem.dto.user.UserCreationDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserUpdateDto;
import com.effectivemobile.taskmanagementsystem.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserCreationDto userCreationDto);

    User toEntity(UserUpdateDto userUpdateDto);

    UserResponseDto toResponseDto(User user);

    default Page<UserResponseDto> toPageResponseDto(Page<User> users) {
        return users.map(this::toResponseDto);
    }
}
