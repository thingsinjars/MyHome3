/*
 * Copyright 2020 Prathab Murugan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.mapper.UserMapper;
import com.myhome.domain.Community;
import com.myhome.domain.SecurityToken;
import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.User;
import com.myhome.model.ForgotPasswordRequest;
import com.myhome.repositories.UserRepository;
import com.myhome.services.MailService;
import com.myhome.services.SecurityTokenService;
import com.myhome.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implements {@link UserService} and uses Spring Data JPA repository to does its work.
 */
/**
 * is an implementation of a service layer for handling user-related operations in a
 * Spring Boot application. It encapsulates the business logic and data access layers
 * to provide a simple and consistent interface for interacting with users. The class
 * provides methods for creating, updating, and retrieving user information, as well
 * as for confirming emails and generating unique user IDs. Additionally, it utilizes
 * various utility classes for password encoding and logging purposes.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserSDJpaService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final SecurityTokenService securityTokenService;
  private final MailService mailService;

  /**
   * creates a new user account by generating a unique ID, encrypting the password, and
   * storing it in the repository. It then sends an email confirmation token to the
   * user's registered email address.
   * 
   * @param request UserDto object containing the user's details to be created.
   * 
   * * `getEmail()`: The email address of the new user.
   * * `generateUniqueUserId()` and `encryptUserPassword()`: Two methods that generate
   * a unique ID for the user and encrypt their password, respectively. These methods
   * are called to create a new user in the repository.
   * * `createUserInRepository()`: A method that creates a new user in the repository.
   * * `securityTokenService.createEmailConfirmToken()`: A method that creates an email
   * confirmation token for the new user.
   * * `mailService.sendAccountCreated()`: A method that sends an email to the new user
   * with their account creation information and email confirmation token.
   * 
   * @returns an `Optional` containing a `UserDto` representation of the newly created
   * user.
   * 
   * * `Optional<UserDto>` represents an optional user object in the form of a DTO (Data
   * Transfer Object). If a user is created successfully, this will contain a non-empty
   * UserDto object. Otherwise, it will be empty.
   */
  @Override
  public Optional<UserDto> createUser(UserDto request) {
    if (userRepository.findByEmail(request.getEmail()) == null) {
      generateUniqueUserId(request);
      encryptUserPassword(request);
      User newUser = createUserInRepository(request);
      SecurityToken emailConfirmToken = securityTokenService.createEmailConfirmToken(newUser);
      mailService.sendAccountCreated(newUser, emailConfirmToken);
      UserDto newUserDto = userMapper.userToUserDto(newUser);
      return Optional.of(newUserDto);
    } else {
      return Optional.empty();
    }
  }

  /**
   * in Java returns a set of user objects based on a page request parameter.
   * 
   * @returns a set of `User` objects representing a collection of users.
   * 
   * * The `Set<User>` object represents a collection of user objects that contain
   * information about users in the system.
   * * The `PageRequest` parameter passed to the function is used to specify the page
   * number and page size for the users to be listed.
   * * The `listAll` function returns a `Set` containing all users that match the
   * specified page request.
   */
  @Override
  public Set<User> listAll() {
    return listAll(PageRequest.of(0, 200));
  }

  /**
   * performs a paginated query on the `User` repository using the `findAll` method and
   * returns a set of users.
   * 
   * @param pageable pagination information for retrieving a subset of users from the
   * repository, allowing for efficient and flexible access to the data.
   * 
   * * `Pageable`: A class that represents an abstraction of a page in a paginated
   * result set. It has various attributes and methods for controlling paging behavior.
   * 
   * @returns a set of `User` objects retrieved from the repository using the `findAll`
   * method with the provided pageable parameter.
   * 
   * The `Set<User>` object represents a collection of user objects that have been
   * fetched from the user repository using the `findAll` method and passed through the
   * `toSet()` method to convert the list into a set. This set contains all users
   * retrieved from the repository, regardless of whether they are active or inactive,
   * and their information is available for access and manipulation.
   */
  @Override
  public Set<User> listAll(Pageable pageable) {
    return userRepository.findAll(pageable).toSet();
  }

  /**
   * retrieves a user's details and communities from the repository, maps them to a
   * `UserDto`, and returns an optional instance of `UserDto`.
   * 
   * @param userId ID of the user whose details are to be retrieved.
   * 
   * @returns an optional instance of `UserDto` containing the user's community IDs and
   * details mapped from the user entity.
   * 
   * * The output is an `Optional` object that contains a `UserDto` instance or an empty
   * `Optional` if no user details could be found.
   * * The `UserDto` instance contains information about the user, such as their name
   * and email address.
   * * The `CommunityIds` attribute of the `UserDto` represents the set of community
   * IDs that the user is a part of.
   * * The `Optional.of(userDto)` statement creates an `Optional` object that contains
   * the `UserDto` instance.
   */
  @Override
  public Optional<UserDto> getUserDetails(String userId) {
    Optional<User> userOptional = userRepository.findByUserIdWithCommunities(userId);
    return userOptional.map(admin -> {
      Set<String> communityIds = admin.getCommunities().stream()
          .map(Community::getCommunityId)
          .collect(Collectors.toSet());

      UserDto userDto = userMapper.userToUserDto(admin);
      userDto.setCommunityIds(communityIds);
      return Optional.of(userDto);
    }).orElse(Optional.empty());
  }

  /**
   * returns an optional `UserDto` object containing the user's community IDs, based
   * on the user's email and the `userRepository` and `mapper` classes.
   * 
   * @param userEmail email address of the user for which the method is searching in
   * the user repository.
   * 
   * @returns an `Optional` instance containing a `UserDto` object with the user's
   * community IDs.
   * 
   * * `Optional<UserDto>` - This is the type of the output, which represents an optional
   * user object with additional community ID information.
   * * `userRepository.findByEmail(userEmail)` - This is a method call that retrieves
   * a user object from the repository based on the provided email address.
   * * `map(user -> { ... }) ` - This is a lambda expression that transforms the retrieved
   * user object into a UserDto object with additional community ID information. The
   * lambda expression takes the retrieved user object as input and returns a UserDto
   * object after applying some transformations.
   * * `UserDto` - This is the type of the transformed UserDto object, which contains
   * additional community ID information.
   */
  public Optional<UserDto> findUserByEmail(String userEmail) {
    return Optional.ofNullable(userRepository.findByEmail(userEmail))
        .map(user -> {
          Set<String> communityIds = user.getCommunities().stream()
              .map(Community::getCommunityId)
              .collect(Collectors.toSet());

          UserDto userDto = userMapper.userToUserDto(user);
          userDto.setCommunityIds(communityIds);
          return userDto;
        });
  }

  /**
   * returns a boolean value based on the email provided in the `ForgotPasswordRequest`.
   * It retrieves the user from the database using the email, creates a new password
   * reset token with the security token service, adds it to the user's tokens, and
   * then sends an email with the token to the user.
   * 
   * @param forgotPasswordRequest request for resetting a password, containing the email
   * address of the user to whom the password reset link should be sent.
   * 
   * * `getEmail()` returns the email address associated with the request.
   * * `userRepository.findByEmailWithTokens(email)` retrieves the user associated with
   * the provided email address and retrieves any existing tokens for that user.
   * * `securityTokenService.createPasswordResetToken(user)` creates a new security
   * token for password reset.
   * * `user.getUserTokens().add(newSecurityToken)` adds the newly created token to the
   * user's token list.
   * * `userRepository.save(user)` saves the updated user entity in the database.
   * * `mailService.sendPasswordRecoverCode(user, newSecurityToken.getToken())` sends
   * a password recovery code to the user's registered email address.
   * 
   * @returns a boolean value indicating whether a password reset link was sent to the
   * user's email address.
   */
  @Override
  public boolean requestResetPassword(ForgotPasswordRequest forgotPasswordRequest) {
    return Optional.ofNullable(forgotPasswordRequest)
        .map(ForgotPasswordRequest::getEmail)
        .flatMap(email -> userRepository.findByEmailWithTokens(email)
            .map(user -> {
              SecurityToken newSecurityToken = securityTokenService.createPasswordResetToken(user);
              user.getUserTokens().add(newSecurityToken);
              userRepository.save(user);
              return mailService.sendPasswordRecoverCode(user, newSecurityToken.getToken());
            }))
        .orElse(false);
  }

  /**
   * resets a user's password by checking if there is a valid token for the given email,
   * finding the user with the token, and then saving a new token for the user and
   * sending a notification to the user that their password has been successfully changed.
   * 
   * @param passwordResetRequest Forgot Password Request object, which contains the
   * email address of the user attempting to reset their password and a token provided
   * by the user for verification purposes.
   * 
   * * `ForgotPasswordRequest`: This class contains an email address and a token, which
   * is used to identify the user requesting password reset.
   * * `getEmail()`: Returns the email address associated with the password reset request.
   * * `getToken()`: Returns the token provided by the user for password reset.
   * * `findByEmailWithTokens()`: This method retrieves a User object from the database
   * based on the email address associated with the password reset request. The method
   * takes a token as an argument, which is used to filter the results to only include
   * users with valid tokens.
   * * `findValidUserToken(token, user, SecurityTokenType)`: This method retrieves a
   * security token from the database that is associated with the provided user and
   * token type. The method returns a Optional<SecurityToken> object, which can be used
   * to validate the token.
   * * `useToken()`: This method validates the security token retrieved by the
   * `findValidUserToken` method. If the token is invalid or expired, it returns an
   * error message. Otherwise, it returns a successful response.
   * * `saveTokenForUser(user, newPassword)`: This method saves a new security token
   * for the user in the database, along with their updated password. The method takes
   * two arguments: the User object and the new password.
   * * `sendPasswordSuccessfullyChanged()`: This method sends an email to the user
   * indicating that their password has been successfully changed.
   * 
   * @returns a boolean value indicating whether the password reset process was successful.
   */
  @Override
  public boolean resetPassword(ForgotPasswordRequest passwordResetRequest) {
    final Optional<User> userWithToken = Optional.ofNullable(passwordResetRequest)
        .map(ForgotPasswordRequest::getEmail)
        .flatMap(userRepository::findByEmailWithTokens);
    return userWithToken
        .flatMap(user -> findValidUserToken(passwordResetRequest.getToken(), user, SecurityTokenType.RESET))
        .map(securityTokenService::useToken)
        .map(token -> saveTokenForUser(userWithToken.get(), passwordResetRequest.getNewPassword()))
        .map(mailService::sendPasswordSuccessfullyChanged)
        .orElse(false);
  }

  /**
   * verifies if an email address is confirmed for a user by checking their repository,
   * token, and security service. If the email address is confirmed, it updates the
   * user's status and returns a confirmation token.
   * 
   * @param userId identifier of the user for whom the email confirmation is being checked.
   * 
   * @param emailConfirmToken confirmation token for the user's email address, which
   * is used to check if the user's email has been confirmed or not.
   * 
   * @returns a boolean value indicating whether the email confirmation process was
   * successful for the provided user.
   * 
   * * `token`: This is the SecurityToken object that was used to confirm the email address.
   * * `true`: This indicates whether the email confirmation was successful or not. If
   * `token` is null, then the confirmation failed.
   * 
   * The function returns a `Optional<SecurityToken>` object, which contains the
   * SecurityToken if it exists, and `null` otherwise. The `map` method is used to
   * transform the `Optional` object into a `Boolean` value, based on whether the token
   * was successfully used for email confirmation.
   */
  @Override
  public Boolean confirmEmail(String userId, String emailConfirmToken) {
    final Optional<User> userWithToken = userRepository.findByUserIdWithTokens(userId);
    Optional<SecurityToken> emailToken = userWithToken
        .filter(user -> !user.isEmailConfirmed())
        .map(user -> findValidUserToken(emailConfirmToken, user, SecurityTokenType.EMAIL_CONFIRM)
        .map(token -> {
          confirmEmail(user);
          return token;
        })
        .map(securityTokenService::useToken)
        .orElse(null));
    return emailToken.map(token -> true).orElse(false);
  }

  /**
   * resends an email confirmation token to a user if they have not confirmed their
   * email address.
   * 
   * @param userId unique identifier of the user for whom the email confirmation process
   * is being performed.
   * 
   * @returns a boolean value indicating whether an email confirmation token was sent
   * successfully.
   */
  @Override
  public boolean resendEmailConfirm(String userId) {
    return userRepository.findByUserId(userId).map(user -> {
      if(!user.isEmailConfirmed()) {
        SecurityToken emailConfirmToken = securityTokenService.createEmailConfirmToken(user);
        user.getUserTokens().removeIf(token -> token.getTokenType() == SecurityTokenType.EMAIL_CONFIRM && !token.isUsed());
        userRepository.save(user);
        boolean mailSend = mailService.sendAccountCreated(user, emailConfirmToken);
        return mailSend;
      } else {
        return false;
      }
    }).orElse(false);
  }

  /**
   * updates a user's encrypted password and saves the user to the repository, returning
   * the updated user.
   * 
   * @param user User object to be updated with a new encrypted password.
   * 
   * * `user`: The User object to be saved with an updated encrypted password. It has
   * various attributes including `id`, `username`, `password`, and `email`.
   * 
   * @param newPassword encrypted password for the user being saved, which is then
   * encoded and saved to the database through the `passwordEncoder.encode()` method
   * and `userRepository.save()` method respectively.
   * 
   * @returns a saved `User` entity with an encrypted password.
   * 
   * * The `User` object is modified by setting its `encryptedPassword` field to an
   * encoded version of the input `newPassword`.
   * * The `User` object is persisted in the repository using the `save` method.
   * 
   * The function does not provide any information about the licensing or authorship
   * of the code.
   */
  private User saveTokenForUser(User user, String newPassword) {
    user.setEncryptedPassword(passwordEncoder.encode(newPassword));
    return userRepository.save(user);
  }

  /**
   * searches for a SecurityToken within a User's token collection that meets the
   * specified criteria: non-used, matching token type, and matching token value, with
   * an expiration date after the current date.
   * 
   * @param token token that the function is searching for in the `user.getUserTokens()`
   * stream.
   * 
   * @param user User object that is being searched for a valid security token.
   * 
   * * `user`: The user object, which contains various attributes such as `getUserTokens()`
   * stream, `isUsed()`, `getTokenType()`, `getToken()`, and `getExpiryDate()`.
   * 
   * @param securityTokenType type of security token being searched for, which is used
   * to filter the user tokens in the stream to only those with the matching token type.
   * 
   * * `isUsed()` - A boolean indicating whether the token has been used or not.
   * * `getTokenType()` - The type of the token, which can be one of the predefined
   * constants in the `SecurityTokenType` class.
   * * `getToken()` - The actual token value.
   * * `getExpiryDate()` - The date and time when the token expires.
   * 
   * These properties are used to filter and find the matching `SecurityToken` object
   * in the `user.getUserTokens()` stream.
   * 
   * @returns an `Optional` of a `SecurityToken` if a valid token is found, otherwise
   * `Optional.empty`.
   * 
   * * `Optional<SecurityToken>`: The output is an optional SecurityToken object, which
   * means that it may be present or absent depending on the input parameters.
   * * `userPasswordResetToken`: This is a stream of UserTokens that contains the user's
   * password reset tokens.
   * * `filter()`: This method filters the stream of UserTokens to only include those
   * that meet the specified conditions.
   * * `findFirst()`: This method finds the first UserToken that meets the conditions
   * specified in the `filter()` method.
   * * `isUsed()`: This property indicates whether a UserToken has been used or not.
   * * `getTokenType()`: This property returns the type of SecurityToken represented
   * by the UserToken object.
   * * `getToken()`: This property returns the actual SecurityToken value represented
   * by the UserToken object.
   * * `getExpiryDate()`: This property returns the expiration date of the SecurityToken,
   * which is compared to the current date using the `isAfter()` method.
   */
  private Optional<SecurityToken> findValidUserToken(String token, User user, SecurityTokenType securityTokenType) {
    Optional<SecurityToken> userPasswordResetToken = user.getUserTokens()
        .stream()
        .filter(tok -> !tok.isUsed()
            && tok.getTokenType() == securityTokenType
            && tok.getToken().equals(token)
            && tok.getExpiryDate().isAfter(LocalDate.now()))
        .findFirst();
    return userPasswordResetToken;
  }

  /**
   * creates a new user object based on a `UserDto` input, saves it to the repository,
   * and logs a trace message with the user ID.
   * 
   * @param request UserDto object containing the data for the new user to be created
   * in the repository.
   * 
   * * `request`: A `UserDto` object containing user details for creation in the repository.
   * 
   * @returns a saved User object in the repository.
   * 
   * * `User user`: The created user object.
   * * `userMapper.userDtoToUser(request)`: The conversion of the `UserDto` object to
   * a `User` object using the `userMapper`.
   * * `log.trace("saving user with id[{}] to repository", request.getId())`: A log
   * statement indicating that the user is being saved to the repository with its ID.
   */
  private User createUserInRepository(UserDto request) {
    User user = userMapper.userDtoToUser(request);
    log.trace("saving user with id[{}] to repository", request.getId());
    return userRepository.save(user);
  }

  /**
   * updates a `User` object's `emailConfirmed` field to `true`, sends an account
   * confirmation notification to the user using the `mailService`, and saves the updated
   * `User` object in the repository.
   * 
   * @param user User object to be updated with the confirmed email status, and is used
   * in the function to set the `emailConfirmed` field to `true`, send an account
   * confirmation notification via the `mailService`, and save the updated user object
   * in the `userRepository`.
   * 
   * * `user.setEmailConfirmed(true)`: This line updates the `emailConfirmed` field of
   * the `User` object to `true`.
   * * `mailService.sendAccountConfirmed(user)`: This line sends an email to the user's
   * registered email address, confirming their account.
   * * `userRepository.save(user)`: This line saves the updated `User` object in the
   * repository, allowing for further processing or storage as needed.
   */
  private void confirmEmail(User user) {
    user.setEmailConfirmed(true);
    mailService.sendAccountConfirmed(user);
    userRepository.save(user);
  }

  /**
   * encodes a user's password using a password encoder and stores the encoded password
   * in the `UserDto`.
   * 
   * @param request UserDto object containing the user's password to be encrypted, and
   * its `setEncryptedPassword()` method sets the encrypted password value of the object.
   * 
   * * `request`: A `UserDto` object that contains the user's password and other relevant
   * information.
   * * `passwordEncoder`: An instance of a `PasswordEncoder` class, which is responsible
   * for encrypting the user's password.
   * * `setEncryptedPassword`: A method that sets the encrypted version of the user's
   * password in the `request` object.
   */
  private void encryptUserPassword(UserDto request) {
    request.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
  }

  /**
   * generates a unique user ID for a `UserDto` object using the `UUID.randomUUID()`
   * method and assigns it to the `UserDto` object's `userId` field.
   * 
   * @param request UserDto object containing information about the user for whom a
   * unique ID is being generated.
   * 
   * * `request`: This is an instance of the `UserDto` class, which contains various
   * attributes related to user data.
   */
  private void generateUniqueUserId(UserDto request) {
    request.setUserId(UUID.randomUUID().toString());
  }
}
