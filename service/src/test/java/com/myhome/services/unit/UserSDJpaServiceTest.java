package com.myhome.services.unit;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.mapper.UserMapper;
import com.myhome.model.ForgotPasswordRequest;
import com.myhome.domain.Community;
import com.myhome.domain.SecurityToken;
import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.User;
import com.myhome.repositories.SecurityTokenRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.springdatajpa.MailSDJpaService;
import com.myhome.services.springdatajpa.SecurityTokenSDJpaService;
import com.myhome.services.springdatajpa.UserSDJpaService;
import helpers.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class UserSDJpaServiceTest {

  private final String USER_ID = "test-user-id";
  private final String USERNAME = "test-user-name";
  private final String USER_EMAIL = "test-user-email";
  private final String USER_PASSWORD = "test-user-password";
  private final String NEW_USER_PASSWORD = "test-user-new-password";
  private final String PASSWORD_RESET_TOKEN = "test-token";
  private final Duration TOKEN_LIFETIME = Duration.ofDays(1);

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private SecurityTokenSDJpaService securityTokenService;
  @Mock
  private MailSDJpaService mailService;
  @Mock
  private SecurityTokenRepository securityTokenRepository;
  @InjectMocks
  private UserSDJpaService userService;

  /**
   * sets up mock objects using the `MockitoAnnotations.initMocks` method, preparing
   * the environment for testing.
   */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * creates a new user based on a provided request, saves it to the database, and
   * generates an email confirmation token.
   */
  @Test
  void createUserSuccess() {
    // given
    UserDto request = getDefaultUserDtoRequest();
    User resultUser = getUserFromDto(request);
    UserDto response = UserDto.builder()
        .id(resultUser.getId())
        .userId(resultUser.getUserId())
        .name(resultUser.getName())
        .encryptedPassword(resultUser.getEncryptedPassword())
        .communityIds(new HashSet<>())
        .build();
    SecurityToken emailConfirmToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, "token", resultUser);

    given(userRepository.findByEmail(request.getEmail()))
        .willReturn(null);
    given(passwordEncoder.encode(request.getPassword()))
        .willReturn(request.getPassword());
    given(userMapper.userDtoToUser(request))
        .willReturn(resultUser);
    given(userRepository.save(resultUser))
        .willReturn(resultUser);
    given(userMapper.userToUserDto(resultUser))
        .willReturn(response);
    given(securityTokenService.createEmailConfirmToken(resultUser))
        .willReturn(emailConfirmToken);

    // when
    Optional<UserDto> createdUserDtoOptional = userService.createUser(request);

    // then
    assertTrue(createdUserDtoOptional.isPresent());
    UserDto createdUserDto = createdUserDtoOptional.get();
    assertEquals(response, createdUserDto);
    assertEquals(0, createdUserDto.getCommunityIds().size());
    verify(userRepository).findByEmail(request.getEmail());
    verify(passwordEncoder).encode(request.getPassword());
    verify(userRepository).save(resultUser);
    verify(securityTokenService).createEmailConfirmToken(resultUser);
  }

  /**
   * tests whether creating a user with an existing email address returns false and
   * calls the `verify` method on the `userRepository` to simulate the query being executed.
   */
  @Test
  void createUserEmailExists() {
    // given
    UserDto request = getDefaultUserDtoRequest();
    User user = getUserFromDto(request);

    given(userRepository.findByEmail(request.getEmail()))
        .willReturn(user);

    // when
    Optional<UserDto> createdUserDto = userService.createUser(request);

    // then
    assertFalse(createdUserDto.isPresent());
    verify(userRepository).findByEmail(request.getEmail());
  }

  /**
   * tests the user service's ability to retrieve a user's details from the repository
   * and map them to a UserDto object. It verifies that the resulting UserDto object
   * matches the expected default user Dto, has no community IDs, and calls the verify
   * method on the repository.
   */
  @Test
  void getUserDetailsSuccess() {
    // given
    UserDto userDto = getDefaultUserDtoRequest();
    User user = getUserFromDto(userDto);

    given(userRepository.findByUserIdWithCommunities(USER_ID))
        .willReturn(Optional.of(user));
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> createdUserDtoOptional = userService.getUserDetails(USER_ID);

    // then
    assertTrue(createdUserDtoOptional.isPresent());
    UserDto createdUserDto = createdUserDtoOptional.get();
    assertEquals(userDto, createdUserDto);
    assertEquals(0, createdUserDto.getCommunityIds().size());
    verify(userRepository).findByUserIdWithCommunities(USER_ID);
  }

  /**
   * verifies that a user's details can be retrieved successfully along with their
   * community IDs using the `userRepository` and `userMapper`.
   */
  @Test
  void getUserDetailsSuccessWithCommunityIds() {
    // given
    UserDto userDto = getDefaultUserDtoRequest();
    User user = new User(userDto.getName(), userDto.getUserId(), userDto.getEmail(), false,
        userDto.getEncryptedPassword(), new HashSet<>(), null);

    Community firstCommunity = TestUtils.CommunityHelpers.getTestCommunity(user);
    Community secCommunity = TestUtils.CommunityHelpers.getTestCommunity(user);

    Set<Community> communities =
        Stream.of(firstCommunity, secCommunity).collect(Collectors.toSet());

    Set<String> communitiesIds = communities
        .stream()
        .map(community -> community.getCommunityId())
        .collect(Collectors.toSet());

    given(userRepository.findByUserIdWithCommunities(USER_ID))
        .willReturn(Optional.of(user));
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> createdUserDtoOptional = userService.getUserDetails(USER_ID);

    // then
    assertTrue(createdUserDtoOptional.isPresent());
    UserDto createdUserDto = createdUserDtoOptional.get();
    assertEquals(userDto, createdUserDto);
    assertEquals(communitiesIds, createdUserDto.getCommunityIds());
    verify(userRepository).findByUserIdWithCommunities(USER_ID);
  }

  /**
   * tests whether the `getUserDetails` method returns an empty Optional when the user
   * with the given ID does not exist in the repository.
   */
  @Test
  void getUserDetailsNotFound() {
    // given
    given(userRepository.findByUserIdWithCommunities(USER_ID))
        .willReturn(Optional.empty());

    // when
    Optional<UserDto> createdUserDto = userService.getUserDetails(USER_ID);

    // then
    assertFalse(createdUserDto.isPresent());
    verify(userRepository).findByUserIdWithCommunities(USER_ID);
  }

  /**
   * verifies an email address is confirmed for a user by sending a confirmation request,
   * retrieving the token response, and saving the user's updated status in the database.
   */
  @Test
  void confirmEmail() {
    // given
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN,
            user);
    user.getUserTokens().add(testSecurityToken);
    given(securityTokenService.useToken(testSecurityToken))
        .willReturn(testSecurityToken);
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));
    //    given(mailService.sendAccountConfirmed(user))
    //        .willReturn(true);

    // when
    boolean emailConfirmed =
        userService.confirmEmail(user.getUserId(), testSecurityToken.getToken());

    // then
    assertTrue(emailConfirmed);
    assertTrue(user.isEmailConfirmed());
    verify(securityTokenService).useToken(testSecurityToken);
    verify(userRepository).save(user);
    //    verify(mailService).sendAccountConfirmed(user);
  }

  /**
   * tests the email confirmation process for a user when an incorrect token is provided.
   * It verifies that the email is not confirmed and fails to save the user in the repository.
   */
  @Test
  void confirmEmailWrongToken() {
    // given
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN,
            user);
    user.getUserTokens().add(testSecurityToken);
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));

    // when
    boolean emailConfirmed = userService.confirmEmail(user.getUserId(), "wrong-token");

    // then
    assertFalse(emailConfirmed);
    assertFalse(user.isEmailConfirmed());
    verify(userRepository, never()).save(user);
    verifyNoInteractions(securityTokenService);
    verifyNoInteractions(mailService);
  }

  /**
   * tests whether an email is confirmed using a token that has been marked as used by
   * the user.
   */
  @Test
  void confirmEmailUsedToken() {
    // given
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN,
            user);
    testSecurityToken.setUsed(true);
    user.getUserTokens().add(testSecurityToken);
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));

    // when
    boolean emailConfirmed =
        userService.confirmEmail(user.getUserId(), testSecurityToken.getToken());

    // then
    assertFalse(emailConfirmed);
    assertFalse(user.isEmailConfirmed());
    verify(userRepository, never()).save(user);
    verifyNoInteractions(securityTokenService);
    verifyNoInteractions(mailService);
  }

  /**
   * tests whether a user's email can be confirmed without providing a token. It does
   * this by attempting to confirm the user's email and verifying that it returns false,
   * as well as checking that the user's email is not marked as confirmed and that no
   * interactions occurred with the `securityTokenService` or `mailService`.
   */
  @Test
  void confirmEmailNoToken() {
    // given
    User user = getDefaultUser();
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));

    // when
    boolean emailConfirmed = userService.confirmEmail(user.getUserId(), "any-token");

    // then
    assertFalse(emailConfirmed);
    assertFalse(user.isEmailConfirmed());
    verify(userRepository, never()).save(user);
    verifyNoInteractions(securityTokenService);
    verifyNoInteractions(mailService);
  }

  /**
   * verifies that an email address is already confirmed by attempting to confirm it
   * with a previously generated security token and checking if the user is updated in
   * the repository.
   */
  @Test
  void confirmEmailAlreadyConfirmed() {
    // given
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.EMAIL_CONFIRM, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN,
            user);
    user.getUserTokens().add(testSecurityToken);
    user.setEmailConfirmed(true);
    given(userRepository.findByUserIdWithTokens(user.getUserId()))
        .willReturn(Optional.of(user));

    // when
    boolean emailConfirmed =
        userService.confirmEmail(user.getUserId(), testSecurityToken.getToken());

    // then
    assertFalse(emailConfirmed);
    verify(userRepository, never()).save(user);
    verifyNoInteractions(securityTokenService);
    verifyNoInteractions(mailService);
  }

  /**
   * verifies that user service can successfully find a user by email using repository
   * and mapper methods.
   */
  @Test
  void findUserByEmailSuccess() {
    // given
    UserDto userDto = getDefaultUserDtoRequest();
    User user = getUserFromDto(userDto);

    given(userRepository.findByEmail(USER_EMAIL))
        .willReturn(user);
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> resultUserDtoOptional = userService.findUserByEmail(USER_EMAIL);

    // then
    assertTrue(resultUserDtoOptional.isPresent());
    UserDto createdUserDto = resultUserDtoOptional.get();
    assertEquals(userDto, createdUserDto);
    assertEquals(0, createdUserDto.getCommunityIds().size());
    verify(userRepository).findByEmail(USER_EMAIL);
  }

  /**
   * finds a user by email and returns their communities IDs in a set. It uses mocking
   * to verify the calls made to the user repository and mapper.
   */
  @Test
  void findUserByEmailSuccessWithCommunityIds() {
    // given
    UserDto userDto = getDefaultUserDtoRequest();
    User user = getUserFromDto(userDto);

    Community firstCommunity = TestUtils.CommunityHelpers.getTestCommunity(user);
    Community secCommunity = TestUtils.CommunityHelpers.getTestCommunity(user);

    Set<Community> communities =
        Stream.of(firstCommunity, secCommunity).collect(Collectors.toSet());

    Set<String> communitiesIds = communities
        .stream()
        .map(Community::getCommunityId)
        .collect(Collectors.toSet());

    given(userRepository.findByEmail(USER_EMAIL))
        .willReturn(user);
    given(userMapper.userToUserDto(user))
        .willReturn(userDto);

    // when
    Optional<UserDto> resultUserDtoOptional = userService.findUserByEmail(USER_EMAIL);

    // then
    assertTrue(resultUserDtoOptional.isPresent());
    UserDto createdUserDto = resultUserDtoOptional.get();
    assertEquals(userDto, createdUserDto);
    assertEquals(communitiesIds, createdUserDto.getCommunityIds());
    verify(userRepository).findByEmail(USER_EMAIL);
  }

  /**
   * verifies that a user is not found by their email address in the repository, and
   * asserts that the optional result from the service call is empty.
   */
  @Test
  void findUserByEmailNotFound() {
    // given
    given(userRepository.findByEmail(USER_EMAIL))
        .willReturn(null);

    // when
    Optional<UserDto> resultUserDtoOptional = userService.findUserByEmail(USER_EMAIL);

    // then
    assertFalse(resultUserDtoOptional.isPresent());
    verify(userRepository).findByEmail(USER_EMAIL);
  }

  /**
   * resets a user's password using their email address and a security token sent via
   * mail. It creates a new security token, saves the updated user record, sends an
   * email with a password recover code, and verifies related method calls.
   */
  @Test
  void requestResetPassword() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, null);
    given(securityTokenService.createPasswordResetToken(user))
        .willReturn(testSecurityToken);
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));
    given(mailService.sendPasswordRecoverCode(user, testSecurityToken.getToken()))
        .willReturn(true);

    // when
    boolean resetRequested = userService.requestResetPassword(forgotPasswordRequest);

    // then
    assertTrue(resetRequested);
    assertEquals(getUserSecurityToken(user, SecurityTokenType.RESET), testSecurityToken);
    verify(securityTokenService).createPasswordResetToken(user);
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verify(userRepository).save(user);
    verify(mailService).sendPasswordRecoverCode(user, testSecurityToken.getToken());
  }

  /**
   * verifies that a password reset request is not triggered when the user does not
   * exist in the system.
   */
  @Test
  void requestResetPasswordUserNotExists() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, user);
    given(securityTokenService.createPasswordResetToken(user))
        .willReturn(testSecurityToken);
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.empty());

    // when
    boolean resetRequested = userService.requestResetPassword(forgotPasswordRequest);

    // then
    assertFalse(resetRequested);
    assertNotEquals(getUserSecurityToken(user, SecurityTokenType.RESET), testSecurityToken);
    verifyNoInteractions(securityTokenService);
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verify(userRepository, never()).save(user);
    verifyNoInteractions(mailService);
  }

  /**
   * resets a user's password by generating a new security token, encoder the new
   * password, and send a password change confirmation email. It also saves the updated
   * user object in the repository and returns true if the password was successfully changed.
   */
  @Test
  void resetPassword() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, user);
    user.getUserTokens().add(testSecurityToken);
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));
    given(passwordEncoder.encode(forgotPasswordRequest.getNewPassword()))
        .willReturn(forgotPasswordRequest.getNewPassword());
    when(userRepository.save(user))
        .then(returnsFirstArg());
    given(mailService.sendPasswordSuccessfullyChanged(user))
        .willReturn(true);
    given(securityTokenService.useToken(testSecurityToken))
        .willReturn(testSecurityToken);

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertTrue(passwordChanged);
    assertEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verify(passwordEncoder).encode(forgotPasswordRequest.getNewPassword());
    verify(mailService).sendPasswordSuccessfullyChanged(user);
    verify(securityTokenService).useToken(testSecurityToken);
  }

  /**
   * verifies that a user does not exist when attempting to reset their password, and
   * returns false and the original password is not updated.
   */
  @Test
  void resetPasswordUserNotExists() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, user);
    user.getUserTokens().add(testSecurityToken);
    ;
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.empty());

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  /**
   * tests whether resetting a password for an expired security token results in the
   * expected behavior, including failure to change the password and marking the security
   * token as unused.
   */
  @Test
  void resetPasswordTokenExpired() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    SecurityToken testSecurityToken = getExpiredTestToken();
    User user = getDefaultUser();
    user.getUserTokens().add(testSecurityToken);
    ;
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    assertFalse(getUserSecurityToken(user, SecurityTokenType.RESET).isUsed());
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  /**
   * tests the user service's ability to reset a password for an email address that
   * does not have a password reset token associated with it.
   */
  @Test
  void resetPasswordTokenNotExists() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    User user = getDefaultUser();
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  /**
   * verifies that the provided token does not match the expected reset password token,
   * and returns false when attempting to reset the password using the incorrect token.
   */
  @Test
  void resetPasswordTokenNotMatches() {
    // given
    ForgotPasswordRequest forgotPasswordRequest = getForgotPasswordRequest();
    SecurityToken testSecurityToken =
        getSecurityToken(SecurityTokenType.RESET, TOKEN_LIFETIME, PASSWORD_RESET_TOKEN, null);
    testSecurityToken.setToken("wrong-token");
    User user = getDefaultUser();
    user.getUserTokens().add(testSecurityToken);
    ;
    given(userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail()))
        .willReturn(Optional.of(user));

    // when
    boolean passwordChanged = userService.resetPassword(forgotPasswordRequest);

    // then
    assertFalse(passwordChanged);
    assertNotEquals(user.getEncryptedPassword(), forgotPasswordRequest.getNewPassword());
    assertNotNull(getUserSecurityToken(user, SecurityTokenType.RESET));
    verify(userRepository).findByEmailWithTokens(forgotPasswordRequest.getEmail());
    verifyNoInteractions(securityTokenRepository);
    verifyNoInteractions(passwordEncoder);
    verifyNoInteractions(mailService);
  }

  /**
   * generates a default UserDto instance with predefined fields based on user-specific
   * values for user ID, name, email, password, and community IDs.
   * 
   * @returns a `UserDto` object populated with default values for a user.
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
   * converts a `UserDto` object into a `User` object by extracting name, user ID,
   * email, and password from the DTO, and setting encrypted password and hash sets accordingly.
   * 
   * @param request `UserDto` object that contains the user's details, which are then
   * used to create a new `User` object.
   * 
   * @returns a `User` object with fields for name, user ID, email, and encrypted password.
   */
  private User getUserFromDto(UserDto request) {
    return new User(
        request.getName(),
        request.getUserId(),
        request.getEmail(),
        false,
        request.getEncryptedPassword(),
        new HashSet<>(),
        new HashSet<>()
    );
  }

  /**
   * retrieves a user's security token based on the specified `tokenType`. It streams
   * through the user's token collection, filters by token type, and returns the first
   * matching token or `null` if none found.
   * 
   * @param user user for whom the security token is being retrieved, and it is used
   * to filter the user's token collection to find the matching token of the required
   * type.
   * 
   * @param tokenType type of security token to be retrieved, and it is used to filter
   * the stream of user tokens to only include tokens with the specified type.
   * 
   * @returns a `SecurityToken` object representing the user's security token of the
   * specified type, or `null` if no such token exists.
   */
  private SecurityToken getUserSecurityToken(User user, SecurityTokenType tokenType) {
    return user.getUserTokens()
        .stream()
        .filter(token -> token.getTokenType() == tokenType)
        .findFirst()
        .orElse(null);
  }

  /**
   * retrieves a default user object from a request containing the necessary details
   * for the user, such as name and email address.
   * 
   * @returns a `User` object representing the default user for the application.
   */
  private User getDefaultUser() {
    return getUserFromDto(getDefaultUserDtoRequest());
  }

  /**
   * creates a new `ForgotPasswordRequest` object with specified email, new password
   * and token for password reset.
   * 
   * @returns a `ForgotPasswordRequest` object containing the user's email, new password,
   * and password reset token.
   */
  private ForgotPasswordRequest getForgotPasswordRequest() {
    ForgotPasswordRequest request = new ForgotPasswordRequest();
    request.setEmail(USER_EMAIL);
    request.setNewPassword(NEW_USER_PASSWORD);
    request.setToken(PASSWORD_RESET_TOKEN);
    return request;
  }

  /**
   * generates a test security token with an expiration date set to the current date
   * minus a specified number of days, and sets the token status to invalid.
   * 
   * @returns a SecurityToken object representing an expired token.
   */
  private SecurityToken getExpiredTestToken() {
    return new SecurityToken(SecurityTokenType.RESET, PASSWORD_RESET_TOKEN, LocalDate.now(),
        LocalDate.now().minusDays(TOKEN_LIFETIME.toDays()), false, null);
  }

  /**
   * generates a new security token instance with specified type, token, and lifetime.
   * It also sets the expiration date to a future date based on the provided lifetime.
   * 
   * @param tokenType type of security token being generated, which determines the
   * format and content of the token.
   * 
   * @param lifetime duration of validity for the security token, which determines when
   * the token will expire.
   * 
   * @param token 128-bit security token value to be generated by the `getSecurityToken`
   * function.
   * 
   * @param user user who will use the generated security token.
   * 
   * @returns a new security token instance with the specified type, token, and expiration
   * date.
   */
  private SecurityToken getSecurityToken(SecurityTokenType tokenType, Duration lifetime,
      String token, User user) {
    LocalDate expireDate = LocalDate.now().plusDays(lifetime.toDays());
    return new SecurityToken(tokenType, token, LocalDate.now(), expireDate, false, user);
  }

  /**
   * creates a new security token with the specified type and token value, sets the
   * expiration date to one day from the current date, and sets the token as invalid
   * for login purposes.
   * 
   * @param tokenType type of security token being generated, which is used to determine
   * the characteristics of the token such as its validity period and user affiliation.
   * 
   * @param token 16-character alphanumeric string that is used to identify the security
   * token.
   * 
   * @param user user who will use the security token.
   * 
   * @returns a new `SecurityToken` object representing the specified type of token
   * with the provided token value and expiration date.
   */
  private SecurityToken getSecurityToken(SecurityTokenType tokenType, String token, User user) {
    LocalDate expireDate = LocalDate.now().plusDays(Duration.ofDays(1).toDays());
    return new SecurityToken(tokenType, token, LocalDate.now(), expireDate, false, user);
  }
}