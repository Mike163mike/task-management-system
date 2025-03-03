package com.effectivemobile.taskmanagementsystem.repository.comment;

import com.effectivemobile.taskmanagementsystem.entity.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findAllByTaskId(Long taskId, Pageable pageable);
}
