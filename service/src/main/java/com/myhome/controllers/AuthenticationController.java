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
 * token for authentication purposes. The class has one method, `login()`, which takes
 * a valid `LoginRequest` object as input and returns a ResponseEntity with the user
 * ID and JWT token for authentication.
 */
@RequiredArgsConstructor
@RestController
public class AuthenticationController implements AuthenticationApi {

  private final AuthenticationService authenticationService;

  /**
   * authenticates a user by calling the `loginService` and returning an `ResponseEntity`
   * with a success status code and headers containing information about the authenticated
   * user.
   * 
   * @param loginRequest authentication request, containing the user credentials and
   * other relevant information for the login process.
   * 
   * AuthenticationData authenticationData = authenticationService.login(loginRequest);
   * 
   * @returns a `ResponseEntity` object with a `headers` field containing authentication-related
   * headers.
   * 
   * * `ResponseEntity`: This is an instance of the `ResponseEntity` class, which
   * represents a response object that contains both a body and headers.
   * * `ok()`: The `ok()` method returns a `ResponseEntity` instance with a status code
   * of 200 (OK), indicating that the login operation was successful.
   * * `headers`: This is an instance of the `Headers` class, which contains metadata
   * about the response, such as caching and security headers.
   * * `build()`: The `build()` method is used to construct the complete response object,
   * by combining the `ResponseEntity`, `Headers`, and any other metadata required for
   * the operation.
   */
  @Override
  public ResponseEntity<Void> login(@Valid LoginRequest loginRequest) {
    final AuthenticationData authenticationData = authenticationService.login(loginRequest);
    return ResponseEntity.ok()
        .headers(createLoginHeaders(authenticationData))
        .build();
  }

  /**
   * creates an HTTP headers object containing user ID and JWT token for login
   * authentication purposes based on input `AuthenticationData`.
   * 
   * @param authenticationData user's identification information, including their user
   * ID and JWT token, which are used to create the HTTP headers for login authentication.
   * 
   * * `getUserId`: A string representing the user ID associated with the authentication
   * request.
   * * `getJwtToken`: A string representing the JWT token issued to the user for login
   * purposes.
   * 
   * @returns a set of HTTP headers containing the user ID and JWT token for authentication
   * purposes.
   * 
   * * `HttpHeaders`: This is an instance of the `HttpHeaders` class from the `java.net.http`
   * package. It contains key-value pairs representing HTTP headers that can be used
   * in a request or response.
   * * `add()` methods: The `add()` methods are used to add new header fields to the
   * `HttpHeaders` instance. In this function, two such methods are called, one for
   * adding a header field with the key "userId" and another for adding a header field
   * with the key "token".
   * 
   * The attributes of the returned output can be inferred from the method name and the
   * code inside it. The `createLoginHeaders` function takes an `AuthenticationData`
   * object as input and returns an instance of `HttpHeaders`. This suggests that the
   * output is related to authentication and can be used in a request or response to
   * provide authentication information.
   */
  private HttpHeaders createLoginHeaders(AuthenticationData authenticationData) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("userId", authenticationData.getUserId());
    httpHeaders.add("token", authenticationData.getJwtToken());
    return httpHeaders;
  }
}
