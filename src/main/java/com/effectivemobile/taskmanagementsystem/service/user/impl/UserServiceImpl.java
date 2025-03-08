package com.effectivemobile.taskmanagementsystem.service.user.impl;

import com.effectivemobile.taskmanagementsystem.entity.role.Role;
import com.effectivemobile.taskmanagementsystem.entity.user.User;
import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.repository.role.RoleRepository;
import com.effectivemobile.taskmanagementsystem.repository.user.UserRepository;
import com.effectivemobile.taskmanagementsystem.security.RoleEnum;
import com.effectivemobile.taskmanagementsystem.security.UserDetailsAdapter;
import com.effectivemobile.taskmanagementsystem.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public User updateUser(Long userId, User updatedUser) {
        User currentUser = getCurrentUser();

        if (!currentUser.getId().equals(userId) && !isAdmin(currentUser)) {
            throw new CustomException("You do not have permission to update this user",
                    HttpStatus.FORBIDDEN, this.getClass(), "updateUser");
        }

        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User with ID %s not found in DB".formatted(userId),
                        this.getClass(), "updateUser"));

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            oldUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()) {
            oldUser.setEmail(updatedUser.getEmail());
        }
        return userRepository.save(oldUser);
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User currentUser = getCurrentUser();

        if (currentUser.getId().equals(userId) || isAdmin(currentUser)) {
            userRepository.deleteById(userId);
        } else {
            throw new CustomException("You do not have permission to delete this user",
                    HttpStatus.FORBIDDEN, this.getClass(), "deleteUser");
        }
    }

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException("User is anonymous", this.getClass(), "getCurrentUserId");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsAdapter userDetailsAdapter) {
            return userDetailsAdapter.getUserId();
        }

        throw new CustomException("Unexpected principal type", this.getClass(), "getCurrentUserId");
    }

    @Override
    public User getCurrentUser() {
        Long currentUserId = getCurrentUserId();
        return userRepository.findById(currentUserId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND, this.getClass(),
                        "getCurrentUser"));
    }

    @Override
    public boolean isAdmin(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleName -> roleName.equals(RoleEnum.ROLE_ADMIN));
    }

    @Override
    public boolean isAdminOrAssignee(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(roleName -> roleName.equals(RoleEnum.ROLE_ADMIN)
                        || roleName.equals(RoleEnum.ROLE_ASSIGNEE));
    }
}
