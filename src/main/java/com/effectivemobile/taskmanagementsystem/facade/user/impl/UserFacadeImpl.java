package com.effectivemobile.taskmanagementsystem.facade.user.impl;

import com.effectivemobile.taskmanagementsystem.dto.user.UserCreationDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserResponseDto;
import com.effectivemobile.taskmanagementsystem.dto.user.UserUpdateDto;
import com.effectivemobile.taskmanagementsystem.facade.user.UserFacade;
import com.effectivemobile.taskmanagementsystem.mapper.user.UserMapper;
import com.effectivemobile.taskmanagementsystem.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    private final UserMapper  userMapper;

    @Override
    public UserResponseDto createUser(UserCreationDto userCreationDto) {
        return userMapper.toResponseDto(userService.createUser(userMapper.toEntity(userCreationDto)));
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userMapper.toPageResponseDto(userService.getAllUsers(pageable));
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        return userMapper.toResponseDto(userService.updateUser(userId, userMapper.toEntity(userUpdateDto)));
    }

    @Override
    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }
}
