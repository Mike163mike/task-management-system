package com.effectivemobile.taskmanagementsystem.repository.role;

import com.effectivemobile.taskmanagementsystem.entity.role.Role;
import com.effectivemobile.taskmanagementsystem.security.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);
}
