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
 * TODO
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
   * creates a new user in the system by generating a unique ID, encrypting their
   * password, and creating a security token for email confirmation. It then sends an
   * account creation email to the user with the security token included. Finally, it
   * maps the newly created user object to a `UserDto` object and returns it as an Optional.
   * 
   * @param request user creation request, containing the user's email and other relevant
   * information.
   * 
   * 	- `getEmail()`: The email address of the user to be created.
   * 	- `generateUniqueUserId()`: A method that generates a unique ID for the user.
   * 	- `encryptUserPassword()`: A method that encrypts the user's password.
   * 	- `createUserInRepository()`: A method that creates a new user object in the repository.
   * 	- `securityTokenService.createEmailConfirmToken()`: A method that creates an email
   * confirmation token for the new user.
   * 	- `mailService.sendAccountCreated()`: A method that sends an account creation
   * confirmation email to the user's registered email address.
   * 
   * The function returns an optional `UserDto` representing the created user object,
   * or an empty optional if the email already exists in the system.
   * 
   * @returns an `Optional<UserDto>` containing the created user's details if a unique
   * email address was not already in use, otherwise it is empty.
   * 
   * 	- The `Optional<UserDto>` return type indicates that the function may return an
   * optional instance of `UserDto`, which means that if no user is created successfully,
   * the function will return an empty Optional.
   * 	- The method first checks whether a user with the provided email address already
   * exists in the repository by calling `userRepository.findByEmail(request.getEmail())`.
   * If such a user exists, the function returns an empty Optional.
   * 	- If no user exists with the provided email address, the method generates a unique
   * user ID using the `generateUniqueUserId` method and then encrypts the user password
   * using the `encryptUserPassword` method.
   * 	- The function then creates a new user object in the repository by calling
   * `createUserInRepository(request)`, which may return an instance of `User`.
   * 	- Next, the method creates an email confirmation token using the
   * `securityTokenService.createEmailConfirmToken(newUser)` method and sends it to the
   * user's registered email address using the `mailService.sendAccountCreated(newUser,
   * emailConfirmToken)` method.
   * 	- Finally, the function maps the newly created user object to a `UserDto` instance
   * using the `userMapper.userToUserDto(newUser)` method and returns an Optional
   * containing the `UserDto` instance as its value.
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
   * retrieves a set of `User` objects from the backing data store and returns them to
   * the caller.
   * 
   * @returns a set of `User` objects representing a paginated list of users.
   * 
   * 	- The output is a `Set<User>` data structure, indicating that it contains a
   * collection of user objects.
   * 	- The elements in the set are represented by instances of the `User` class, which
   * contain information about individual users.
   * 	- The `listAll` function returns a page of results, as indicated by the `PageRequest`
   * parameter passed to it. The page size is 200, indicating that the function returns
   * a maximum of 200 user objects in each page.
   * 	- The output does not include any additional information about the users, such
   * as their names or email addresses.
   */
  @Override
  public Set<User> listAll() {
    return listAll(PageRequest.of(0, 200));
  }

  /**
   * from the provided Java code returns a set of all users retrieved from the `userRepository`.
   * 
   * @param pageable pagination information for retrieving a subset of users from the
   * repository.
   * 
   * The `Pageable` interface provides a way to paginate a sequence of objects. It
   * contains the `getPageNumber()` and `getPageSize()` methods that allow users to
   * navigate through the collection. Additionally, the `toSet()` method returns a set
   * containing all the elements in the collection.
   * 
   * @returns a set of `User` objects retrieved from the database using the
   * `userRepository.findAll()` method and passed as a pageable parameter.
   * 
   * 	- The output is a `Set` of `User` objects. This indicates that the function returns
   * a collection of user objects, where each user object represents a unique user in
   * the system.
   * 	- The `pageable` parameter is passed to the `findAll` method of the `userRepository`.
   * This suggests that the function is designed to work with pagination, allowing for
   * efficient retrieval of large sets of user data.
   * 	- The returned `Set` contains all the user objects that match the query, regardless
   * of whether they are present on the current page or not. This implies that the
   * function returns a complete set of users, rather than just those present in the
   * current page of results.
   */
  @Override
  public Set<User> listAll(Pageable pageable) {
    return userRepository.findAll(pageable).toSet();
  }

  /**
   * retrieves a user's details from the repository and communities, maps them to a
   * `UserDto`, and returns an optional instance of `UserDto`.
   * 
   * @param userId unique identifier of the user for whom the details are being retrieved.
   * 
   * 	- `userOptional`: This is an optional instance of `User`, which represents a user
   * object that can be obtained from the database using the `userRepository.findByUserIdWithCommunities()`
   * method.
   * 	- `admin`: This is the actual user object that is returned by the `userOptional`.
   * 	- `communityIds`: This is a set of community IDs that belong to the user.
   * 	- `userMapper`: This is an instance of `UserMapper`, which is responsible for
   * mapping the user object to a `UserDto` object.
   * 
   * @returns an optional object containing a user details DTO and a set of community
   * IDs.
   * 
   * 	- The function returns an `Optional` object containing a `UserDto` instance. This
   * indicates that the function may or may not return a valid user details object,
   * depending on whether a user with the provided ID exists in the database.
   * 	- The `UserDto` instance contains information about the user, such as their name
   * and community IDs.
   * 	- The `communityIds` attribute of the `UserDto` instance is a set of strings
   * representing the IDs of the communities to which the user belongs. This property
   * is computed by mapping the communities belonging to each user in the database to
   * their corresponding IDs using the `getCommunityId()` method of the `Community` class.
   * 	- The function returns an `Optional` object with either a valid `UserDto` instance
   * or `Optional.empty()`, indicating whether a user details object was found or not.
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
   * retrieves a user from the repository based on their email address, maps the user
   * to a `UserDto` object, and populates the `CommunityIds` field of the `UserDto`
   * with the IDs of the communities the user is a member of.
   * 
   * @param userEmail email address of the user to find in the user repository.
   * 
   * 	- `userEmail`: A string parameter representing an email address of a user to be
   * found in the repository.
   * 
   * @returns an optional UserDto object containing the user's community IDs.
   * 
   * 	- The Optional object returned is an instance of the `Optional` class in Java,
   * which can contain either a value or be empty.
   * 	- If the value is present, it is a `UserDto` object representing a user in the system.
   * 	- The `UserDto` object has several properties, including:
   * 	+ `id`: an integer representing the user's unique identifier.
   * 	+ `email`: a string representing the user's email address.
   * 	+ `name`: a string representing the user's name.
   * 	+ `communities`: a set of strings representing the community IDs that the user
   * is part of.
   * 	- The `findByEmail` method returns an Optional object after mapping the original
   * query result to a `UserDto` object using the `userMapper` function. This function
   * transforms the `User` entity into a `UserDto` object with additional attributes
   * for community membership.
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
   * takes a `ForgotPasswordRequest` object and resets the password for a user based
   * on their email address, creating a new security token and sending a password
   * recovery code to the user's registered email address.
   * 
   * @param forgotPasswordRequest email address of the user who is requesting to reset
   * their password, which is used as the primary key for querying and updating the
   * user's tokens and related data in the database.
   * 
   * 	- `forgotPasswordRequest`: This is the object containing the email address of the
   * user requesting password reset.
   * 	- `getEmail()`: It retrieves the email address from the `ForgotPasswordRequest`
   * object.
   * 	- `userRepository.findByEmailWithTokens(email)`: This method retrieves the user
   * associated with the provided email address by checking if a token is available for
   * that email address in the `UserTokens` table.
   * 	- `map(user -> { ... })`: It maps the retrieved user object to a new security
   * token object, which contains a unique token for password reset.
   * 	- `securityTokenService.createPasswordResetToken(user)`: This method creates a
   * new security token for password reset based on the user's information.
   * 	- `user.getUserTokens().add(newSecurityToken)`: It adds the newly created security
   * token to the user's token list.
   * 	- `userRepository.save(user)`: It saves the updated user object in the database,
   * which persists the changes made to the user's token list.
   * 	- `mailService.sendPasswordRecoverCode(user, newSecurityToken.getToken())`: This
   * method sends an email with a password reset token to the user's registered email
   * address.
   * 
   * @returns a boolean value indicating whether the password reset process was successful.
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
   * performs a multi-step process to reset a user's password based on a token provided
   * by the user. It first retrieves the user with the matching email address, then
   * verifies the token and saves a new one for the user if successful.
   * 
   * @param passwordResetRequest Forgot Password request from the user, containing the
   * email and the token provided by the user for password reset.
   * 
   * 	- `ForgotPasswordRequest`: This class represents a request for resetting a user's
   * password.
   * 	- `getEmail()`: Returns the email address of the user who made the request.
   * 	- `getToken()`: Returns the token provided by the user for resetting their password.
   * 	- `getNewPassword()`: Returns the new password that the user wants to set.
   * 
   * The function then proceeds with checking if a valid user token exists, using the
   * `findByEmailWithTokens` method of the `userRepository`. If a valid token is found,
   * the function checks if the provided token is of the correct type (i.e.,
   * `SecurityTokenType.RESET`) using the `securityTokenService`, and then saves the
   * updated token for the user in the `saveTokenForUser` method. Finally, the function
   * sends an email to the user indicating that their password has been successfully
   * changed using the `mailService`.
   * 
   * @returns a boolean value indicating whether the password reset was successful.
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
   * verifies an email address for a user by checking if the token provided matches a
   * valid token in the user's profile. If a match is found, the email is confirmed and
   * a new security token is generated for the user.
   * 
   * @param userId user for whom the email confirmation status is being checked.
   * 
   * 	- `userId`: This is a String input parameter representing the unique identifier
   * of a user in the system.
   * 
   * The function first retrieves the user object from the `userRepository` using the
   * `userId`, and then filters out the users who have already confirmed their email
   * by checking the `isEmailConfirmed()` method.
   * 
   * The function then maps over the remaining users and checks if they have a valid
   * email confirmation token using the `findValidUserToken()` method. If a valid token
   * is found, the function calls the `confirmEmail()` method on the user object and
   * retrieves the security token using the `useToken()` method provided by the `securityTokenService`.
   * 
   * Finally, the function returns a Boolean value indicating whether the email
   * confirmation was successful (true) or not (false).
   * 
   * @param emailConfirmToken token that is sent to the user's email address for
   * confirmation of their email address.
   * 
   * 	- `userId`: The unique identifier of the user associated with the email confirmation
   * token.
   * 	- `emailConfirmToken`: A string that represents the email confirmation token.
   * 	- `SecurityTokenType.EMAIL_CONFIRM`: An enumeration value indicating that the
   * token is for email confirmation.
   * 
   * @returns a boolean value indicating whether the email confirmation process was
   * successful or not.
   * 
   * 	- `map(token -> true).orElse(false)`: This method returns `true` if the email
   * confirmation is successful, otherwise it returns `false`.
   * 	- `Optional<SecurityToken> emailToken`: This represents the Security Token generated
   * by the function for email confirmation. If the email confirmation is successful,
   * this will contain a non-null value. Otherwise, it will be empty.
   * 	- `filter(user -> !user.isEmailConfirmed())`: This method filters the user
   * repository to find only those users who have not confirmed their email yet.
   * 	- `map(user -> findValidUserToken(emailConfirmToken, user, SecurityTokenType.EMAIL_CONFIRM))`:
   * This method maps each user to a Security Token that can be used for email confirmation.
   * If the email confirmation is successful, this will contain a non-null value.
   * Otherwise, it will be empty.
   * 	- `useToken(securityTokenService::useToken)`: This method calls the `useToken`
   * method of the `SecurityTokenService` class to use the generated Security Token for
   * email confirmation.
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
   * @param userId ID of the user for whom an email confirmation token is to be resent.
   * 
   * 	- `userRepository`: This represents the repository interface for accessing the
   * user data stored in the database.
   * 	- `securityTokenService`: This is an interface that provides methods for creating
   * and managing security tokens, such as the email confirmation token.
   * 	- `mailService`: This is an interface that provides methods for sending emails
   * to users.
   * 	- `userRepository.findByUserId(userId)`: This method retrieves a user object from
   * the database based on the input `userId`.
   * 	- `map(user -> { ... })`: This method applies a function to the user object, which
   * in this case is a mapping operation that checks if the user's email confirmation
   * status is not confirmed and removes any existing email confirmation token if it exists.
   * 	- `SecurityToken emailConfirmToken = securityTokenService.createEmailConfirmToken(user)`:
   * This method creates a new email confirmation token for the user using the `securityTokenService`.
   * 	- `user.getUserTokens().removeIf(token -> token.getTokenType() ==
   * SecurityTokenType.EMAIL_CONFIRM && !token.isUsed())`: This method removes any
   * existing email confirmation token from the user's list of tokens if it exists and
   * is not used.
   * 	- `userRepository.save(user)`: This method saves the updated user object in the
   * database.
   * 	- `mailSend = mailService.sendAccountCreated(user, emailConfirmToken)`: This
   * method sends an email to the user with the created email confirmation token.
   * 	- `orElse(false)`: This method returns the result of the `map` method if it is
   * not null, or else returns false.
   * 
   * @returns a boolean value indicating whether an email confirmation token was sent
   * to the user.
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
   * saves a user's token by setting their encrypted password and saving the user object
   * to the repository.
   * 
   * @param user User object to be saved, which is updated with a new encrypted password
   * before being persisted in the database by the `saveTokenForUser()` function.
   * 
   * 	- `user`: The input parameter representing the User object to be saved.
   * 	- `newPassword`: The new password for the user's token, encoded using the `passwordEncoder`.
   * 
   * @param newPassword encrypted password for the user that is being saved.
   * 
   * 	- `newPassword`: The new password for the user to be saved.
   * 	- `passwordEncoder`: The encoder used to encrypt the password.
   * 
   * @returns a saved User object containing an encrypted password.
   * 
   * 	- The `User` object that is passed as an argument to the function is updated by
   * setting its `encryptedPassword` field to the encoded password using the `passwordEncoder`.
   * 	- The modified `User` object is then saved in the repository using the `save()`
   * method.
   * 	- The returned output is the saved `User` object.
   */
  private User saveTokenForUser(User user, String newPassword) {
    user.setEncryptedPassword(passwordEncoder.encode(newPassword));
    return userRepository.save(user);
  }

  /**
   * searches for a valid security token belonging to a given user, based on specified
   * criteria.
   * 
   * @param token token that is being searched for among the user's tokens.
   * 
   * 	- `token`: This is the token being searched for in the user's tokens collection.
   * It has various attributes such as `isUsed()`, `tokenType`, and `token` itself.
   * 	- `user`: This is the user whose tokens are being searched.
   * 	- `securityTokenType`: This specifies the type of security token being searched
   * for.
   * 
   * The function first filters the user's tokens based on the token type, then checks
   * if the token is unused and has the correct expiration date. If a matching token
   * is found, it returns an `Optional` containing that token.
   * 
   * @param user User object that is being searched for a valid security token.
   * 
   * 	- `user`: A `User` object representing the user for whom the token is being checked.
   * 	- `token`: The token value being checked against the user's tokens.
   * 	- `securityTokenType`: The type of security token being checked (e.g., password
   * reset).
   * 
   * The function uses a stream API to filter and find the most relevant token based
   * on the specified criteria, which includes whether the token is unused, has the
   * correct type, and matches the provided token value. If a matching token is found,
   * it is returned as an `Optional` object containing the token.
   * 
   * @param securityTokenType type of security token being searched for, and is used
   * to filter the user tokens in the stream to only those with the matching token type.
   * 
   * 	- `isUsed()`: This method returns a boolean indicating whether the token has been
   * used already or not.
   * 	- `getTokenType()`: This method returns the type of security token that was generated.
   * 	- `getToken()`: This method returns the actual security token value.
   * 	- `getExpiryDate()`: This method returns the date when the token expires.
   * 
   * These properties are used in the filter and findFirst methods to narrow down the
   * search to only valid tokens that match the specified criteria.
   * 
   * @returns an optional `SecurityToken` object representing the valid user token.
   * 
   * 	- `Optional<SecurityToken>`: The return type is an optional instance of
   * `SecurityToken`, indicating that the token may or may not be present.
   * 	- `userPasswordResetToken`: The variable contains a stream of user tokens associated
   * with the user, where each token is evaluated using the given filter criteria.
   * 	- `filter()`: This method filters the stream of user tokens based on the specified
   * conditions (i.e., !token.isUsed(), token.getTokenType() == securityTokenType,
   * token.getToken().equals(token), and tok.getExpiryDate().isAfter(LocalDate.now())).
   * 	- `findFirst()`: This method finds the first token in the stream that satisfies
   * all the conditions, or returns `Optional.empty()` if no token is found.
   * 	- `getExpiryDate()`: This method provides the expiration date of the found token,
   * which is compared with the current date using the `isAfter()` method.
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
   * creates a new user object based on a `UserDto` input, maps it to a `User` entity
   * using a mapper, and saves it to the repository for storage.
   * 
   * @param request UserDto object containing the details of the user to be created,
   * which is used by the `userMapper` to convert it into a `User` object before saving
   * it to the repository.
   * 
   * 1/ `request.getId()` - an integer attribute representing the user's ID.
   * 
   * @returns a `User` object saved to the repository.
   * 
   * 	- The `User` object represents a user that has been created in the repository.
   * 	- The `id` attribute is set to the value provided in the `request.getId()` method.
   * 	- The `log.trace()` statement logs a message indicating that the user has been
   * saved to the repository.
   */
  private User createUserInRepository(UserDto request) {
    User user = userMapper.userDtoToUser(request);
    log.trace("saving user with id[{}] to repository", request.getId());
    return userRepository.save(user);
  }

  /**
   * updates a user's email confirmation status to true, sends a notification to the
   * user's registered email address, and saves the updated user object in the repository.
   * 
   * @param user User object that contains the email address to be confirmed, and its
   * `setEmailConfirmed()` method sets the `emailConfirmed` field of the user to `true`,
   * while the `mailService.sendAccountConfirmed()` method sends a confirmation email
   * to the user's registered email address, and the `userRepository.save()` method
   * persists the updated user object in the database.
   * 
   * 	- `user`: A `User` object containing fields for email address, name, and other
   * relevant details.
   * 	- `setEmailConfirmed(true)`: Updates the `emailConfirmed` field of the `user`
   * object to indicate that the email address has been confirmed.
   */
  private void confirmEmail(User user) {
    user.setEmailConfirmed(true);
    mailService.sendAccountConfirmed(user);
    userRepository.save(user);
  }

  /**
   * encodes a user's password using a password encoder, then sets the encrypted password
   * as the user's encrypted password.
   * 
   * @param request UserDto object containing the user's password to be encrypted.
   * 
   * 	- `request.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));`:
   * The original password is encrypted using a password encoder and the resulting
   * encoded value is assigned to the `encryptedPassword` field of the `request` object.
   */
  private void encryptUserPassword(UserDto request) {
    request.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
  }

  /**
   * generates a unique user ID for a `UserDto` object using the `UUID.randomUUID()`
   * method and assigns it to the `UserDto` object's `userId` field.
   * 
   * @param request `UserDto` object containing information about a user that is used
   * to generate a unique ID for the user.
   * 
   * Request (class):
   * A DTO (Data Transfer Object) that contains information about a user to be created.
   * It has attributes such as `setUserId()` which is set to an UUID-generated string
   * value.
   */
  private void generateUniqueUserId(UserDto request) {
    request.setUserId(UUID.randomUUID().toString());
  }
}
