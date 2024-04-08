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
 * is a REST controller that provides endpoints for managing users, houses, and email
 * confirmation. It receives requests to create new users, list all users, get details
 * of a specific user, reset passwords, and confirm or resend email confirmations.
 * The class uses dependency injection and maps the responses from the service layers
 * to the corresponding REST API responses.
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
   * handles a `CreateUserRequest` and creates a new user in the system, mapping the
   * user entity to a `CreateUserResponse`. If the creation is successful, it returns
   * a `ResponseEntity` with a status code of `CREATED`, otherwise it returns a
   * `ResponseEntity` with a status code of `CONFLICT`.
   * 
   * @param request `CreateUserRequest` object passed to the method, which contains the
   * user's details for creation.
   * 
   * 	- `@Valid`: This annotation indicates that the input object `request` is validated
   * by the framework.
   * 	- `CreateUserRequest`: This is the class that represents the request body sent
   * by the client. It contains attributes such as `username`, `email`, `password`, and
   * `nickname`.
   * 	- `userApiMapper`: This is an injected class that performs mapping between the
   * request body and the desired response format.
   * 	- `UserDto`: This is a class representing the user entity, which is created or
   * updated in the function.
   * 	- `userService`: This is an injected class that provides the functionality to
   * create or update a user.
   * 	- `Optional<UserDto>`: This represents the result of calling `createUser` on the
   * `userService`, which may return `None` if the user already exists.
   * 
   * @returns a `ResponseEntity` object with a status code of `HTTP_CREATED` and a body
   * containing a `CreateUserResponse`.
   * 
   * 	- `ResponseEntity<CreateUserResponse>` is a generic class that represents a
   * response entity with a status code and a body. The status code indicates whether
   * the request was successful (201 Created) or not (409 Conflict).
   * 	- `CreateUserResponse` is a class that contains the data returned to the client
   * after a successful sign-up operation. It has properties for the user's ID, name,
   * email, and password.
   * 	- `map` method is used to transform the `Optional<UserDto>` into a `ResponseEntity`
   * with a status code and a body. If the `Optional` contains a value, the method
   * returns a response entity with a status code of 201 Created and a body containing
   * the transformed `CreateUserResponse`. Otherwise, it returns a response entity with
   * a status code of 409 Conflict.
   * 	- `orElseGet` method is used to provide an alternative response entity if the
   * `map` method does not produce one. In this case, the alternative response entity
   * has a status code of 409 Conflict.
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
   * receives a `Pageable` parameter and returns a response with a list of users fetched
   * from the user service and mapped to the API response format using the `userApiMapper`.
   * 
   * @param pageable pagination information for listing all users, allowing the
   * listAllUsers method to retrieve a subset of user details from the database based
   * on the specified page number and page size.
   * 
   * 	- `log.trace("Received request to list all users")` - This line logs a trace
   * message indicating that the function has received a request to list all users.
   * 	- `Set<User> userDetails = userService.listAll(pageable);` - This line calls the
   * `listAll` method of the `userService` class, passing in `pageable` as a parameter.
   * The `listAll` method returns a set of `User` objects representing all users in the
   * system.
   * 	- `Set<GetUserDetailsResponseUser> userDetailsResponse =
   * userApiMapper.userSetToRestApiResponseUserSet(userDetails);` - This line maps the
   * `User` objects returned by the `listAll` method to a set of `GetUserDetailsResponseUser`
   * objects using the `userApiMapper` class. The resulting `userDetailsResponse` set
   * represents the users in the system in a format suitable for return as part of the
   * API response.
   * 	- `GetUserDetailsResponse response = new GetUserDetailsResponse();` - This line
   * creates a new instance of the `GetUserDetailsResponse` class, which is the response
   * object for this API endpoint.
   * 	- `response.setUsers(userDetailsResponse);` - This line sets the `users` field
   * of the `GetUserDetailsResponse` object to the `userDetailsResponse` set, thereby
   * populating the response with the mapped users.
   * 
   * @returns a list of user details in REST API format.
   * 
   * 	- `response`: This is the main output of the function, which contains a set of
   * `GetUserDetailsResponseUser` objects representing all the users in the system.
   * 	- `users`: This is a Set containing the user details, where each user detail is
   * represented as a `GetUserDetailsResponseUser` object.
   * 	- `HttpStatus.OK`: This is the status code of the response, indicating that the
   * request was successful.
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
   * receives a user ID and returns a `ResponseEntity` with a `GetUserDetailsResponseUser`
   * object containing user details or a `HttpStatus.NOT_FOUND` status code if the user
   * is not found.
   * 
   * @param userId ID of the user for whom the details are being requested.
   * 
   * @returns a `ResponseEntity` object with an HTTP status code of OK and a body
   * containing the user details.
   * 
   * 	- The response entity is of type `ResponseEntity`, which indicates that it contains
   * a result and an HTTP status code.
   * 	- The status code is set to `HttpStatus.OK`, indicating that the request was successful.
   * 	- The body of the response entity contains an instance of `GetUserDetailsResponse`,
   * which represents the details of the user requested. This class has a single property,
   * `userDto`, which is a `UserDTO` object representing the user's details.
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
   * processes password reset requests. It checks if the action is FORGOT or RESET, and
   * if the request is valid, it calls the corresponding method in the `userService`
   * to reset the password. If the password is successfully reset, it returns a
   * `ResponseEntity` with a status code of `OK`. Otherwise, it returns a `ResponseEntity`
   * with a status code of `BAD_REQUEST`.
   * 
   * @param action password action type, which determines the corresponding action to
   * be taken by the method.
   * 
   * @param forgotPasswordRequest Forgot Password Request object that contains the
   * user's email address and other information required to initiate the password reset
   * process.
   * 
   * 	- `@NotNull`: The `action` parameter must not be null.
   * 	- `@Valid`: The `forgotPasswordRequest` parameter must be validated by the framework.
   * 	- `@RequestBody`: The `forgotPasswordRequest` parameter is passed as a request
   * body in the HTTP request.
   * 	- `ForgotPasswordRequest`: This class represents the request body for resetting
   * a user's password. It contains properties such as:
   * 	+ `email`: The email address of the user to whom the password should be reset.
   * 	+ `password`: The new password to be set.
   * 
   * @returns a `ResponseEntity` object with a status code of either `ok` or `badRequest`,
   * depending on the success of the password reset process.
   * 
   * 	- `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response to a RESTful API request. It contains information about the
   * status of the response, such as whether it was successful or not, and any additional
   * details that may be useful for handling the response.
   * 	- `ok`: This is a boolean attribute of the `ResponseEntity` class, indicating
   * whether the response was successful (i.e., true) or not (i.e., false).
   * 	- `build`: This is a method of the `ResponseEntity` class that returns a new
   * instance of the `ResponseEntity` class with the specified attributes. In this case,
   * it returns a new instance of the `ResponseEntity` class with the `ok` attribute
   * set to true.
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
   * receives a user ID and a pageable parameter, retrieves the list of house members
   * for all houses associated with the given user ID, maps them to a `HashSet`, and
   * returns a `ResponseEntity` object representing the list of house members.
   * 
   * @param userId user for whom the list of housemates is being requested.
   * 
   * @param pageable page number and page size for fetching a subset of the list of
   * house members, allowing for efficient pagination and result set retrieval.
   * 
   * 	- `userId`: The ID of the user whose houses are to be listed.
   * 	- `pageable`: A `Pageable` object representing the pagination parameters for
   * listing all housemates. The various properties and attributes of `pageable` include:
   * 	+ `pageNumber`: The current page number being requested (optional)
   * 	+ `pageSize`: The number of housemates to be listed per page (optional)
   * 	+ `sort`: The field by which the list of housemates should be sorted (optional)
   * 	+ `direction`: The direction of the sort order (optional)
   * 
   * @returns a `ResponseEntity` object containing a list of `HouseMemberSet` objects,
   * representing all members of all houses belonging to the specified user.
   * 
   * 	- `ResponseEntity`: This is the type of the outermost component of the return
   * value, indicating whether the call was successful or not. In this case, it is `ok`,
   * indicating a successful response.
   * 	- `ListHouseMembersResponse`: This is the inner component of the return value,
   * representing the list of house members returned by the function. It contains a
   * list of `HouseMemberSet` objects, which are the actual data returned by the function.
   * 	- `members`: This is a list of `HouseMemberSet` objects, each representing a set
   * of house members for a particular house. The `House Member Set` class has several
   * properties, including the `houseId`, `userId`, and `members` fields.
   * 
   * Overall, the `listAllHousemates` function returns a list of house members belonging
   * to the user with the specified ID, organized into sets for each house.
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
   * confirms an email address for a user using a confirmation token provided by the
   * server. If the email is confirmed, a `ResponseEntity.ok()` is returned. Otherwise,
   * a `ResponseEntity.badRequest()` is returned.
   * 
   * @param userId identity of the user whose email confirmation is being checked.
   * 
   * @param emailConfirmToken token sent to the user's email address for confirmation
   * of their email address.
   * 
   * @returns a `ResponseEntity` object with a status of either `ok` or `badRequest`,
   * depending on whether the email confirmation was successful or not.
   * 
   * The `ResponseEntity` object is an instance of the `ResponseEntity` class, which
   * represents a response entity in a web application. The `ok` method returns a
   * ResponseEntity with a status code of 200 (OK), indicating that the email confirmation
   * was successful. On the other hand, the `badRequest` method returns a ResponseEntity
   * with a status code of 400 (Bad Request), indicating that there was an error in
   * processing the request.
   * 
   * The `Void` parameter represents the lack of any content returned by the function.
   * It is a type parameter passed to the `ResponseEntity` constructor, indicating that
   * no content is being returned.
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
   * resends an email confirmation to a user if one was previously sent, and returns a
   * response entity indicating the result of the operation.
   * 
   * @param userId identifier of the user for whom an email confirmation link is to be
   * resent.
   * 
   * @returns an `OK` response entity indicating successful email resending for the
   * specified user ID.
   * 
   * 	- `ResponseEntity`: This is an object that represents the response of the API.
   * It has several attributes, including the status code (either 200 for success or a
   * non-200 status code for failure), the body of the response (which can be empty or
   * contain data depending on the response), and the headers of the response.
   * 	- `ok`: This is a boolean attribute that indicates whether the resending of the
   * email confirmation was successful or not. If `emailConfirmResend` is true, then
   * the response entity has an `statusCode` of 200 and an empty body. Otherwise, it
   * has a status code of 400 (bad request) and an empty body.
   * 	- `build`: This is a method that creates a new instance of `ResponseEntity` with
   * the specified attributes. It is called automatically when the function returns an
   * instance of `ResponseEntity`.
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
