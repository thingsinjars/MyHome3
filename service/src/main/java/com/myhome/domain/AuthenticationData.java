package com.myhome.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * is used to store and represent authentication data for a user in a JSON Web Token
 * (JWT) format, including the user's ID and a unique token for verifying their identity.
 * Fields:
 * 	- jwtToken (String): represents a unique token issued to authenticated users by
 * the system.
 * 	- userId (String): in the AuthenticationData class represents a unique identifier
 * for a user.
 */
@Getter
@RequiredArgsConstructor
public class AuthenticationData {
  private final String jwtToken;
  private final String userId;
}
