package com.myhome.services.unit;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.exceptions.CredentialsIncorrectException;
import com.myhome.controllers.exceptions.UserNotFoundException;
import com.myhome.domain.AuthenticationData;
import com.myhome.model.LoginRequest;
import com.myhome.security.jwt.AppJwt;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import com.myhome.services.springdatajpa.AuthenticationSDJpaService;
import com.myhome.services.springdatajpa.UserSDJpaService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * TODO
 */
public class AuthenticationSDJpaServiceTest {

  private final String USER_ID = "test-user-id";
  private final String USERNAME = "test-user-name";
  private final String USER_EMAIL = "test-user-email";
  private final String USER_PASSWORD = "test-user-password";
  private final String REQUEST_PASSWORD = "test-request-password";
  private final Duration TOKEN_LIFETIME = Duration.ofDays(1);
  private final String SECRET = "secret";

  @Mock
  private final UserSDJpaService userSDJpaService = mock(UserSDJpaService.class);
  @Mock
  private final AppJwtEncoderDecoder appJwtEncoderDecoder = mock(AppJwtEncoderDecoder.class);
  @Mock
  private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
  private final AuthenticationSDJpaService authenticationSDJpaService =
      new AuthenticationSDJpaService(TOKEN_LIFETIME, SECRET, userSDJpaService, appJwtEncoderDecoder,
          passwordEncoder);

  /**
   * tests the login functionality of the system by providing a valid email and password
   * and verifying that the user is authenticated and the JWT token is generated correctly.
   */
  @Test
  void loginSuccess() {
    // given
    LoginRequest request = getDefaultLoginRequest();
    UserDto userDto = getDefaultUserDtoRequest();
    AppJwt appJwt = getDefaultJwtToken(userDto);
    String encodedJwt = appJwtEncoderDecoder.encode(appJwt, SECRET);
    given(userSDJpaService.findUserByEmail(request.getEmail()))
        .willReturn(Optional.of(userDto));
    given(passwordEncoder.matches(request.getPassword(), userDto.getEncryptedPassword()))
        .willReturn(true);
    given(appJwtEncoderDecoder.encode(appJwt, SECRET))
        .willReturn(encodedJwt);

    // when
    AuthenticationData authenticationData = authenticationSDJpaService.login(request);

    // then
    assertNotNull(authenticationData);
    assertEquals(authenticationData.getUserId(), userDto.getUserId());
    assertEquals(authenticationData.getJwtToken(), encodedJwt);
    verify(userSDJpaService).findUserByEmail(request.getEmail());
    verify(passwordEncoder).matches(request.getPassword(), userDto.getEncryptedPassword());
    verify(appJwtEncoderDecoder).encode(appJwt, SECRET);
  }

  /**
   * tests whether an exception is thrown when a user with the given email address is
   * not found in the database.
   */
  @Test
  void loginUserNotFound() {
    // given
    LoginRequest request = getDefaultLoginRequest();
    given(userSDJpaService.findUserByEmail(request.getEmail()))
        .willReturn(Optional.empty());

    // when and then
    assertThrows(UserNotFoundException.class,
        () -> authenticationSDJpaService.login(request));
  }

  /**
   * tests the `CredentialsIncorrectException` thrown when the user's password does not
   * match the encrypted password stored in the database for their email address.
   */
  @Test
  void loginCredentialsAreIncorrect() {
    // given
    LoginRequest request = getDefaultLoginRequest();
    UserDto userDto = getDefaultUserDtoRequest();
    given(userSDJpaService.findUserByEmail(request.getEmail()))
        .willReturn(Optional.of(userDto));
    given(passwordEncoder.matches(request.getPassword(), userDto.getEncryptedPassword()))
        .willReturn(false);

    // when and then
    assertThrows(CredentialsIncorrectException.class,
        () -> authenticationSDJpaService.login(request));
  }

  /**
   * creates a default login request with an email address of `USER_EMAIL` and a password
   * of `REQUEST_PASSWORD`.
   * 
   * @returns a `LoginRequest` object containing the email address and password for a
   * default login.
   * 
   * 	- `email`: This is an instance of the `Email` class that represents the email
   * address of the default login request.
   * 	- `password`: This is an instance of the `Password` class that represents the
   * password for the default login request.
   */
  private LoginRequest getDefaultLoginRequest() {
    return new LoginRequest().email(USER_EMAIL).password(REQUEST_PASSWORD);
  }

  /**
   * builds a default instance of the `UserDto` class, setting user ID, name, email,
   * encrypted password, and community IDs to specified values.
   * 
   * @returns a `UserDto` object containing default values for user fields.
   * 
   * 	- `userId`: An integer representing the user's ID.
   * 	- `name`: A string containing the user's name.
   * 	- `email`: An email address associated with the user.
   * 	- `encryptedPassword`: An encrypted password for the user.
   * 	- `communityIds`: A set of integers representing the communities to which the
   * user belongs.
   */
  private UserDto getDefaultUserDtoRequest() {
    return UserDto.builder()
        .userId(USER_ID)
        .name(USERNAME)
        .email(USER_EMAIL)
        .encryptedPassword(USER_PASSWORD)
        .communityIds(new HashSet<>())
        .build();
  }

  /**
   * creates a new JWT token with a specified expiration time based on the current date
   * and time, and returns it with the user ID and expiration information.
   * 
   * @param userDto user details which are used to generate the JWT token.
   * 
   * 	- `userId`: The user ID of the authenticated user.
   * 	- `TOKEN_LIFETIME`: A constant representing the lifetime of the JWT token in milliseconds.
   * 
   * @returns a newly-created AppJwt instance with a user ID and an expiration time
   * calculated based on the token lifetime.
   * 
   * 	- `userId`: The user ID of the user to whom the token is issued.
   * 	- `expiration`: The expiration time of the token in LocalDateTime format, which
   * is calculated by adding `TOKEN_LIFETIME` to the current date and time.
   */
  private AppJwt getDefaultJwtToken(UserDto userDto) {
    final LocalDateTime expirationTime = LocalDateTime.now().plus(TOKEN_LIFETIME);
    return AppJwt.builder()
        .userId(userDto.getUserId())
        .expiration(expirationTime)
        .build();
  }
}
