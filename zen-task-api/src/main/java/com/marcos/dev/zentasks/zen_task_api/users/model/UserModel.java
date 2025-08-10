package com.marcos.dev.zentasks.zen_task_api.users.model;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.InvalidInputException;
import com.marcos.dev.zentasks.zen_task_api.tasks.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.users.enums.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "tb_users")
public class UserModel implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false, length = 50, unique = true, updatable = false)
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

    public UserModel(String username, String email, String password, UserRole role) {
        this.id = null;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }



    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    private static void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new InvalidInputException("Username cannot be null or empty");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidInputException("Invalid email format");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new InvalidInputException("Password must be at least 8 characters long");
        }
    }


   public void changePassword(String currentPassword, String newPassword) {
        validatePassword(newPassword);

        if (!new BCryptPasswordEncoder().matches(currentPassword, this.passwordHash)) {
            throw new InvalidInputException("Current password is incorrect");
        }

        this.passwordHash = new BCryptPasswordEncoder().encode(newPassword);
    }

    public void updateEmail(String newEmail) {
        validateEmail(newEmail);
        this.email = newEmail;
    }

    public void updateUsername(String newUsername) {
        validateUsername(newUsername);
        this.username = newUsername;
    }

    public void updateRole(UserRole newRole) {
        this.role = newRole;
    }

    public boolean authenticate(String password) {
        return new BCryptPasswordEncoder().matches(password, this.passwordHash);
    }

    public void  addTask(TaskModel task) {
        this.tasks.add(task);
    }

    public void removeTask(TaskModel task) {
        this.tasks.remove(task);
    }

    public void removeAllTasks() {
        this.tasks.clear();
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
        return passwordHash;
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

    public UserRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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
