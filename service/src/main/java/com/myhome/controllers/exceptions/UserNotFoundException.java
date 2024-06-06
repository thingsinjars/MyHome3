package com.myhome.controllers.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * is an extension of AuthenticationException with additional logging information
 * when a user is not found using their email address, using the @Slf4j annotation
 * for logging purposes.
 */
@Slf4j
public class UserNotFoundException extends AuthenticationException {
  public UserNotFoundException(String userEmail) {
    super();
    log.info("User not found - email: " + userEmail);
  }
}
