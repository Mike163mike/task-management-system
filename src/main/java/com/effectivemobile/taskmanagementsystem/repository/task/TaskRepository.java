package com.effectivemobile.taskmanagementsystem.repository.task;

import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = {"comments", "creator", "assignee"})
    Page<Task> findAllByAssigneeUsernameOrderByCreateDateDesc(String assigneeUsername, Pageable pageable);

    @EntityGraph(attributePaths = {"comments", "creator", "assignee"})
    Page<Task> findAllByCreatorUsernameOrderByCreateDateDesc(String creatorUsername, Pageable pageable);
}
