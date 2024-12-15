package com.myhome.services.springdatajpa;

import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;
import com.myhome.services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

/**
 * is responsible for implementing Mail Service functionality using Spring Data JPA.
 * It provides methods for sending password recover codes, account confirmation
 * messages, and password successfully changed messages to users, as well as an account
 * creation confirmation message. The class uses log messages to indicate successful
 * execution of each method.
 */
@Slf4j
@Service
@ConditionalOnProperty(value = "spring.mail.dev-mode", havingValue = "true", matchIfMissing = true)
public class DevMailSDJpaService implements MailService {

  /**
   * sends a password recovery code to a user via logging the event and returning `true`.
   * 
   * @param user user for whom the password recover code is being sent.
   * 
   * * `user.getUserId()`: This property returns the unique identifier of the user for
   * whom the password recover code is being sent.
   * 
   * The function then logs an information message using the `log.info()` method,
   * followed by the generated random code in parentheses. Finally, the function returns
   * a boolean value indicating successful execution.
   * 
   * @param randomCode 4-digit password recovery code that is sent to the user via email.
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
   * sends a message to a user with their ID indicating that their account has been confirmed.
   * 
   * @param user User object containing information about the user for whom an account
   * confirmation message is to be sent.
   * 
   * * `user.getUserId()` - Returns the user ID of the user for whom the account
   * confirmation message is to be sent.
   * 
   * @returns a message indicating that the account has been confirmed, along with the
   * user's ID.
   */
  @Override
  public boolean sendAccountConfirmed(User user) {
    log.info(String.format("Account confirmed message sent to user with id=%s", user.getUserId()));
    return true;
  }

  /**
   * sends a message to a user indicating that their password has been successfully changed.
   * 
   * @param user User object containing information about the user for whom the password
   * has been successfully changed.
   * 
   * * `user.getUserId()` returns the user ID of the user whose password has been
   * successfully changed.
   * 
   * @returns a message indicating that the user's password has been successfully changed.
   */
  @Override
  public boolean sendPasswordSuccessfullyChanged(User user) {
    log.info(String.format("Password successfully changed message sent to user with id=%s", user.getUserId()));
    return true;
  }


  /**
   * sends an account creation message to a user via logging an informational message
   * with the user ID and returning `true`.
   * 
   * @param user User object containing the information of the newly created account.
   * 
   * * `user.getUserId()` - This property returns the unique identifier of the user
   * whose account was created.
   * 
   * The function then logs an informational message using `log.info()` and returns `true`.
   * 
   * @param emailConfirmToken email confirmation token sent to the user for verification
   * after creating an account.
   * 
   * * `SecurityToken emailConfirmToken`: This object contains an email confirmation
   * token for the user's account creation. It is used to verify the user's email address
   * and activate their account.
   * 
   * @returns a boolean value indicating that the account creation message was sent to
   * the user successfully.
   */
  @Override
  public boolean sendAccountCreated(User user, SecurityToken emailConfirmToken) {
    log.info(String.format("Account created message sent to user with id=%s", user.getUserId()));
    return true;
  }


}
