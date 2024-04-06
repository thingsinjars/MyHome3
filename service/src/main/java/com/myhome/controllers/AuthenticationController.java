package com.myhome.controllers;

import com.myhome.api.AuthenticationApi;
import com.myhome.domain.AuthenticationData;
import com.myhome.model.LoginRequest;
import com.myhome.services.AuthenticationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 */
@RequiredArgsConstructor
@RestController
public class AuthenticationController implements AuthenticationApi {

  private final AuthenticationService authenticationService;

  /**
   * handles user authentication by calling the `loginService` to generate an authentication
   * response, which is then returned as a `ResponseEntity`.
   * 
   * @param loginRequest логин request to be processed by the `login()` method, providing
   * the necessary data for authentication.
   * 
   * 	- `AuthenticationData authenticationData`: This is the result of the `login`
   * function's internal logic, which is not explicitly stated in the code snippet provided.
   * 
   * @returns a `ResponseEntity` object with an `OK` status and custom headers containing
   * authentication data.
   * 
   * 	- `ResponseEntity`: This is an instance of `ResponseEntity`, which is a generic
   * class that represents a response entity in Spring WebFlux. It contains a `body`
   * property that can be set to any type that implements `HttpEntity`.
   * 	- `ok()` method: This is a static method on the `ResponseEntity` class that returns
   * a `ResponseEntity` instance with a status code of 200 (OK).
   * 	- `headers()` method: This is another static method on the `ResponseEntity` class
   * that allows you to set custom headers on the response. In this case, the `headers()`
   * method is called with an instance of `HttpHeaders`, which contains various header
   * fields such as `Content-Type`, `Set-Cookie`, and `Expires`.
   * 	- `build()` method: This is a factory method on the `ResponseEntity` class that
   * creates a new instance of the response entity based on the properties specified
   * in its constructor. In this case, the `build()` method takes an instance of
   * `AuthenticationData` as a parameter and uses it to create a new instance of
   * `ResponseEntity` with the appropriate headers and status code.
   */
  @Override
  public ResponseEntity<Void> login(@Valid LoginRequest loginRequest) {
    final AuthenticationData authenticationData = authenticationService.login(loginRequest);
    return ResponseEntity.ok()
        .headers(createLoginHeaders(authenticationData))
        .build();
  }

  /**
   * creates HTTP headers with user ID and JWT token obtained from the `AuthenticationData`
   * object passed as a parameter.
   * 
   * @param authenticationData user's login information, providing the user ID and JWT
   * token used for authentication.
   * 
   * 	- `getUserId()`: retrieves the user ID from the input data.
   * 	- `getJwtToken()`: retrieves the JWT token from the input data.
   * 
   * @returns an HTTP headers object containing the user ID and JWT token of the
   * authenticated user.
   * 
   * 	- `HttpHeaders`: This is an instance of the `HttpHeaders` class in Java, which
   * is a map of headers that can be added to an HTTP request or response.
   * 	- `userId`: This is an integer value that represents the user ID of the authenticated
   * user.
   * 	- `token`: This is a string value that represents the JWT token issued to the
   * user for authentication purposes.
   */
  private HttpHeaders createLoginHeaders(AuthenticationData authenticationData) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("userId", authenticationData.getUserId());
    httpHeaders.add("token", authenticationData.getJwtToken());
    return httpHeaders;
  }
}
