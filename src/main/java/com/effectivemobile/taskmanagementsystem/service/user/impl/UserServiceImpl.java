package com.effectivemobile.taskmanagementsystem.service.user.impl;

import com.effectivemobile.taskmanagementsystem.entity.role.Role;
import com.effectivemobile.taskmanagementsystem.entity.user.User;
import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.repository.role.RoleRepository;
import com.effectivemobile.taskmanagementsystem.repository.user.UserRepository;
import com.effectivemobile.taskmanagementsystem.security.RoleEnum;
import com.effectivemobile.taskmanagementsystem.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new CustomException("Default role not found", this.getClass(), "createUser"));

        user.setRoles(Set.of(role));

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
