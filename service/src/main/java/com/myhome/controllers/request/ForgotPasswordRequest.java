package com.myhome.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

/**
 * represents an email address, token, and new password for forgot password functionality.
 * Fields:
 * 	- email (String): in the ForgotPasswordRequest class represents an email address
 * associated with the user's account.
 * 	- token (String): in the ForgotPasswordRequest class represents a unique code or
 * identifier for the user to reset their password.
 * 	- newPassword (String): in the ForgotPasswordRequest class represents a string
 * value entered by the user to reset their password.
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
