package com.marcos.dev.zentasks.zen_task_api.common.infraestructure.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ForbiddenAccessException;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

@ExtendWith(MockitoExtension.class)
class AuthenticatedUserServiceTest {

  @InjectMocks
  private AuthenticatedUserService authenticatedUserService;

  @Test
  void shouldReturnCurrentUserId() {
    // Given
    UUID expectedUserId = UUID.randomUUID();
    UserModel userDetails = mock(UserModel.class);
    when(userDetails.getId()).thenReturn(expectedUserId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authentication.isAuthenticated()).thenReturn(true);

    try (MockedStatic<SecurityContextHolder> mockedSecurityContext = mockStatic(SecurityContextHolder.class)) {

      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedSecurityContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      // When
      UUID result = authenticatedUserService.getCurrentUserId();

      // Then
      assertThat(result).isEqualTo(expectedUserId);
    }
  }

  @Test
  void shouldReturnCurrentUsername() {
    // Given
    String expectedUsername = "testuser";
    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(expectedUsername);
    when(authentication.isAuthenticated()).thenReturn(true);

    try (MockedStatic<SecurityContextHolder> mockedSecurityContext = mockStatic(SecurityContextHolder.class)) {

      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedSecurityContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      // When
      String result = authenticatedUserService.getCurrrentUsername();

      // Then
      assertThat(result).isEqualTo(expectedUsername);
    }
  }

  @Test
  void shouldThrowExceptionWhenNoAuthentication() {
    // Given
    try (MockedStatic<SecurityContextHolder> mockedSecurityContext = mockStatic(SecurityContextHolder.class)) {

      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(null);
      mockedSecurityContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      // When & Then
      assertThatThrownBy(() -> authenticatedUserService.getCurrentUserId())
          .isInstanceOf(ForbiddenAccessException.class)
          .hasMessage("Usuário não autenticado");
    }
  }

  @Test
  void shouldThrowExceptionWhenAuthenticationNotAuthenticated() {
    // Given
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false);

    try (MockedStatic<SecurityContextHolder> mockedSecurityContext = mockStatic(SecurityContextHolder.class)) {

      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedSecurityContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      assertThatThrownBy(() -> authenticatedUserService.getCurrentAuthentication())
          .isInstanceOf(ForbiddenAccessException.class);
    }
  }
}
