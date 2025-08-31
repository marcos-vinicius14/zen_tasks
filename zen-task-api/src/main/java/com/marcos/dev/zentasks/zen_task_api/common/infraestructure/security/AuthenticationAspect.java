package com.marcos.dev.zentasks.zen_task_api.common.infraestructure.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.marcos.dev.zentasks.zen_task_api.common.domain.security.annotations.RequireAuthentication;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ForbiddenAccessException;

@Aspect
@Component

public class AuthenticationAspect {
  private Logger logger = LoggerFactory.getLogger(AuthenticationAspect.class);

  @Before("@annotation(requireAuth)")
  public void checkAuthentication(JoinPoint joinPoint, RequireAuthentication requireAuthentication) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      String methodName = joinPoint.getSignature().getName();
      String className = joinPoint.getTarget().getClass().getSimpleName();

      logger.warn("Tentativa de acesso não autorizada ao método {}.{}", className, methodName);
      throw new ForbiddenAccessException(
          String.format("[%s] %s", className, requireAuthentication.message()));

    }
  }
}
