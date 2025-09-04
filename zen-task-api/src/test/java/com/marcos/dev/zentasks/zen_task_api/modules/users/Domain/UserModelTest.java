package com.marcos.dev.zentasks.zen_task_api.modules.users.Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.InvalidInputException;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test from UserModel entity")
class UserModelTest {

  private UserModel userModel;
  private BCryptPasswordEncoder passwordEncoder;
  private static final String RAW_PASSWORD = "password123";
  private static final String USERNAME = "testuser";
  private static final String EMAIL = "test@example.com";

  @BeforeEach
  void setUp() {
    passwordEncoder = new BCryptPasswordEncoder();
    String passwordHash = passwordEncoder.encode(RAW_PASSWORD);
    userModel = new UserModel(USERNAME, EMAIL, passwordHash, UserRole.USER);
  }

  @Test
  @DisplayName("Should change password when current password is correct")
  void changePassword_shouldSucceed_whenCurrentPasswordIsCorrectAndNewIsValid() {

    String newPassword = "newPassword456";
    String oldPasswordHash = userModel.getPasswordHash();

    userModel.changePassword(RAW_PASSWORD, newPassword);

    assertThat(userModel.getPasswordHash()).isNotEqualTo(oldPasswordHash);
    assertTrue(passwordEncoder.matches(newPassword, userModel.getPasswordHash()));
  }

  @Test
  @DisplayName("Should thow exception when current password is incorrect")
  void changePassword_shouldThrowException_whenCurrentPasswordIsIncorrect() {

    String wrongCurrentPassword = "wrongPassword";
    String newPassword = "newPassword456";

    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
      userModel.changePassword(wrongCurrentPassword, newPassword);
    });

    assertThat(exception.getMessage()).isEqualTo("A senha atual esta incorreta");
  }

  @Test
  @DisplayName("Should throw exception when new password is short")
  void changePassword_shouldThrowException_whenNewPasswordIsTooShort() {
    String shortNewPassword = "short";

    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
      userModel.changePassword(RAW_PASSWORD, shortNewPassword);
    });

    assertThat(exception.getMessage()).isEqualTo("A senha deve conter, no minímo, 8 caracteres.");
  }

  @Test
  @DisplayName("Should update email when new email is valid")
  void updateEmail_shouldSucceed_whenNewEmailIsValid() {

    String newEmail = "new.email@example.com";

    userModel.updateEmail(newEmail);

    assertThat(userModel.getEmail()).isEqualTo(newEmail);
  }

  @Test
  @DisplayName("Should throw exception when email is invalid")
  void updateEmail_shouldThrowException_whenNewEmailIsInvalid() {
    String invalidEmail = "invalid-email";

    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
      userModel.updateEmail(invalidEmail);
    });

    assertThat(exception.getMessage()).isEqualTo("Email inválido.");
  }

  @Test
  @DisplayName("Should update username with success")
  void updateUsername_shouldSucceed_whenUsernameIsValid() {
    String newUsername = "newusername";

    userModel.updateUsername(newUsername);

    assertThat(userModel.getUsername()).isEqualTo(newUsername);
  }

  @Test
  @DisplayName("Should throw new exception when username is blank")
  void updateUsername_shouldThrowException_whenUsernameIsBlank() {
    String blankUsername = "   ";

    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
      userModel.updateUsername(blankUsername);
    });

    assertThat(exception.getMessage()).isEqualTo("Username não pode estar vazio!");
  }

  @Test
  @DisplayName("Should authenticated with success when password is correct")
  void authenticate_shouldReturnTrue_whenPasswordIsCorrect() {
    boolean isAuthenticated = userModel.authenticate(RAW_PASSWORD);

    // Assert
    assertThat(isAuthenticated).isTrue();
  }

  @Test
  @DisplayName("Authentication should failed when password is incorrect")
  void authenticate_shouldReturnFalse_whenPasswordIsIncorrect() {
    boolean isAuthenticated = userModel.authenticate("wrongPassword");

    assertThat(isAuthenticated).isFalse();
  }

  @Test
  @DisplayName("Should return correct roles to Spring security ")
  void getAuthorities_shouldReturnCorrectlyFormattedRole() {
    Collection<? extends GrantedAuthority> authorities = userModel.getAuthorities();

    // Assert
    assertThat(authorities).hasSize(1);
    assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_USER");

    userModel.updateRole(UserRole.ADMIN);
    authorities = userModel.getAuthorities();
    assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
  }
}
