package com.effectivemobile.taskmanagementsystem.service.user;

import com.effectivemobile.taskmanagementsystem.entity.user.User;
import com.effectivemobile.taskmanagementsystem.security.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User createUser(User user);

    void deleteUser(Long userId);

    User updateUser(Long userId, User user);

    Page<User> getAllUsers(Pageable pageable);

    Long getCurrentUserId();

    User getCurrentUser();

    boolean isAdmin(User user);

    void setRoleToUser(String email, RoleEnum roleEnum);
}
