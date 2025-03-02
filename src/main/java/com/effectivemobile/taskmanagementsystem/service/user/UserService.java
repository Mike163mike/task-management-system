package com.effectivemobile.taskmanagementsystem.service.user;

import com.effectivemobile.taskmanagementsystem.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User createUser(User user);

    void deleteUser(Long userId);

    User updateUser(Long userId, User user);

    Page<User> getAllUsers(Pageable pageable);
}
