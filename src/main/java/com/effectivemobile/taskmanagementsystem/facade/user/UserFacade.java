package com.effectivemobile.taskmanagementsystem.facade.user;

import com.effectivemobile.taskmanagementsystem.dto.user.SetRoleDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserCreationDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserFacade {

    UserResponseDto createUser(UserCreationDto userCreationDto);

    UserResponseDto updateUser(Long userId, UserUpdateDto userUpdateDto);

    void deleteUser(Long taskId);

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    void setRoleToUser(SetRoleDto setRoleDto);
}
