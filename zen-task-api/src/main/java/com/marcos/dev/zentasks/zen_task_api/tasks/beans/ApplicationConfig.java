package com.marcos.dev.zentasks.zen_task_api.tasks.beans;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class ApplicationConfig {

  @Bean
  public AuditorAware<UUID> auditorProvider() {
    return new AuditorAware<UUID>() {
      @Override
      public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
            !authentication.isAuthenticated() ||
            !(authentication.getPrincipal() instanceof UserModel user)) {
          return Optional.empty();
        }

        return Optional.of(user.getId());
      }
    };
  }
}
