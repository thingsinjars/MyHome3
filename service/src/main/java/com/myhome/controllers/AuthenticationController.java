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
 * is a Spring Boot REST controller that implements the AuthenticationApi interface.
 * It handles login requests and returns an HTTP response with the user ID and JWT
 * token for authentication. The class has one method, `login()`, which takes a valid
 * `LoginRequest` object as input and returns a ResponseEntity with the user ID and
 * JWT token.
 */
@RequiredArgsConstructor
@RestController
public class AuthenticationController implements AuthenticationApi {

  private final AuthenticationService authenticationService;

  /**
   * authenticates a user using the provided login request data and returns an
   * `ResponseEntity` with a `Void` body and custom headers containing authentication
   * data.
   * 
   * @param loginRequest authentication request containing the user credentials for
   * authentication verification and validation by the `authenticationService`.
   * 
   * 	- The `@Valid` annotation on the `LoginRequest` parameter indicates that the
   * object must be validated before it can be processed by the method.
   * 	- The `authenticationService` field is used to call the `login` method, which
   * takes the `LoginRequest` object as a parameter and returns an authentication data
   * object.
   * 
   * @returns a `ResponseEntity` object containing an `OK` status and custom headers
   * generated based on the `AuthenticationData` result.
   * 
   * 	- `ResponseEntity`: This is an instance of a class that represents a response
   * entity, which is a general-purpose response class in Spring WebFlux. It contains
   * information about the status code, headers, and body of the response.
   * 	- `ok()`: This is a method that returns a `ResponseEntity` instance with a status
   * code of 200 (OK), indicating that the login request was successful.
   * 	- `headers()`: This is a method that returns a list of headers, which are key-value
   * pairs that provide additional information about the response. In this case, the
   * headers contain information about the authentication data.
   * 	- `build()`: This is a method that builds the response entity by combining the
   * status code, headers, and body.
   * 
   * Overall, the output of the `login` function is a successful response with information
   * about the authentication data.
   */
  @Override
  public ResponseEntity<Void> login(@Valid LoginRequest loginRequest) {
    final AuthenticationData authenticationData = authenticationService.login(loginRequest);
    return ResponseEntity.ok()
        .headers(createLoginHeaders(authenticationData))
        .build();
  }

  /**
   * generates HTTP headers for login authentication, adding user ID and JWT token to
   * the headers.
   * 
   * @param authenticationData login data of a user, providing the user ID and JWT token
   * for authentication purposes.
   * 
   * 	- `getUserId()`: Retrieves the user ID associated with the authentication data.
   * 	- `getJwtToken()`: Retrieves the JWT token issued for the user ID.
   * 
   * @returns a set of HTTP headers containing the user ID and JWT token for authentication
   * purposes.
   * 
   * 	- `HttpHeaders`: This is an instance of the `HttpHeaders` class in Java, which
   * contains a collection of HTTP headers.
   * 	- `userId`: The value of this header is a string representing the user ID associated
   * with the login credentials provided in the `authenticationData` parameter.
   * 	- `token`: The value of this header is a string representing the JWT token issued
   * to the user for authentication purposes.
   */
  private HttpHeaders createLoginHeaders(AuthenticationData authenticationData) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("userId", authenticationData.getUserId());
    httpHeaders.add("token", authenticationData.getJwtToken());
    return httpHeaders;
  }
}
