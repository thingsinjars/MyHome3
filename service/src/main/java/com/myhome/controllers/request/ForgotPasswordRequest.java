package com.myhome.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

/**
 * represents a data class for storing email address, token, and new password for
 * forgot password functionality.
 * Fields:
 * 	- email (String): in the ForgotPasswordRequest class is of type String and
 * represents an email address associated with the requester's account.
 * 	- token (String): in the ForgotPasswordRequest class is likely used to store a
 * unique code or identifier for the user to reset their password.
 * 	- newPassword (String): in the ForgotPasswordRequest class represents a string
 * value that a user enters to reset their password.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgotPasswordRequest {
  @Email
  public String email;
  public String token;
  public String newPassword;
}
