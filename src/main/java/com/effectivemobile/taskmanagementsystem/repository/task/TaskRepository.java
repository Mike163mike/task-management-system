package com.effectivemobile.taskmanagementsystem.repository.task;

import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = {"comments", "creator", "assignee"})
    @Query("SELECT DISTINCT t FROM Task t WHERE t.assignee.email = :assigneeEmail ORDER BY t.changeDate DESC")
    Page<Task> findAllByAssigneeEmailOrderByCreateDateDesc(@Param("assigneeEmail") String assigneeEmail,
                                                           Pageable pageable);

    @EntityGraph(attributePaths = {"comments", "creator", "assignee"})
    @Query("SELECT DISTINCT t FROM Task t WHERE t.creator.email = :creatorEmail ORDER BY t.changeDate DESC")
    Page<Task> findAllByCreatorEmailOrderByCreateDateDesc(@Param("creatorEmail") String creatorEmail,
                                                          Pageable pageable);
}
