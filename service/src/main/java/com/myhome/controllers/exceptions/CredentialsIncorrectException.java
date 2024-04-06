package com.myhome.controllers.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * extends AuthenticationException and provides a custom message for user Id when
 * credentials are incorrect.
 */
@Slf4j
public class CredentialsIncorrectException extends AuthenticationException {
  public CredentialsIncorrectException(String userId) {
    super();
    log.info("Credentials are incorrect for userId: " + userId);
  }
}
