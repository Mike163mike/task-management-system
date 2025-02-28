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
    Page<Task>  findAllByAssignee(Pageable pageable, String assignee);

    @EntityGraph(attributePaths = {"comments", "creator", "assignee"})
    Page<Task> findAllByCreator(Pageable pageable, String creator);
}
