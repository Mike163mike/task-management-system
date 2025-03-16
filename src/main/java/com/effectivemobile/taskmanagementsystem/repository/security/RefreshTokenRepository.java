package com.effectivemobile.taskmanagementsystem.repository.security;

import com.effectivemobile.taskmanagementsystem.entity.security.RefreshToken;
import com.effectivemobile.taskmanagementsystem.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    @Transactional
    void deleteByToken(String token);


    boolean existsByToken(String newRefreshToken);

    boolean existsByUser(User user);
}

