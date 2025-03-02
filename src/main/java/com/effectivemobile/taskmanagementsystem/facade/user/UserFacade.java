package com.effectivemobile.taskmanagementsystem.facade.user;

import com.effectivemobile.taskmanagementsystem.dto.user.UserCreationDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserFacade {

    UserResponseDto createUser(UserCreationDto userCreationDto);

    UserResponseDto updateUser(Long userId, UserCreationDto userCreationDto);

    void deleteUser(Long taskId);

    Page<UserResponseDto> getAllUsers(Pageable pageable);
}
