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

package com.myhome.controllers;

import com.myhome.api.UsersApi;
import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.mapper.HouseMemberMapper;
import com.myhome.controllers.mapper.UserApiMapper;
import com.myhome.domain.PasswordActionType;
import com.myhome.domain.User;
import com.myhome.model.CreateUserRequest;
import com.myhome.model.CreateUserResponse;
import com.myhome.model.ForgotPasswordRequest;
import com.myhome.model.GetUserDetailsResponse;
import com.myhome.model.GetUserDetailsResponseUser;
import com.myhome.model.ListHouseMembersResponse;
import com.myhome.services.HouseService;
import com.myhome.services.UserService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * Controller for facilitating user actions.
 */
/**
 * TODO
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController implements UsersApi {

  private final UserService userService;
  private final UserApiMapper userApiMapper;
  private final HouseService houseService;
  private final HouseMemberMapper houseMemberMapper;

  /**
   * receives a `CreateUserRequest` object, creates a `UserDto` object using the provided
   * request data, and then either creates a new user or returns a conflict status code
   * if the user already exists.
   * 
   * @param request `CreateUserRequest` object passed from the client, containing the
   * user's details for creation.
   * 
   * The `@Valid` annotation on the `CreateUserRequest` parameter indicates that the
   * request body must contain valid JSON data in accordance with the provided schema.
   * The `log.trace()` statement logs a trace message indicating that the sign-up request
   * was received.
   * 
   * The `userApiMapper.createUserRequestToUserDto(request)` method converts the
   * `CreateUserRequest` object into a `UserDto` object, which contains the user's
   * details in a more structured format for further processing.
   * 
   * The `Optional<UserDto>` variable `createdUserDto` stores the result of calling the
   * `userService.createUser(requestUserDto)` method, which creates a new user in the
   * system. If the user creation was successful, this variable will contain the created
   * `UserDto` object; otherwise, it will be empty (i.e., `Optional<UserDto> empty = ...`).
   * 
   * Finally, the `map()` method is used to transform the `createdUserDto` into a
   * `CreateUserResponse` object using the `userApiMapper.userDtoToCreateUserResponse(userDto)`
   * method. The resulting `ResponseEntity` object is then constructed with a status
   * code of `HttpStatus.CREATED` and the created `CreateUserResponse` object as its
   * body. If the user creation failed, an `Optional<ResponseEntity>` object will be
   * constructed containing a `ResponseEntity` object with a status code of `HttpStatus.CONFLICT`.
   * 
   * @returns a `ResponseEntity` object with a status code of `CREATED` and the created
   * user's response details.
   * 
   * 	- `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response to a HTTP request. The status code of the response is specified
   * in the `status` field, which can be either `CREATED`, `CONFLICT`, or any other
   * valid HTTP status code.
   * 	- `body`: This is a reference to the actual response body, which in this case is
   * an instance of the `CreateUserResponse` class. The `body` field cannot be null.
   * 
   * In summary, the output of the `signUp` function is a `ResponseEntity` object with
   * a non-null `body` field containing a `CreateUserResponse`.
   */
  @Override
  public ResponseEntity<CreateUserResponse> signUp(@Valid CreateUserRequest request) {
    log.trace("Received SignUp request");
    UserDto requestUserDto = userApiMapper.createUserRequestToUserDto(request);
    Optional<UserDto> createdUserDto = userService.createUser(requestUserDto);
    return createdUserDto
        .map(userDto -> {
          CreateUserResponse response = userApiMapper.userDtoToCreateUserResponse(userDto);
          return ResponseEntity.status(HttpStatus.CREATED).body(response);
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
  }

  /**
   * receives a pageable request from the client, retrieves all users from the database
   * using the `userService`, maps them to a response entity using the `userApiMapper`,
   * and returns it to the client with a status code of OK.
   * 
   * @param pageable page number and page size for listing all users, allowing for
   * efficient pagination of the user list.
   * 
   * 	- `log.trace()` - Traces the method execution for logging purposes.
   * 	- `userService.listAll(pageable)` - Calls the `listAll` method of the `userService`
   * object, passing in the `pageable` input as a parameter.
   * 	- `userApiMapper.userSetToRestApiResponseUserSet(userDetails)` - Maps the user
   * details to a response format using the `userApiMapper`.
   * 	- `GetUserDetailsResponse response = new GetUserDetailsResponse()` - Creates a
   * new instance of the `GetUserDetailsResponse` class.
   * 	- `response.setUsers(userDetailsResponse)` - Sets the users property of the
   * response object to the mapped user details.
   * 
   * @returns a list of user details in REST API format.
   * 
   * 	- `GetUserDetailsResponse`: This is the class that represents the response to the
   * list all users request. It has a set of `User` objects as its attribute, which
   * contain the details of each user.
   * 	- `setUsers()`: This method sets the `User` object set in the response.
   * 	- `HttpStatus.OK`: This is the HTTP status code returned by the function, indicating
   * that the request was successful.
   * 	- `body()`: This method returns the response body, which contains the list of
   * users in the form of a `GetUserDetailsResponse`.
   */
  @Override
  public ResponseEntity<GetUserDetailsResponse> listAllUsers(Pageable pageable) {
    log.trace("Received request to list all users");

    Set<User> userDetails = userService.listAll(pageable);
    Set<GetUserDetailsResponseUser> userDetailsResponse =
        userApiMapper.userSetToRestApiResponseUserSet(userDetails);

    GetUserDetailsResponse response = new GetUserDetailsResponse();
    response.setUsers(userDetailsResponse);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * receives a user ID as input and returns a `ResponseEntity` object containing the
   * details of the user with that ID, processed through the `userService` and
   * `userApiMapper` objects.
   * 
   * @param userId unique identifier of the user for whom details are requested.
   * 
   * 	- `log.trace()` records a log message about receiving a request to retrieve user
   * details with the specified `userId`.
   * 
   * @returns a `ResponseEntity` object with an HTTP status code of OK and the user
   * details in the body.
   * 
   * 	- The `ResponseEntity` object is constructed with a status code of `HttpStatus.OK`,
   * indicating that the request was successful.
   * 	- The `body` property of the `ResponseEntity` object contains the actual response
   * data, which is a `GetUserDetailsResponseUser` object in this case. This object
   * represents the user details returned by the function.
   * 	- The `map` method is used to transform the `userDtoToGetUserDetailsResponse`
   * object into a `GetUserDetailsResponseUser` object. This transformation is performed
   * using the `userApiMapper` function, which is not explicitly defined in the code
   * snippet provided.
   */
  @Override
  public ResponseEntity<GetUserDetailsResponseUser> getUserDetails(String userId) {
    log.trace("Received request to get details of user with Id[{}]", userId);

    return userService.getUserDetails(userId)
        .map(userApiMapper::userDtoToGetUserDetailsResponse)
        .map(response -> ResponseEntity.status(HttpStatus.OK).body(response))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  /**
   * takes a `String` action and a `@Valid @RequestBody ForgotPasswordRequest` object
   * as input, and based on the value of the `action` parameter, performs a password
   * reset or sends an email to reset the password. If successful, it returns a
   * `ResponseEntity` with a status code of `OK`.
   * 
   * @param action type of password action to be performed, which determines the
   * corresponding logic to be executed within the function.
   * 
   * 	- `@NotNull` and `@Valid` annotations on `action` indicate that it must be provided
   * as a non-null and valid JSON object.
   * 	- `parsedAction` is a field that stores the parsed value of `action`. It is
   * initialized to `PasswordActionType.valueOf(action)` using the `PasswordActionType.valueOf()`
   * method, which parses the value of `action` based on its string representation.
   * 	- `result` is a boolean variable that indicates whether the operation was successful.
   * It is set to `true` if the operation was successful, and `false` otherwise.
   * 
   * In summary, the `action` input parameter has two properties: it must be provided
   * as a non-null and valid JSON object, and its value is parsed using the
   * `PasswordActionType.valueOf()` method to determine the type of action being performed.
   * 
   * @param forgotPasswordRequest password reset request from the user, which contains
   * the username and other necessary information to initiate the password reset process.
   * 
   * 	- `action`: A string indicating the type of password action being performed, which
   * can be either `FORGOT` or `RESET`.
   * 	- `forgotPasswordRequest`: An object containing information about the user's
   * forgotten password request, including their username and email address.
   * 
   * @returns a `ResponseEntity` object with a status code of either `OK` or `BAD_REQUEST`,
   * depending on whether the password reset was successful or not.
   * 
   * 	- `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response message to a HTTP request. It has a `ok()` method that returns
   * a `ResponseEntity` with a status code of 200 (OK) and a `badRequest()` method that
   * returns a `ResponseEntity` with a status code of 400 (BAD REQUEST).
   * 	- `Void`: This is the type of the value returned by the function, which means it
   * has no value or is void.
   * 
   * The various attributes of the output are:
   * 
   * 	- `result`: This is a boolean attribute that indicates whether the password reset
   * was successful or not. It is set to `true` if the password was successfully reset,
   * and `false` otherwise.
   * 	- `parsedAction`: This is an instance of the `PasswordActionType` class, which
   * represents the action taken by the function. It can take on one of the following
   * values: `FORGOT`, `RESET`, or `UNKNOWN`.
   */
  @Override
  public ResponseEntity<Void> usersPasswordPost(@NotNull @Valid String action, @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
    boolean result = false;
    PasswordActionType parsedAction = PasswordActionType.valueOf(action);
    if (parsedAction == PasswordActionType.FORGOT) {
      result = true;
      userService.requestResetPassword(forgotPasswordRequest);
    } else if (parsedAction == PasswordActionType.RESET) {
      result = userService.resetPassword(forgotPasswordRequest);
    }
    if (result) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * receives a user ID and pageable parameters, queries the `houseService` for all
   * houses associated with the user, maps the resulting house members to a
   * `RestApiResponseHouseMemberSet`, and returns a `ResponseEntity` object representing
   * the list of house members.
   * 
   * @param userId ID of the user whose houses are to be listed.
   * 
   * 	- `userId`: String representing the user ID for whom all houses' members will be
   * listed.
   * 
   * @param pageable pagination information for retrieving a list of house members,
   * allowing the method to return a limited number of results per page.
   * 
   * 	- `userId`: A String representing the user whose houses to list members for.
   * 	- `pageable`: A Pageable object that can be used to specify pagination parameters
   * for the list request. The pageable object has various attributes, including:
   * 	+ `pageNumber`: An Integer indicating the current page number being retrieved.
   * 	+ `pageSize`: An Integer representing the number of members to display per page.
   * 	+ `sort`: A String representing the field to sort the results by (optional).
   * 	+ `direction`: A String representing the direction of the sort (optional).
   * 
   * @returns a `ResponseEntity` object representing a list of house members for the
   * specified user.
   * 
   * 	- `ResponseEntity`: This is the outermost class in the chain of method calls that
   * returns the final response entity. It contains an `ok` field that indicates whether
   * the request was successful or not. If it's not `ok`, then the response entity will
   * be a `NotFound` entity with a build() method that creates the response.
   * 	- `ListHouseMembersResponse`: This is a inner class of `ResponseEntity`. It
   * contains a `members` field that holds a list of `HouseMemberSet` objects, which
   * are converted from `houseService.listHouseMembersForHousesOfUserId(userId, pageable)`
   * using the `map()` method.
   * 	- `HouseMemberSet`: This is an inner class of `ListHouseMembersResponse`. It
   * contains a list of `HouseMember` objects, which are converted from the
   * `houseMemberMapper.houseMemberSetToRestApiResponseHouseMemberSet()` method using
   * the `map()` method.
   * 	- `House Member`: This is an inner class of `HouseMemberSet`. It contains the
   * details of a single house member, such as their name and role in the household.
   */
  @Override
  public ResponseEntity<ListHouseMembersResponse> listAllHousemates(String userId, Pageable pageable) {
    log.trace("Received request to list all members of all houses of user with Id[{}]", userId);

    return houseService.listHouseMembersForHousesOfUserId(userId, pageable)
            .map(HashSet::new)
            .map(houseMemberMapper::houseMemberSetToRestApiResponseHouseMemberSet)
            .map(houseMembers -> new ListHouseMembersResponse().members(houseMembers))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  /**
   * verifies if an email address is confirmed for a user by querying the user service.
   * If the email address is confirmed, it returns an `OK` response entity, otherwise
   * it returns a `BAD_REQUEST` response entity.
   * 
   * @param userId unique identifier of the user whose email is being confirmed.
   * 
   * 	- `userId`: A string representing the user ID for whom email confirmation is being
   * performed.
   * 
   * @param emailConfirmToken 12-digit token generated by the email confirmation service
   * to verify the user's email address.
   * 
   * 	- `userId`: A string representing the user ID.
   * 	- `emailConfirmToken`: A token generated by the server to confirm the email address
   * of a user.
   * 
   * @returns a `ResponseEntity` object with a status code of either `ok` or `badRequest`,
   * depending on whether the email confirmation was successful or not.
   * 
   * 	- `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response message in a web service. It has an `ok` method that returns
   * a `ResponseEntity` object with a status code of 200 and a `badRequest` method that
   * returns a `ResponseEntity` object with a status code of 400.
   * 	- `Void`: This is the type of the entity in the `ResponseEntity` object. It
   * represents the absence of an entity, which means that the function does not return
   * any data.
   * 
   * The `confirmEmail` function returns a `ResponseEntity` object with a status code
   * of 200 if the email confirmation was successful, or a status code of 400 if there
   * was an error. The function does not provide any additional information about the
   * result beyond the status code.
   */
  @Override
  public ResponseEntity<Void> confirmEmail(String userId, String emailConfirmToken) {
    boolean emailConfirmed = userService.confirmEmail(userId, emailConfirmToken);
    if(emailConfirmed) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * resends an email confirmation to a user if one was previously sent and failed, or
   * returns a bad request if no email confirmation was sent.
   * 
   * @param userId ID of the user whose email confirmation should be resent.
   * 
   * 	- `userService`: An instance of `UserService`, which is likely to be an abstract
   * class or interface that provides methods for managing users in the application.
   * 	- `resendEmailConfirm`: A boolean value indicating whether the email confirmation
   * was resent successfully or not.
   * 
   * @returns an `OkResponseEntity` indicating successful resending of the email
   * confirmation to the user.
   * 
   * 	- `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response to a RESTful API request.
   * 	- `ok`: The status code of the response is set to `OK`, indicating that the email
   * confirmation was successfully resent.
   * 	- `build`: The response entity is built with the specified properties.
   */
  @Override
  public ResponseEntity<Void> resendConfirmEmailMail(String userId) {
    boolean emailConfirmResend = userService.resendEmailConfirm(userId);
    if(emailConfirmResend) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }
}
