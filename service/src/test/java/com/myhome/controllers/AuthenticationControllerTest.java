package com.myhome.controllers;

import com.myhome.domain.AuthenticationData;
import com.myhome.model.LoginRequest;
import com.myhome.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * TODO
 */
public class AuthenticationControllerTest {

  private static final String TEST_ID = "1";
  private static final String TEST_EMAIL = "email@mail.com";
  private static final String TEST_PASSWORD = "password";
  private static final String TOKEN = "token";

  @Mock
  private AuthenticationService authenticationService;
  @InjectMocks
  private AuthenticationController authenticationController;

  /**
   * initializes Mockito annotations for the class, enabling mocking of dependencies
   * and behaviors.
   */
  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * tests the login functionality of the `AuthenticationController`. It provides a
   * default `LoginRequest` and `AuthenticationData`, mocks the `authenticationService`
   * to return the `AuthenticationData`, and verifies the response status code, headers,
   * and method call.
   */
  @Test
  void loginSuccess() {
    // given
    LoginRequest loginRequest = getDefaultLoginRequest();
    AuthenticationData authenticationData = getDefaultAuthenticationData();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("userId", authenticationData.getUserId());
    httpHeaders.add("token", authenticationData.getJwtToken());
    given(authenticationService.login(loginRequest))
        .willReturn(authenticationData);

    // when
    ResponseEntity<Void> response = authenticationController.login(loginRequest);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(response.getHeaders().size(), 2);
    assertEquals(response.getHeaders(), httpHeaders);
    verify(authenticationService).login(loginRequest);
  }

  /**
   * generates a default login request with predefined email and password for testing
   * purposes.
   * 
   * @returns a `LoginRequest` object containing the email address "TEST_EMAIL" and the
   * password "TEST_PASSWORD".
   * 
   * 	- The `email` field is set to `TEST_EMAIL`, representing an email address for authentication.
   * 	- The `password` field is set to `TEST_PASSWORD`, indicating a password for authentication.
   * 
   * Overall, the function returns a new `LoginRequest` object with predefined values
   * for the email and password fields.
   */
  private LoginRequest getDefaultLoginRequest() {
    return new LoginRequest().email(TEST_EMAIL).password(TEST_PASSWORD);
  }

  /**
   * creates a new `AuthenticationData` object with the default token and test ID.
   * 
   * @returns an `AuthenticationData` object containing the token "TOKEN" and the test
   * ID "TEST_ID".
   * 
   * 	- `TOKEN`: A string value representing an authentication token.
   * 	- `TEST_ID`: An integer value signifying a test ID for the authentication data.
   */
  private AuthenticationData getDefaultAuthenticationData() {
    return new AuthenticationData(TOKEN, TEST_ID);
  }
}
