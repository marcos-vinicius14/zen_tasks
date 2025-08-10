package com.marcos.dev.zentasks.zen_task_api.users.model;

import com.marcos.dev.zentasks.zen_task_api.tasks.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.users.enums.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "tb_users")
public class UserModel implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 250)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TaskModel> tasks = new HashSet<TaskModel>();


    public UserModel() {
    }

    public UserModel(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public UUID getId() {
        return id;
    }

    public Set<TaskModel> getTasks() {
        return tasks;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(createGrantedAuthority(this.role));
    }


    private GrantedAuthority createGrantedAuthority(UserRole role) {
        return new SimpleGrantedAuthority(formatRoleName(role));
    }

    private String formatRoleName(UserRole role) {
        return ROLE_PREFIX + role.getRole().toUpperCase();
    }



    @Override
    public String getPassword() {
        return "";
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setTasks(Set<TaskModel> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(id, userModel.id) && Objects.equals(username, userModel.username) && Objects.equals(email, userModel.email) && Objects.equals(passwordHash, userModel.passwordHash) && Objects.equals(createdAt, userModel.createdAt) && Objects.equals(updatedAt, userModel.updatedAt) && Objects.equals(tasks, userModel.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
