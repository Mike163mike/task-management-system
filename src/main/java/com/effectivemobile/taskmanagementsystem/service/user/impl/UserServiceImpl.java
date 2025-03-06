package com.effectivemobile.taskmanagementsystem.service.user.impl;

import com.effectivemobile.taskmanagementsystem.entity.user.User;
import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.repository.user.UserRepository;
import com.effectivemobile.taskmanagementsystem.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User with ID %s not found in DB".formatted(userId),
                        this.getClass(), "updateUser"));

        if (updatedUser.getUsername() != null) {
            oldUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getPassword() != null) {
            oldUser.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getEmail() != null) {
            oldUser.setEmail(updatedUser.getEmail());
        }

        return userRepository.save(oldUser);
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
