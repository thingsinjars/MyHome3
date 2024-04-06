package com.myhome.services.springdatajpa;

import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;
import com.myhome.services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Slf4j
@Service
@ConditionalOnProperty(value = "spring.mail.dev-mode", havingValue = "true", matchIfMissing = true)
public class DevMailSDJpaService implements MailService {

  /**
   * sends a password recovery code to a specified user via log messages and returns `true`.
   * 
   * @param user User object containing information about the user for whom the password
   * recover code is being sent.
   * 
   * 	- `user`: A `User` object representing a user for whom a password recover code
   * is being sent. The `User` class has attributes such as `getUserId()` and `randomCode()`.
   * 
   * @param randomCode 6-digit password recover code sent to the user via email for
   * password recovery.
   * 
   * 	- `randomCode`: A String variable representing a unique code sent to the user for
   * password recovery.
   * 
   * The function then returns `true` indicating successful execution.
   * 
   * @returns a message indicating that the password recover code has been sent to the
   * specified user.
   */
  @Override
  public boolean sendPasswordRecoverCode(User user, String randomCode) throws MailSendException {
    log.info(String.format("Password recover code sent to user with id= %s, code=%s", user.getUserId()), randomCode);
    return true;
  }

  /**
   * sends a message to a user indicating that their account has been confirmed.
   * 
   * @param user User object containing information about the user whose account
   * confirmation message should be sent.
   * 
   * 	- `User Id`: A unique identifier for the user, typically an integer.
   * 	- `log`: An instance of `java.util.logging.Log`, used to log messages at different
   * levels.
   * 
   * @returns a boolean value indicating that the account confirmation message was sent
   * to the specified user.
   */
  @Override
  public boolean sendAccountConfirmed(User user) {
    log.info(String.format("Account confirmed message sent to user with id=%s", user.getUserId()));
    return true;
  }

  /**
   * sends a message to a user indicating that their password has been successfully changed.
   * 
   * @param user User object whose password has been successfully changed, and is used
   * to log the event and return a success message.
   * 
   * 	- `user`: A `User` object representing the user whose password has been successfully
   * changed. The object contains attributes such as `userId`, `oldPassword`, and `newPassword`.
   * 
   * @returns a message indicating that the user's password has been successfully
   * changed, along with the user's ID.
   */
  @Override
  public boolean sendPasswordSuccessfullyChanged(User user) {
    log.info(String.format("Password successfully changed message sent to user with id=%s", user.getUserId()));
    return true;
  }


  /**
   * sends an account creation confirmation message to a user via logging an informative
   * message and returning `true`.
   * 
   * @param user User object containing information about the newly created account.
   * 
   * 	- `user`: A `User` object with fields such as `getUserId()`, `getEmail()`, and `getSecurityToken()`.
   * 
   * @param emailConfirmToken email confirmation token sent to the user's registered
   * email address for account verification purposes.
   * 
   * 	- `SecurityToken emailConfirmToken`: This is an instance of `SecurityToken`, which
   * represents a secure token for verifying the user's identity. It contains information
   * such as a token value and a token type.
   * 
   * @returns a message indicating that an account has been created and sent to the user.
   */
  @Override
  public boolean sendAccountCreated(User user, SecurityToken emailConfirmToken) {
    log.info(String.format("Account created message sent to user with id=%s", user.getUserId()));
    return true;
  }


}
