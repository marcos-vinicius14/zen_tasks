package com.marcos.dev.zentasks.zen_task_api.common.infraestructure.security;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ForbiddenAccessException;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

@Service
public class AuthenticatedUserService {

  public UUID getCurrentUserId() {
    Authentication authentication = getCurrentAuthentication();

    return getUserIdFromAuthentication(authentication);
  }

  public String getCurrrentUsername() {
    return getCurrentAuthentication().getName();
  }

  public Authentication getCurrentAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new ForbiddenAccessException("Usuário não autenticado");
    }

    return authentication;
  }

  private UUID getUserIdFromAuthentication(Authentication authentication) {
    UserModel userAuthenticated = (UserModel) authentication.getPrincipal();

    if (userAuthenticated == null) {
      throw new ForbiddenAccessException("Usuário não autenticado");

    }

    return userAuthenticated.getId();
  }
}
