package com.myhome.services;

import com.myhome.domain.AuthenticationData;
import com.myhome.model.LoginRequest;

/**
 * allows for the login of a user through an incoming LoginRequest and returns an
 * AuthenticationData value.
 */
public interface AuthenticationService {
  AuthenticationData login(LoginRequest loginRequest);
}
