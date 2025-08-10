package com.marcos.dev.zentasks.zen_task_api.users.repository;

import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<UserDetails> findByUsername(String username);
}
