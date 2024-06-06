package com.myhome.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * stores and represents authentication data for a user in a JSON Web Token (JWT)
 * format, including their unique token and identifier.
 * Fields:
 * 	- jwtToken (String): in the AuthenticationData class represents a unique token
 * issued by the system to authenticated users for verifying their identity.
 * 	- userId (String): in the AuthenticationData class represents a unique identifier
 * for a user.
 */
@Getter
@RequiredArgsConstructor
public class AuthenticationData {
  private final String jwtToken;
  private final String userId;
}
