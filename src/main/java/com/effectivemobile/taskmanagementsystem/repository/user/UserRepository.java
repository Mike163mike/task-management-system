package com.effectivemobile.taskmanagementsystem.repository.user;

import com.effectivemobile.taskmanagementsystem.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
