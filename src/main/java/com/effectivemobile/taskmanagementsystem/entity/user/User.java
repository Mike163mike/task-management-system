package com.effectivemobile.taskmanagementsystem.entity.user;

import com.effectivemobile.taskmanagementsystem.entity.comment.Comment;
import com.effectivemobile.taskmanagementsystem.entity.task.Task;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"comments", "createdTasks", "assignedTasks", "roles"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @CreationTimestamp
    @Column(name = "create_date")
    private OffsetDateTime createDate;

    @UpdateTimestamp
    @Column(name = "change_date")
    private OffsetDateTime changeDate;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false, unique = true)
        private String password;

    @Column(name = "email", nullable = false, unique = true)
        private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> createdTasks = new ArrayList<>();

    @OneToMany(mappedBy = "assignee")
    private List<Task> assignedTasks = new ArrayList<>();
}
