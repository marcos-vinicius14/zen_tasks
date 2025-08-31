package com.marcos.dev.zentasks.zen_task_api.modules.users.Infrastructure.repository;

import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  Optional<UserDetails> findByUsername(String username);
}
